package com.bamboo.demo.test;

import cn.hutool.core.io.FileUtil;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.List;


public class JsonObject {

    private static String getxmlString() {
        String url = JsonObject.class.getResource("/com/test/xml/C73646A1.xml")
                .getFile();
        String string = FileUtil.readUtf8String(url);
        return string;
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
