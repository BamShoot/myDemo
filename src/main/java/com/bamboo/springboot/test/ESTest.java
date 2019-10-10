package com.bamboo.springboot.test;


import com.bamboo.springboot.common.ESHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ESTest {


    @RequestMapping("/existsInndex")
    public void existsInndex() throws IOException {
        boolean exists = ESHandler.existsInndex("test");
        System.out.println(exists);
    }

    @RequestMapping("/createIndex")
    public void createIndex() throws IOException {
        boolean exists = ESHandler.createIndex("test");
        System.out.println(exists);
    }

    @RequestMapping("/deleteIndex")
    public void deleteIndex() throws IOException {
        boolean exists = ESHandler.deleteIndex("test");
        System.out.println(exists);
    }


    @RequestMapping("/insertData")
    public void insertData() throws IOException {
        Test test = new Test();
        test.setId("20");
        test.setName("tom");
        test.setAge("22");
        HashMap map = new HashMap();
        map.put("id", 55);
        map.put("name", "small");
        map.put("date", new Date());
        ESHandler.insertDocument("test", "test", "4", test);
    }

    @RequestMapping("/getData")
    public void getData() throws IOException {
        Test test = ESHandler.getDocument("test", "test", "1", Test.class);
        System.out.println(test.toString());
    }

    @RequestMapping("/existDocument")
    public void existDocument() throws IOException {
        boolean b = ESHandler.existDocument("test", "test", "1");
        System.out.println(b);
    }

    @RequestMapping("/deleteDocument")
    public void deleteDocument() throws IOException {
        ESHandler.deleteDocument("test", "test", "1");

    }

    @RequestMapping("/updateDocument")
    public void updateDocument() throws IOException {
        Test test = new Test();
        test.setId("25");
        test.setName("tom");
        test.setAge("22");
        test.setDate(new Date());
        HashMap map=new HashMap();
        map.put("id",55);
        map.put("name","small");
        map.put("date",new Date());
        ESHandler.updateDocument("test", "test", "3", test);
    }

    @RequestMapping("/searchDocuments")
    public ArrayList searchDocuments() throws IOException {
        HashMap must = new HashMap();
//        must.put("name","tom");
//        must.put("id","25");
        HashMap mustNot = new HashMap();
//        mustNot.put("age","18");
        return ESHandler.searchDocuments("test", "test", must, mustNot, 0, 100);


    }


    public static void main(String[] args) {


    }

}
