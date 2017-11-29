package com.wzitech.Z7Bao.util;

import com.wzitech.gamegold.common.utils.HttpsClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Http客户端工具类<br/>
 * 这是内部调用类，请不要在外部调用。
 *
 * @author miklchen
 */
public class HttpClientUtil {

    public static final String SunX509 = "SunX509";
    public static final String IbmX509 = "IbmX509";
    public static final String JKS = "JKS";
    public static final String PKCS12 = "PKCS12";
    public static final String TLS = "TLS";

    /**
     * get HttpURLConnection
     *
     * @param strUrl url地址
     * @return HttpURLConnection
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        return httpURLConnection;
    }

    /**
     * get HttpsURLConnection
     *
     * @param strUrl url地址
     * @return HttpsURLConnection
     * @throws IOException
     */
/*	public static HttpsURLConnection getHttpsURLConnection(String strUrl)
            throws IOException {
		URL url = new URL(strUrl);
		HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
				.openConnection();
		return httpsURLConnection;
	}*/
    public static HttpsURLConnection getHttpsURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(hv);

        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
                .openConnection();

        return httpsURLConnection;
    }

    /**
     * 获取不带查询串的url
     *
     * @param strUrl
     * @return String
     */
    public static String getURL(String strUrl) {

        if (null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf) {
                return strUrl.substring(0, indexOf);
            }

            return strUrl;
        }

        return strUrl;

    }

    /**
     * 获取查询串
     *
     * @param strUrl
     * @return String
     */
    public static String getQueryString(String strUrl) {

        if (null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf) {
                return strUrl.substring(indexOf + 1, strUrl.length());
            }

            return "";
        }

        return strUrl;
    }

    /**
     * 查询字符串转换成Map<br/>
     * name1=key1&name2=key2&...
     *
     * @param queryString
     * @return
     */
    public static Map queryString2Map(String queryString) {
        if (null == queryString || "".equals(queryString)) {
            return null;
        }

        Map m = new HashMap();
        String[] strArray = queryString.split("&");
        for (int index = 0; index < strArray.length; index++) {
            String pair = strArray[index];
            HttpClientUtil.putMapByPair(pair, m);
        }

        return m;

    }

    /**
     * 把键值添加至Map<br/>
     * pair:name=value
     *
     * @param pair name=value
     * @param m
     */
    public static void putMapByPair(String pair, Map m) {

        if (null == pair || "".equals(pair)) {
            return;
        }

        int indexOf = pair.indexOf("=");
        if (-1 != indexOf) {
            String k = pair.substring(0, indexOf);
            String v = pair.substring(indexOf + 1, pair.length());
            if (null != k && !"".equals(k)) {
                m.put(k, v);
            }
        } else {
            m.put(pair, "");
        }
    }

    /**
     * BufferedReader转换成String<br/>
     * 注意:流关闭需要自行处理
     *
     * @param reader
     * @return String
     * @throws IOException
     */
    public static String bufferedReader2String(BufferedReader reader) throws IOException {
        StringBuffer buf = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\r\n");
        }

        return buf.toString();
    }

    /**
     * 处理输出<br/>
     * 注意:流关闭需要自行处理
     *
     * @param out
     * @param data
     * @param len
     * @throws IOException
     */
    public static void doOutput(OutputStream out, byte[] data, int len)
            throws IOException {
        int dataLen = data.length;
        int off = 0;
        while (off < data.length) {
            if (len >= dataLen) {
                out.write(data, off, dataLen);
                off += dataLen;
            } else {
                out.write(data, off, len);
                off += len;
                dataLen -= len;
            }

            // 刷新缓冲区
            out.flush();
        }

    }


    /**
     * 获取SSLContext
     *
     * @param trustPasswd
     * @param keyPasswd
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public static SSLContext getSSLContext(
            FileInputStream trustFileInputStream, String trustPasswd,
            FileInputStream keyFileInputStream, String keyPasswd)
            throws NoSuchAlgorithmException, KeyStoreException,
            CertificateException, IOException, UnrecoverableKeyException,
            KeyManagementException {

        String X509 = HttpClientUtil.getVendorX509();

        // ca
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(X509);
        KeyStore trustKeyStore = KeyStore.getInstance(HttpClientUtil.JKS);
        trustKeyStore.load(trustFileInputStream, HttpClientUtil
                .str2CharArray(trustPasswd));
        tmf.init(trustKeyStore);

        final char[] kp = HttpClientUtil.str2CharArray(keyPasswd);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(X509);
        KeyStore ks = KeyStore.getInstance(HttpClientUtil.PKCS12);
        ks.load(keyFileInputStream, kp);
        kmf.init(ks, kp);

        SecureRandom rand = new SecureRandom();
        SSLContext ctx = SSLContext.getInstance(HttpClientUtil.TLS);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);

        return ctx;
    }

    /**
     * 获取CA证书信息
     *
     * @param cafile CA证书文件
     * @return Certificate
     * @throws CertificateException
     * @throws IOException
     */
    public static Certificate getCertificate(File cafile)
            throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(cafile);
        Certificate cert = cf.generateCertificate(in);
        in.close();
        return cert;
    }

    /**
     * 字符串转换成char数组
     *
     * @param str
     * @return char[]
     */
    public static char[] str2CharArray(String str) {
        if (null == str) return null;

        return str.toCharArray();
    }

    /**
     * 存储ca证书成JKS格式
     *
     * @param cert
     * @param alias
     * @param password
     * @param out
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     */
    public static void storeCACert(Certificate cert, String alias,
                                   String password, OutputStream out) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(null, null);

        ks.setCertificateEntry(alias, cert);

        // store keystore
        ks.store(out, HttpClientUtil.str2CharArray(password));

    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    /**
     * 获取虚拟机厂商
     *
     * @return String
     */
    public static String getVmVendor() {
        return System.getProperty("java.vm.vendor");
    }

    /**
     * 是否此厂商
     *
     * @param vendorAllName   厂商全称
     * @param vendorShortName 厂商简写
     * @return boolean
     */
    public static boolean isVendor(String vendorAllName, String vendorShortName) {
        //转换成小写
        vendorAllName = vendorAllName.toLowerCase();

        return -1 != vendorAllName.indexOf(vendorShortName);
    }

    /**
     * 获取厂商X509
     *
     * @return <br/>sun的jdk返回SunX509,其它的返回IbmX509<br/>
     */
    public static String getVendorX509() {
        String X509 = HttpClientUtil.SunX509;
        if (!HttpClientUtil.isVendor(
                HttpClientUtil.getVmVendor(), "sun") && !HttpClientUtil.isVendor(
                HttpClientUtil.getVmVendor(), "oracle")) {
            X509 = HttpClientUtil.IbmX509;
        }

        return X509;
    }

    /**
     * InputStream转换成Byte
     * 注意:流关闭需要自行处理
     *
     * @param in
     * @return byte
     * @throws Exception
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {

        int BUFFER_SIZE = 4096;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;

        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        byte[] outByte = outStream.toByteArray();
        outStream.close();

        return outByte;
    }

    /**
     * InputStream转换成String
     * 注意:流关闭需要自行处理
     *
     * @param in
     * @param encoding 编码
     * @return String
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in, String encoding) throws IOException {

        return new String(InputStreamTOByte(in), encoding);

    }

    /**
     * 发送虚拟请求
     *
     * @param reqUrl 请求地址
     * @param map    请求参数
     * @return
     */
    public static String sendGetReq(String reqUrl, Map<String, String> map) {
        HttpClient httpClient = HttpsClient.newHttpsClient();
        HttpResponse response = null;
        try {
            StringBuilder queryString = null;
            if (map != null && map.size() > 0) {
                queryString = new StringBuilder("?");
                for (Map.Entry<String, String> entry :
                        map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    queryString.append(key).append("=").append(value);
                    queryString.append("&");
                }
                reqUrl = reqUrl + queryString.substring(0, queryString.length() - 1);
            }

            HttpGet httpGet = new HttpGet(reqUrl);
//            httpGet.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            response = httpClient.execute(httpGet);
            if (response == null || response.getEntity() == null) {
                return null;
            }
            return EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<String, String> transToMAP(Map parameterMap, String encoding) {
        // 返回值Map
        HashMap returnMap = new HashMap();
        Iterator entries = parameterMap.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            if (encoding == null || "".equals(encoding)) {
                returnMap.put(name, value);
            } else {
                try {
                    returnMap.put(name, URLDecoder.decode(value, encoding));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnMap;
    }

    public static Map<String, String> queryStrToMap(String queryString, String encoding) {
        HashMap<String, String> result = new HashMap<String, String>();
        String[] paramsStr = queryString.split("&");
        for (String param : paramsStr) {
            String[] paramPair = param.split("=");
            if (paramPair.length > 1) {
                try {
                    result.put(paramPair[0], URLDecoder.decode(paramPair[1], encoding));
                } catch (UnsupportedEncodingException e) {
                    result.put(paramPair[0], paramPair[1]);
                    e.printStackTrace();
                }
            } else {
                result.put(paramPair[0], "");
            }
        }
        return result;
    }
}