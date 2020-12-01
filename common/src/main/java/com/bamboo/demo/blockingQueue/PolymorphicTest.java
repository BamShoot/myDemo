package com.bamboo.demo.blockingQueue;


import java.io.FileOutputStream;
import java.io.IOException;


public class PolymorphicTest {
    public static void main(String[] args) throws IOException {
        // 使用文件名称创建流对象
        String file = PolymorphicTest.class.getResource("/static/userinfo.txt").getFile();
        System.out.println(file);
        FileOutputStream fos =  new FileOutputStream(file);
        // 定义字节数组
        byte[] words = {97, 98, 99, 100, 101};
        // 遍历数组
        for (int i = 0; i < words.length; i++) {
            // 写出一个字节
            fos.write(words[i]);
            // 写出一个换行, 换行符号转成数组写出
            fos.write("\n".getBytes());
        }
        // 关闭资源，输出在target文件夹中
        fos.close();
    }


}