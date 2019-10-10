package com.bamboo.springboot.common;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheGetResult;
import com.alicp.jetcache.CacheResult;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.bamboo.springboot.test.CacheTest;
import com.bamboo.springboot.test.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@RestController
public class CacheUtil {
    @Autowired
    CacheTest cacheTest;

    @CreateCache(name = "a", expire = 10, cacheType = CacheType.REMOTE)
    private Cache<String, Long> cache;

    @RequestMapping("test1")
    public void test1() {
        cache.put("a1", System.currentTimeMillis());
    }

    @RequestMapping("test2")
    public void test2() {
        CacheResult cacheResult = cache.PUT("a1", System.currentTimeMillis(), 30, TimeUnit.SECONDS);
        System.out.println(cacheResult.getMessage() + "----" + cacheResult.getResultCode() + "-----" + cacheResult.isSuccess());
    }

    @RequestMapping("test3")
    public void test2(String a) {
        CacheGetResult<Long> cacheResult = cache.GET(a);
        System.out.println(cacheResult.getMessage() + "----" + cacheResult.getResultCode() + "-----" + cacheResult.isSuccess() + "---" + cacheResult.getValue());
    }

    @RequestMapping("test4")
    public void test3(long id) {
        User user = cacheTest.getUserById(id);
        System.out.println(user.toString());
    }

    @RequestMapping("test5")
    public void test4(long id) {
        cacheTest.deleteUser(id);
    }

    @RequestMapping("test6")
    public void test5() {
        User user = new User();
        user.setId(1L);
        user.setUsername("da");
        cacheTest.updateUser(user);
    }

    @RequestMapping("test7")
    public void test6(long id) {
        System.out.println(cacheTest.getMap(id));
    }
}
