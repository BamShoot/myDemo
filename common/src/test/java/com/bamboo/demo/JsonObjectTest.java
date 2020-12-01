package com.bamboo.demo;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bamboo.demo.entity.FormXml;
import com.bamboo.demo.entity.User;
import com.bamboo.demo.test.JsonObject;
import com.bamboo.demo.test.XmlUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * JsonObjectTest
 *
 * @author xu.xudong
 * @date 2020-11-27 11:49
 */
@RunWith(SpringRunner.class)
class JsonObjectTest {
    private static String getxmlString() {
        String url = JsonObject.class.getResource("/com/test/xml/C73646A1.xml")
                .getFile();
        String string = FileUtil.readUtf8String(url);
        return string;
    }

    @Test
    public void xml4() throws Exception {
        Document doc = DocumentHelper.parseText(getxmlString());
        HashMap<String, String> map = new HashMap<String, String>();
        Element root = doc.getRootElement();
        Element flag = (Element) doc.selectSingleNode("//flag[text()=0]");
        if (flag != null && !"".equals(flag)) {
            map.put("flag", "0");
            map.put("message", root.elementText("errorMsg"));
        } else {
            map.put("flag", "1");
            Element e = root.element("apply");
            String ywlx = e.attributeValue("ywlx");
            if ("110".equals(ywlx)) {
                map.put("ywlx", "护照");
            }
            if ("120".equals(ywlx)) {
                map.put("ywlx", "港澳双程");
            }
            if ("140".equals(ywlx)) {
                map.put("ywlx", "大陆证");
            }
            map.put("zwxm", e.attributeValue("zwxm"));
            map.put("spjg", e.attributeValue("spjg"));
            map.put("spbz", e.attributeValue("spbz"));
        }
        System.out.println(JSONObject.toJSONString(map));
    }

    @Test
    public void xml3() throws Exception {
        Document doc = DocumentHelper.parseText(getxmlString());
        Element ele = doc.getRootElement();
        Element data = ele.element("data");
        Iterator<Element> datas = data.elementIterator();
        while (datas.hasNext()) {
            Element e = datas.next();
            System.out.println(e.getName() + "===" + e.getText());
        }
    }

