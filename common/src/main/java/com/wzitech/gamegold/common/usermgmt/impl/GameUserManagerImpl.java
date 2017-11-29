package com.wzitech.gamegold.common.usermgmt.impl;

import com.kb5173.framework.shared.exception.BusinessException;
import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.dao.IMainStationKeyRedisDAO;
import com.wzitech.gamegold.common.usermgmt.IGameUserManager;
import com.wzitech.gamegold.common.usermgmt.entity.IpContext;
import com.wzitech.gamegold.common.usermgmt.entity.MainStationConstant;
import com.wzitech.gamegold.common.usermgmt.entity.MainStationKeyEO;
import com.wzitech.gamegold.common.usermgmt.entity.SRRequsestTokenResponse;
import com.wzitech.gamegold.common.utils.HttpConnectionUtils;
import com.wzitech.gamegold.common.utils.MainStationUtil;
import com.wzitech.gamegold.common.utils.SpitConstant;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 5173注册用户管理
 *
 * @author SunChengfei
 */
@Component
public class GameUserManagerImpl extends AbstractBusinessObject implements IGameUserManager {

    @Autowired
    IMainStationKeyRedisDAO mainStationKeyRedisDAO;
    /**
     * 日志记录器
     */
    private final Logger logger = LoggerFactory.getLogger(GameUserManagerImpl.class);

    /**
     * 设置连接超时时间
     */
    @Value("${httpclient.connection.timeout}")
    private int connectionTimeout = 30000;

    /**
     * 设置读取超时时间
     */
    @Value("${httpclient.connection.sotimeout}")
    private int SoTimeout = 120000;

    /**
     * 设置最大连接数
     */
    @Value("${httpclient.connection.maxconnection}")
    private int maxConnection = 200;

    /**
     * 设置每个路由最大连接数
     */
    @Value("${httpclient.connection.maxrouteconnection}")
    private int maxRouteConneciton = 50;

    /**
     * 设置接收缓冲
     */
    @Value("${httpclient.connection.socketbuffersize}")
    private int sockeBufferSize = 8192;

    /**
     * 设置失败重试次数
     */
    @Value("${httpclient.connection.maxretry}")
    private int maxRetry = 5;

    /**
     * 下载多线程管理器
     */
    private static PoolingClientConnectionManager connMger;

    //生产环境使用
    @Value("${7bao.send.baseServiceUrl}")
    private String baseServiceUrl="";
    /**
     * 获取用户接口名称
     */
    private String openIdMethod = "kubao.passport.openUserInfo.get";

    /**
     * openId接口参数
     */
    private String OpenIdParaInfo = "{\"OpenId\":\"\"}";
    /**
     * 下载参数
     */
    private static HttpParams connParams;

    private JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    private static final CloseableHttpClient httpClient = HttpConnectionUtils.getHttpClient();

    @Value("${7bao.openapi.5173.com.url}")
    private String openApiUrl = "";
    @Value("${7bao.openapi.5173.com.appid}")
    private String openApiAppid = "";
    @Value("${7bao.openapi.5173.com.authVers}")
    private String openApiAuthVers = "";
    @Value("${7bao.openapi.5173.com.appsecret}")
    private String openApiAppsecret = "";

    private static int requestTime = 0;


    public String getMainResponse(String method, Map<String, Object> param, String vers, String token) throws Exception {
        logger.info("请求主站处理开始：方法" + method);
        JSONObject json = JSONObject.fromObject(param);
        String paraInfo = json.toString();
        String disPalyParaInfo = "{\"IsSupportPasspod\":true,\"CallbackUrl\":\""+baseServiceUrl+"/api/7Bao-frontend/getUserInfoServlet\",\"IsLogout\":false}";
        String urlStr = this.getUrl(method, paraInfo, vers, token ,disPalyParaInfo);
        logger.info("OPENAPI生成URL：{}", urlStr);
        return urlStr;
    }

