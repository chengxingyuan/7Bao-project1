package com.wzitech.Z7Bao.util;



import org.jdom.JDOMException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class DirectTransClientRequestHandler extends RequestHandler {

    public DirectTransClientRequestHandler(HttpServletRequest request,
                                           HttpServletResponse response) {
        super(request, response);

        this.setGateUrl("https://api.mch.tenpay.com/cgi-bin/mchbatchtransfer.cgi");

    }

    public void setAllParameterFromXml(String xmlStr) throws JDOMException, IOException {

        //解析xml,得到map
        Map m = XMLUtil.doXMLParse(xmlStr);

        //设置参数
        Iterator it = m.keySet().iterator();
        while(it.hasNext()) {
            String k = (String) it.next();
            String v = (String) m.get(k);
            this.setParameter(k, v);
        }

    }


    public String getRequestURL() throws UnsupportedEncodingException {

        StringBuffer xmlSb = new StringBuffer();
        //代付接口只支持GBK编码
        String enc = "GBK";
        xmlSb.append("<root>");
        Map m = this.getAllParameters();

        Iterator it = m.keySet().iterator();

        while (it.hasNext()) {
            String k = (String) it.next();
            String v = this.getParameter(k);
            xmlSb.append("<" + k + ">");
            xmlSb.append(v);
            xmlSb.append("</" + k + ">");
        }
        xmlSb.append("</root>");

        String xmlContent = xmlSb.toString();

        // base64
        String content = new String(Base64.encode(xmlContent.getBytes(enc)));

        // md5 first
        String md5Res1 = MD5Util.MD5Encode(content, enc);

        // md5 second
        String md5Src2 = md5Res1 + this.getKey();
        String abstractStr = MD5Util.MD5Encode(md5Src2, enc).toLowerCase();

        this.setDebugInfo("xml: => " + xmlContent + "\r\n"
                + "base64(xml): => " + content + "\r\n"
                + "md5(base64(xml)): => " + md5Res1 + "\r\n"
                + "md5(md5(base64(xml))+key): => " + abstractStr);

        String contentURLEncode = URLEncoder.encode(content, enc);

        String requestURL = this.getGateUrl() + "?" + "content="
                + contentURLEncode + "&abstract=" + abstractStr;

        return requestURL;
    }


}