    @Test
    public void xml2() throws Exception {
        Document doc = DocumentHelper.parseText(getxmlString());
        String aa = "本地护照办证类别";

        Element ele = (Element) doc.selectSingleNode("//name[text()=\"" + aa
                + "\"]");
        ele = ele.getParent();
        ele = ele.element("options");
        // 获取根节点
        Iterator<Element> iterator = ele.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();

            System.out.println(e.asXML().substring(13, 15));
            System.out.println("ssss==" + e.attributeCount());
            System.out.println("aaaa==" + e.getText());

        }
    }

    @Test
    public void xml1() throws Exception {
        Document doc = DocumentHelper.parseText(getxmlString());
        String aa = "异地港澳双程签注种类";
        Element ele = (Element) doc.selectSingleNode("//codeGroup[@dmlb=\""
                + aa + "\"]");
        // 获取根节点
        Iterator<Element> iterator = ele.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            System.out.println(e.attributeValue("value") + ":"
                    + e.attributeValue("dmmc"));

        }
    }

    @Test
    public void xml() throws Exception {
        // System.out.println(getxmlString());
        // xml字符串读取为Document
        HashMap<String, Object> map = new HashMap<>();
        Document document = DocumentHelper.parseText(getxmlString());
        // 获取根节点
        Element ele = document.getRootElement();
        ele = ele.element("head");
        map.put("code", ele.elementText("code"));
        map.put("message", ele.elementText("message"));
        map.put("rownum", ele.elementText("rownum"));
        Element node = ele.element("body");
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        Iterator<Element> iterator = node.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            // 首先获取当前节点的所有节点
            List<Element> elements = e.elements();
            HashMap<String, String> map1 = new HashMap<String, String>();
            for (Element element : elements) {
                map1.put(element.getName(), element.getText());
            }
            list.add(map1);
        }
        map.put("body", list);
        System.out.println(JSONObject.toJSONString(map));
    }





    /**
     * xml格式的字符串与json格式的字符串互转
     */
    @Test
    public void xmlToJSONAndJSONToXml() throws Exception{
        String url = JsonObject.class.getResource("/static/xml/16.xml")
                .getFile();
        String xmlStr = FileUtil.readUtf8String(url);


        String jsonStr = XmlUtils.xml2json(xmlStr);//xml转json
        System.out.println(jsonStr);//打印

        //json转xml输出
        xmlStr = XmlUtils.json2xml(jsonStr);//json转xml
        //xml格式化
        Document doc = DocumentHelper.parseText(xmlStr);
        System.out.println(XmlUtils.formatXml(doc));
    }


    /**
     * javabean转换为json字符串
     */
    @Test
    public void javaBeanToJSON() {
        User user = new User();
        user.setUsername("张三");
        user.setPassword("111");
        String s = JSONObject.toJSONString(user);
        System.out.println("javabean--->json \n" + s);
    }

    @Test
    public void javaBeanTojson() {
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < 4; i++) {
            User user = new User();
            user.setUsername("张三" + i);
            user.setPassword("mm" + i);
            list.add(user);
        }
        String s = JSONObject.toJSONString(list);
        System.out.println("javabean--->json \n" + s);
    }

    /**
     * json字符串转换为javabean
     */
    @Test
    public void JsonToJAVA() {
        String jsonStr = "[{\"password\":\"11\",\"username\":\"张三\"},{\"username\":\"李四\",\"password\":\"22\"}]";
        JSONArray json = JSONArray.parseArray(jsonStr);
        User user;
        for (int i = 0; i < json.size(); i++) {
            //JSONObject object = json.getJSONObject(i);
            String object = json.getString(i);
            user = (User) JSONObject.parseObject(object, User.class);
            System.out.println(user);
        }

    }



    /**
     * xml转为javabean
     */
    @Test
    public void xmltest() throws Exception {
        String string = getxmlString();
        // xml字符串读取为Document
        Document document = DocumentHelper.parseText(string);
        // 获取根节点
        Element root = document.getRootElement();
        Iterator<Element> eles = root.elementIterator();
        FormXml formXml = new FormXml();
        HashMap<String, String> map = new HashMap<>();
        while (eles.hasNext()) {
            Element ele = eles.next();
            System.out.println(ele.getName() + "：" + ele.getText());
            if ("表单名称".equals(ele.getName())) {
                formXml.setName(ele.getText());
            } else {
                map.put(ele.getName(), ele.getText());
            }
//			Info info = new Info();
//			Element ele = eles.next();
//			String name = ele.getName();
//			System.out.println(name);
//			info.setId(ele.elementText("id"));
//			info.setState(ele.elementText("state"));
//			info.setAdddate(ele.elementText("adddate"));
//			info.setTitle(ele.elementText("title"));
//			System.out.println(info.toString());
        }

        formXml.setContent(map);
        System.out.println(formXml);
    }

    /**
     * 生成xml文件，新增属性
     */
    @Test
    public void getxml() throws Exception {
        File file = new File("F:/1.xml");
        if (!file.exists()) {
            // 创建document
            Document document = DocumentHelper.createDocument();
            // 创建根元素
            document.addElement("root");
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter write = new XMLWriter(new FileWriter(file), format);
            write.write(document);
            write.close();
        }

        Document doc = new SAXReader().read(file);
        Element root = doc.getRootElement();
        root.addElement("auther")
                .addAttribute("name", "Jamessss")
                .addAttribute("location", "UKAAAA");
        // 写一个没有格式的XML文件
        // XMLWriter write = new XMLWriter(new FileWriter("/src/output.xml"));

        // 创建文件输出的时候，自动缩进的格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter write = new XMLWriter(new FileWriter(file), format);
        write.write(doc);
        write.close();
    }

    /**
     * 遍历当前节点下的所有节点
     *
     * @param node
     */
    public void listNodes(Element node) {
        System.out.println("当前节点的名称：" + node.getName());
        // 首先获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        // 遍历属性节点
        for (Attribute attribute : list) {
            System.out.println("属性" + attribute.getName() + ":"
                    + attribute.getValue());
        }
        // 如果当前节点内容不为空，则输出
        if (!(node.getTextTrim().equals(""))) {
            System.out.println(node.getName() + "：" + node.getText());
        }
        // 同时迭代当前节点下面的所有子节点
        // 使用递归
        Iterator<Element> iterator = node.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            System.out.println("使用递归=======");
            listNodes(e);
        }
    }
}