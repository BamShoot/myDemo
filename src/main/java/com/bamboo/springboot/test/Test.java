package com.bamboo.springboot.test;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Data
public class Test {

    private String id;

    private String name;


    private String age;

    private Date date;



    public static void main(String[] args) throws IOException {

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