    @Override
    public String loginOutForMain5173() {
        String method = MainStationConstant.METHOD_DISPLAY;
        String vers = MainStationConstant.RESULT_AUTHVERS2;
        Map<String, Object> param = new HashMap<String, Object>();
        String urlStr = null;
        try {

            logger.info("请求主站处理开始：方法" + method);
            JSONObject json = JSONObject.fromObject(param);
            String paraInfo = json.toString();
            String disPalyParaInfoForLoginOut = "{\"IsSupportPasspod\":true,\"CallbackUrl\":\""+baseServiceUrl+"/api/7Bao-frontend/getUserInfoServlet\",\"IsLogout\":true}";
            urlStr = this.getUrl(method, paraInfo, vers, null ,disPalyParaInfoForLoginOut );
            logger.info("OPENAPI生成URL：{}", urlStr);

            if (urlStr == null) {
                logger.info("请求主站loginOutForMain5173失败：{}", urlStr);
            }
//            ObjectMapper mapper = new ObjectMapper();
//            //mapper.getNodeFactory();
//            JsonNode node = mapper.readTree(mainResponse.toString());
//            String html = node.get("BizResult").toString();
//            String passwordKey = node.get("BizResult").get("Validation").get("PasswordKey").toString();
        } catch (Exception e) {
            logger.error("请求主站loginOutForMain5173未知错误：{}", e.toString());
            e.printStackTrace();
        }
        return urlStr;
    }


