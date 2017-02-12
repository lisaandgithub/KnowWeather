package com.example.administrator.knowweather.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Administrator on 2017/2/12 0012.
 */
public class parseXMLWithPull {
    public static String  parseBingPicForUrl(String xmlData) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = factory.newPullParser();//得到xmlPullParser对象
        xmlPullParser.setInput(new StringReader(xmlData));//将服务器返回的XML数据设置进去就可以开始解析了
        //下面开始解析,首先获取当前的解析事件
        int eventType = xmlPullParser.getEventType();
        String url = "";
        //然后在while循环里进行解析
        while (eventType != XmlPullParser.END_DOCUMENT){
            String nodeName = xmlPullParser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:{
                    if ("url".equals(nodeName)) {
                        try {
                            url = xmlPullParser.nextText();//获取节点的具体内容
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                case XmlPullParser.END_TAG:{
                    if ("url".equals(nodeName)) {

                    }
                    break;
                }
            }
            eventType = xmlPullParser.next();
        }

    return url;
    }
}
