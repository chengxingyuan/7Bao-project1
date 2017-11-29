package com.wzitech.Z7Bao.util;


import org.jdom.JDOMException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * 解析XML应答类
 * @author miklchen
 *
 */
public class XMLClientResponseHandler extends ClientResponseHandler {

    protected void doParse() throws JDOMException, IOException {
        String xmlContent = this.getContent();

        //解析xml,得到map
        Map m = XMLUtil.doXMLParse(xmlContent);

        //设置参数
        Iterator it = m.keySet().iterator();
        while(it.hasNext()) {
            String k = (String) it.next();
            String v = (String) m.get(k);
            this.setParameter(k, v);
        }

    }

}
