package com.bamboo.demo.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultText;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XmlUtils {

    /**
     * xml转json
     *
     * @param xmlStr
     * @return
     */
    public static String xml2json(String xmlStr) {
        try {
            JSONObject json = new JSONObject(true);
            Document doc = DocumentHelper.parseText(xmlStr);
            Element root = doc.getRootElement();
            xml2jsonStr(root, json);

            return JSON.toJSONString(json, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat);//格式化输出
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * json转xml
     *
     * @param jsonStr
     * @return
     */
    public static String json2xml(String jsonStr) {
        try {
            StringBuffer buffer = new StringBuffer();
//            buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
//            JSONObject json = JSON.parseObject(jsonStr);
            JSONObject json = JSONObject.parseObject(jsonStr, Feature.OrderedField);//带顺序
            jsonToXmlstr(json, buffer);
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /***
     * 格式化xml为string
     *
     * @param document
     * @return
     * @throws IOException
     */
    public static String formatXml(Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(document.getXMLEncoding());
        StringWriter stringWriter = new StringWriter();
        XMLWriter writer = new XMLWriter(stringWriter, format);
        writer.write(document);
        writer.close();
        return stringWriter.toString();
    }


    /**
     * 转换该element元素到json
     *
     * @param element
     * @param json
     */
    private static void xml2jsonStr(Element element, JSONObject json) {
        if (element.getName().equals("observation")) {
//            System.out.println(element.getPath());
        }
        if (isNullObject(element)) {
            json.put(element.getName(), "");
            return;
        }

        if (json.get(element.getName()) == null || json.get(element.getName()) instanceof JSONObject) {
            json.put(element.getName(), new JSONObject(true));
        }

        JSONObject detail = json.getJSONObject(element.getName());

        //属性，命名空间处理
        if (element.attributes() != null && element.attributes().size() > 0) {
            Namespace namespace = null;
            String prefix = null;
            for (Object o : element.declaredNamespaces()) {
                namespace = (Namespace) o;
                prefix = namespace.getPrefix();
                detail.put(prefix.equals("") ? "-xmlns" : "-xmlns:" + prefix, namespace.getStringValue());
            }
            Attribute attr = null;
            for (Object ao : element.attributes()) {
                attr = (Attribute) ao;
                detail.put("-" + attr.getQualifiedName(), attr.getValue());
            }
        }
        //元素内容处理
        if (element.content() != null && element.content().size() > 0) {
            //该元素下只有一个content且类型为text
            if (element.content().size() == 1 && element.content().get(0) instanceof DefaultText) {
                DefaultText text = (DefaultText) element.content().get(0);
                if (detail.size() == 0) {//元素无属性情况下，直接放入text
                    json.put(element.getName(), text.getText());
                } else {
                    detail.put("#text", text.getText());//元素有属性情况下，key=text
                }
                return;
            }

            Element el = null;
            JSONObject eljson = null;
            Map<String, Integer> keys = new HashMap<>();
            //多个元素情况下遍历，元素名不同，放入
            for (Object o : element.content()) {
                if (o instanceof DefaultElement) {
                    eljson = new JSONObject(true);
                    el = (Element) o;
                    if (!keys.containsKey(el.getName())) {
                        keys.put(el.getName(), 1);
                        xml2jsonStr(el, eljson);//根据元素构造json数据
                        detail.put(el.getName(), eljson.get(el.getName()));
                    } else {
                        //重新放置jsonarray
                        if (detail.get(el.getName()) instanceof JSONObject) {
                            //变更jsonObject为数组结构存储
                            xml2jsonStr(el, eljson);//根据元素构造json数据
                            JSONArray array = new JSONArray();
                            array.add(detail.getJSONObject(el.getName()));
                            array.add(eljson.getJSONObject(el.getName()));
                            detail.put(el.getName(), array);
                        } else if (detail.get(el.getName()) instanceof JSONArray) {
                            xml2jsonStr(el, eljson);//根据元素构造json数据
                            detail.getJSONArray(el.getName()).add(eljson.getJSONObject(el.getName()));//追加
                        }
                    }
                } else {
//                    System.out.println(o.toString());
                }
            }
        }
    }

    private static boolean isNullObject(Element element) {
        if (element.getText() != null && !element.getText().equals("")) {
            return false;
        }
        if (element.content() != null && element.content().size() > 0) {
            return false;
        }
        if (element.attributes() != null && element.attributes().size() > 0) {
            return false;
        }
        return true;
    }


    /**
     * json文本转xml方法
     *
     * @param json
     * @param buffer
     */
    private static void jsonToXmlstr(JSONObject json, StringBuffer buffer) {
        Iterator<Map.Entry<String, Object>> it = json.entrySet().iterator();
        Map.Entry<String, Object> en = null;
        while (it.hasNext()) {
            en = it.next();
//            System.out.println(en.getKey());
            if (en.getKey().startsWith("-")) {
                continue;
            }
            if (en.getKey().equals("#text")) {
                buffer.append(en.getValue());//直接输出文本
                continue;
            }
            if (en.getValue() instanceof JSONObject) {
                buffer.append("<" + en.getKey() + getAttr((JSONObject) en.getValue()) + ">");
                JSONObject jo = json.getJSONObject(en.getKey());
                jsonToXmlstr(jo, buffer);
                buffer.append("</" + en.getKey() + ">");
            } else if (en.getValue() instanceof JSONArray) {
                JSONArray jarray = json.getJSONArray(en.getKey());
                for (int i = 0; i < jarray.size(); i++) {
                    JSONObject jsonobject = jarray.getJSONObject(i);
                    buffer.append("<" + en.getKey() + getAttr(jsonobject) + ">");
                    jsonToXmlstr(jsonobject, buffer);
                    buffer.append("</" + en.getKey() + ">");
                }
            } else {
                buffer.append("<" + en.getKey() + ">" + en.getValue()).append("</" + en.getKey() + ">");
            }
        }
    }

    /**
     * 拼当前节点属性
     */
    private static String getAttr(JSONObject json) {
        StringBuffer sb = new StringBuffer();
        JSONObject obj = null;
        for (Map.Entry<String, Object> entity : json.entrySet()) {
            if (entity.getKey().startsWith("-")) {
                sb.append(" ").append(entity.getKey().substring(1)).append("=\"").append(entity.getValue().toString()).append("\"");
            }

        }
        return sb.toString();
    }


}