    /**
     * 1.主站请求接口的获取授权
     *
     * @param
     */
    @Override
    public SRRequsestTokenResponse getRequestAuthorization() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        long timeNow = System.currentTimeMillis();
//        logger.info(LogCategory.SYSTEM,"时间戳为:{}", timeNow);
        String sign = MainStationUtil.ComputeSignature(SpitConstant.getBaseSign(openApiAppid) + "&" + MainStationConstant.RESULT_MD5 + "&" + timeNow
                , openApiAppsecret, "");
        String url = MainStationConstant.URL_REQUESTTOKEN + SpitConstant.getRequestStr(openApiAppid) + "&timestamp=" + timeNow + "&sign=" + sign;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader(MainStationConstant.HEADER_CONTENT_ENCODING, MainStationConstant.CONTANT_CODE);
        get.setHeader(MainStationConstant.HEADER_CONTENT_TYPE, MainStationConstant.CONTANT_CONTENT_TYPE);
        try {
            HttpResponse response = httpclient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream is = response.getEntity().getContent();
                String result = MainStationConstant.inStream2String(is);
                SRRequsestTokenResponse token = JsonMapper.nonEmptyMapper().fromJson(result, SRRequsestTokenResponse.class);
                return token;
            }
        } catch (Exception e) {
            logger.info("时间戳为:{}", timeNow);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 2.主站请求接口的获取令牌
     */
    public SRRequsestTokenResponse getRequestToken() {
//        logger.info(LogCategory.SYSTEM,"获取令牌开始");
        SRRequsestTokenResponse srrt = getRequestAuthorization();//调用获取授权方法
        String requesttoken = srrt.getBizResult().getRequestToken();
        String rquestSecret = srrt.getBizResult().getRequestSecret();

//        logger.info(LogCategory.SYSTEM,"requesttoken:"+requesttoken,"rquestSecret:"+rquestSecret);
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        long timeNow = System.currentTimeMillis();
        try {

            String sign = MainStationUtil.ComputeSignature(SpitConstant.getBaseSign(openApiAppid) + "&" + requesttoken
                    + "&" + MainStationConstant.RESULT_MD5 + "&" + timeNow, openApiAppsecret, rquestSecret);

            String urlStr = MainStationConstant.URL_ACCESS + SpitConstant.getRequestStr(openApiAppid) + "&timestamp=" + URLEncoder.encode(String.valueOf(timeNow)) + "&requesttoken="
                    + URLEncoder.encode(String.valueOf(requesttoken)) + "&sign=" + URLEncoder.encode(sign);

            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
//            logger.info(LogCategory.SYSTEM,"URL结束");

            HttpClient httpclient = new DefaultHttpClient();

//            logger.info(LogCategory.SYSTEM,"HttpClient");
            HttpGet get = new HttpGet(uri);
            get.setHeader(MainStationConstant.HEADER_CONTENT_ENCODING, MainStationConstant.CONTANT_CODE);
            get.setHeader(MainStationConstant.HEADER_CONTENT_TYPE, MainStationConstant.CONTANT_CONTENT_TYPE);
            HttpResponse response = httpclient.execute(get);
//            logger.info(LogCategory.SYSTEM,"HttpResponse结束");
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream is = response.getEntity().getContent();
                String result = MainStationConstant.inStream2String(is);
//                logger.info(LogCategory.SYSTEM,"URL结束"+result);
                //SRRequsestTokenResponse token= MainStationConstant.JSONToObj(result,SRRequsestTokenResponse.class);
                SRRequsestTokenResponse token = JsonMapper.nonEmptyMapper().fromJson(result, SRRequsestTokenResponse.class);
//                logger.info(LogCategory.SYSTEM,"获取令牌结束: "+ ToStringBuilder.reflectionToString(token));
                return token;
            }
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException异常{}", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception异常{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private String getUrl(String method, String paraInfo, String vers, String token , String disPaly) {
        String url = MainStationConstant.URL_REST;
        String authVers = MainStationConstant.RESULT_AUTHVERS;//默认1.0
//        if(method.equals(MainStationConstant.METHOD_VALIDATECOOKIE)){
//            authVers=MainStationConstant.RESULT_AUTHVERS2;//默认2.0
//        }

        MainStationKeyEO mainStationKeyEO = new MainStationKeyEO();
        SRRequsestTokenResponse srrt = null;
        String accesstoken = null;
        String accessSecret = null;
        //从redis中取主站密钥
        //1.key不存在的话 去调用主站方法获得key，再存入redis   2.存在直接拿来用
        MainStationKeyEO key = getTokenForRedis();
        accesstoken = key.getAccessToken();
        accessSecret = key.getAccessSecret();

        if (StringUtils.isBlank(accesstoken) || StringUtils.isBlank(accessSecret)) {
//            logger.info(LogCategory.SYSTEM,"授权失败{}",srrt);
            return "";
        }

        String clientIP = IpContext.getCurrentContext().getIp();
        if (StringUtils.isBlank(clientIP)) {
            clientIP = MainStationUtil.getLocalIpAddress();
        }
        long timeNow = System.currentTimeMillis();
        String fields = "";
        //设置参数类型为json
        String format = MainStationConstant.RESULT_TYPE;
        //设置加密类型为md5
        String signMethod = MainStationConstant.RESULT_MD5;
        if (token == null) {
            token = "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(accesstoken).append("&").append(openApiAppid).append("&").append(authVers).append("&").append(clientIP);
        builder.append("&").append(fields).append("&").append(format).append("&").append(method).append("&").append(disPaly);
        builder.append("&").append(signMethod).append("&").append(timeNow).append("&").append("").append("&GET:").append(url).append("&").append(vers);
        String baseSign = builder.toString();

        String sign = MainStationUtil.ComputeSignature(baseSign, openApiAppsecret, accessSecret);
        Map<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("accesstoken", accesstoken);
        queryMap.put("appid", openApiAppid);
        queryMap.put("authVers", authVers);
        queryMap.put("clientIP", clientIP);
        queryMap.put("fields", fields);
        queryMap.put("format", format);
        queryMap.put("method", method);
        queryMap.put("paraInfo", disPaly);
        queryMap.put("signmethod", signMethod);
        queryMap.put("timestamp", String.valueOf(timeNow));
        queryMap.put("token", token);
        queryMap.put("vers", vers);
        queryMap.put("sign", sign);
        //url拼接
        url = MainStationUtil.keyValueJoin(url + "?", queryMap, "&");
        return url;
    }

    public String getRequestPasswordKey() {
        String method = MainStationConstant.METHOD_DISPLAY;
        String vers = MainStationConstant.RESULT_AUTHVERS2;
        Map<String, Object> param = new HashMap<String, Object>();
        String mainResponse = null;
        try {
            mainResponse = getMainResponse(method, param, vers, null);
            if (mainResponse == null) {
                logger.info("请求主站getRequestPasswordKey失败：{}", mainResponse);
            }
//            ObjectMapper mapper = new ObjectMapper();
//            //mapper.getNodeFactory();
//            JsonNode node = mapper.readTree(mainResponse.toString());
//            String html = node.get("BizResult").toString();
//            String passwordKey = node.get("BizResult").get("Validation").get("PasswordKey").toString();
        } catch (Exception e) {
            logger.error("请求主站getRequestPasswordKey未知错误：{}", e.toString());
            e.printStackTrace();
        }
        return mainResponse;
    }

    /**
     * 请求openUserInfo.get接口 通过openid获取用户信息
     * <p>
     * 回调用来更加openid获取到该用户的用户名和Uid
     *
     * @param openId
     * @return
     */
    @Override
    public Map<String, String> getUser(String openId,String ip) {
        String timeStamp = String.valueOf(new Date().getTime());
//        InetAddress ia = null;
        String url = null;
        String responseStr = null;
        Map<String, String> resMap = new HashMap<String, String>();
        try {
            String localIp = ip;
            MainStationKeyEO key = getTokenForRedis();
            String accessToken = key.getAccessToken();
            String secret = key.getAccessSecret();

            JSONObject object = JSONObject.fromObject(OpenIdParaInfo);
            if (openId == null) {
                throw new BusinessException("用户未登录");
            }
            object.put("OpenId", openId);
            String fields = "";
            String authVers = MainStationConstant.RESULT_AUTHVERS;
            String format = MainStationConstant.RESULT_TYPE;
            String signMethod = MainStationConstant.RESULT_MD5;
            String openIdParaInfoJson = jsonMapper.toJson(object);

            String baseSign = MessageFormat.format("{0}&{1}&{2}&{3}&{4}&{5}&{6}&{7}&{8}&{9}&{10}&{11}&{12}",
                    accessToken, openApiAppid, authVers, localIp, fields, format, openIdMethod, openIdParaInfoJson, signMethod, timeStamp,
                    "", "GET:" + MainStationConstant.URL_REST, "1.0");

            String sign = MainStationUtil.ComputeSignature(baseSign, openApiAppsecret, secret);
            String urlStr = URLEncoder.encode(openIdParaInfoJson, "gb2312");

            Map<String, String> queryMap = new HashMap<String, String>();

            queryMap.put("accesstoken", accessToken);
            queryMap.put("appid", openApiAppid);
            queryMap.put("authVers", authVers);
            queryMap.put("clientIP", localIp);
            queryMap.put("fields", fields);
            queryMap.put("format", format);
            queryMap.put("method", openIdMethod);
            queryMap.put("paraInfo", urlStr);
            queryMap.put("signMethod", signMethod);
            queryMap.put("timestamp", timeStamp);
            queryMap.put("vers", "1.0");
            queryMap.put("token", "");
            queryMap.put("sign", sign);
            url = MainStationUtil.keyValueJoin(MainStationConstant.URL_REST + "?", queryMap, "&");

            URL urlreq = new URL(url);
            URI uri = new URI(urlreq.getProtocol(), urlreq.getHost(), urlreq.getPath(), urlreq.getQuery(), null);
            HttpGet get = new HttpGet(uri);
            //设置超时时间1min,请求时间3秒连接超时时间6s
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(6000).setConnectionRequestTimeout(60000)
                    .setSocketTimeout(5000).build();
            get.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(get);
            if (response == null) return null;
            InputStream is = response.getEntity().getContent();
            String resultData = MainStationConstant.inStream2String(is);

            logger.info("返回信息：{}",resultData);
            JSONObject bizResultObject = JSONObject.fromObject(resultData);
            String userID = bizResultObject.getString("UserID");
            String userName = bizResultObject.getString("UserName");
            resMap.put("userID", userID);
            resMap.put("userName", userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resMap;
    }

    public MainStationKeyEO getTokenForRedis() {
        //从redis中取主站密钥
        //1.key不存在的话 去调用主站方法获得key，再存入redis   2.存在直接拿来用
//        MainStationKeyEO key = null;
        MainStationKeyEO key = mainStationKeyRedisDAO.getKey();
        if (key == null) {
            key = new MainStationKeyEO();
            SRRequsestTokenResponse srrt = getRequestToken();//调用获取授权方法
            key.setAccessSecret(srrt.getBizResult().getAccessSecret());
            key.setAccessToken(srrt.getBizResult().getAccessToken());
            mainStationKeyRedisDAO.saveKey(key);
            return key;
        }
        return key;
    }

}
