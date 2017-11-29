package com.wzitech.gamegold.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 签名工具类
 */
public class SignHelper {
    private static final Logger logger = LoggerFactory.getLogger(SignHelper.class);

    public static String sign(Map<String, String> map, String key, String enc) {
        Set<String> sortSet = toSortSet(map, "=", null);
        String result = joinCollectionToString(sortSet, "&");
        result = key + result;
        logger.info("签名字符串：{}", result);
        String md5 = null;
        try {
            md5 = EncryptHelper.md5(result, enc);
        } catch (Exception e) {
            logger.error("MD5签名出错了", e);
        }
        return md5;
    }

    public static String sign2(Map<String, String> map, String key, String enc) {
        StringBuilder sb=new StringBuilder();
        for(String keyData :map.keySet()){
            String value = "";
            Object valueObject = map.get(keyData);
            if (valueObject == null) {
                value = "";
            } else if (valueObject instanceof String[]) {
                String[] values = (String[]) valueObject;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObject.toString();
            }
//
//            if (StringUtils.isNotBlank(enc)) {
//                value = urlEncoding(value, enc);
//            }
            sb.append("_"+keyData + "=" + value);
        }
        String result = key + sb.toString();
        logger.info("签名字符串：{}", result);
        String md5 = null;
        try {
            md5 = EncryptHelper.md5(result, enc);
        } catch (Exception e) {
            logger.error("MD5签名出错了", e);
        }
        return md5;
    }

    public static String formatURL(String url, Map<String, String> map, String secretKey, String enc) {
        String sign = sign(map, secretKey, enc);
        String paramsURL = joinCollectionToString(toLinkedSet(map, "=", enc), "&");
        return url + "?" + paramsURL + "&sign=" + sign;
    }

    public static String joinCollectionToString(Collection<String> collection, String joinStr) {
        if (CollectionUtils.isEmpty(collection)) return null;
        StringBuffer s = new StringBuffer();
        Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            s.append(iterator.next());
            if (iterator.hasNext())
                s.append(joinStr);
        }
        return s.toString();
    }

    public static Set<String> toSortSet(Map<String, String> map, String joinStr, String enc) {
        if (map.isEmpty()) return null;

        Set<String> set = new TreeSet<String>();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = "";
            Object valueObject = map.get(key);
            if (valueObject == null) {
                value = "";
            } else if (valueObject instanceof String[]) {
                String[] values = (String[]) valueObject;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObject.toString();
            }

            if (StringUtils.isNotBlank(enc)) {
                value = urlEncoding(value, enc);
            }
            set.add(key + joinStr + value);
        }
        return set;
    }

    public static Set<String> toLinkedSet (Map<String, String> map, String joinStr, String enc) {
        if (map.isEmpty()) return null;
        Set<String> paramSet = new LinkedHashSet<String>();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entity = iterator.next();
            if (StringUtils.isNotBlank(enc)) {
                paramSet.add(entity.getKey() + joinStr + urlEncoding(entity.getValue(), enc));
            } else {
                paramSet.add(entity.getKey() + joinStr + entity.getValue());
            }
        }
        return paramSet;
    }

    public static String urlEncoding(String content, String enc) {
        if (StringUtils.isBlank(content)) return "";
        try {
            return URLEncoder.encode(content, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }


    /** <一句话功能简述>
     * <功能详细描述>验证返回参数
     * @param params
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean checkParam(Map<String,String> params,String key){
        boolean result = false;
        if(params.containsKey("sign")){
            String sign = params.get("sign");
            params.remove("sign");
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            SignHelper.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String signRecieve = MD5.sign(preStr, "&key=" + key, "utf-8");
            result = sign.equalsIgnoreCase(signRecieve);
        }
        return result;
    }

    /**
     * 过滤参数
     * @author
     * @param sArray
     * @return
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>(sArray.size());
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /** <一句话功能简述>
     * <功能详细描述>将map转成String
     * @param payParams
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String payParamsToString(Map<String, String> payParams){
        return payParamsToString(payParams,false);
    }

    public static String payParamsToString(Map<String, String> payParams,boolean encoding){
        return payParamsToString(new StringBuilder(),payParams,encoding);
    }
    /**
     * @author
     * @param payParams
     * @return
     */
    public static String payParamsToString(StringBuilder sb,Map<String, String> payParams,boolean encoding){
        buildPayParams(sb,payParams,encoding);
        return sb.toString();
    }

    /**
     * @author
     * @param payParams
     * @return
     */
    public static void buildPayParams(StringBuilder sb,Map<String, String> payParams,boolean encoding){
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for(String key : keys){
            sb.append(key).append("=");
            if(encoding){
                sb.append(urlEncode(payParams.get(key)));
            }else{
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }

    public static String urlEncode(String str){
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        }
    }


    public static Element readerXml(String body, String encode) throws DocumentException {
        SAXReader reader = new SAXReader(false);
        InputSource source = new InputSource(new StringReader(body));
        source.setEncoding(encode);
        Document doc = reader.read(source);
        Element element = doc.getRootElement();
        return element;
    }

}
