package com.bamboo.demo;

import com.bamboo.demo.es.ESTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() throws IOException {
        ESTest es=new ESTest();
        es.existsInndex();
    }

}
