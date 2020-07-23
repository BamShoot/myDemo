package com.bamboo.demo.test;


import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@Data
public class Test {

    private String id;

    private String name;


    private String age;

    private Date date;


    public static void main(String[] args) {

    }

    public static void main2(String[] args) throws Exception {
        String url="https://dav.jianguoyun.com/dav/YueDu/";
        String userName="xxd0422@qq.com";
        String password="ahnrq7xeutggtttk";
        //  String password="xxd552809";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("file", FileUtil.file("D:\\face.jpg"));

        Sardine sardine = SardineFactory.begin(userName, password);
        System.out.println(sardine.list(url));


        url="https://dav.jianguoyun.com/dav/YueDu/";
        HttpRequest httpRequest=new HttpRequest(url);


        HttpResponse execute = HttpRequest.get(url).header("Content-Type", "text/xml; charset=utf-8").header("Depth","1").basicAuth(userName, password).execute();
        System.out.println(execute.getStatus());

        url="https://dav.jianguoyun.com/dav/YueDu/test/";
        execute = HttpRequest.post(url).basicAuth(userName, password).execute();
        System.out.println(execute.getStatus());

    }



    public static void main1(String[] args) throws IOException {

        String s = "ab";
        String s1 = "a";
        String s2 = s1 + "b";
        String s3 = "ab";
        System.out.println(s == s2);//false
        System.out.println(s2 == s3);//false
        System.out.println(s2.hashCode() == s3.hashCode());



    }



    /**
     * 读取文件数据加入到map缓存中
     * @throws IOException
     */
    public static void readJsonData() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/a.json");
        File file = resource.getFile();
        String jsonString = FileUtils.readFileToString(file);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        JSONArray resultObject = jsonObject.getJSONArray("resultObject");
        StringBuffer sBuffer = new StringBuffer();
        resultObject.stream().forEach(jsonobject->arrayIdToString((JSONObject) jsonobject,sBuffer));
        System.out.println(sBuffer.toString());


    }
    private static StringBuffer arrayIdToString(JSONObject jsonobejct,
                                                StringBuffer sBuffer) {
        return sBuffer.append(jsonobejct.getString("offerId")).append(",");
    }


}
