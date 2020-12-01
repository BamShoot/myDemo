package com.bamboo.jetcache;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 1、缓存的类如果是实体类，必须实现序列化接口
 * 2、调用缓存的方法时，该类必须通过spring ioc获取
 */
@Component
public class CacheService {

    @Cached(name = "userCache.", key = "#id", expire = 3600, cacheType = CacheType.REMOTE)
    public User getUserById(long id) {
        System.out.println("Cached---" + System.currentTimeMillis());
        User user = new User();
        user.setId(id);
        user.setUsername("xiao" + System.currentTimeMillis());
        return user;
    }

    @CacheUpdate(name = "userCache.", key = "#user.id", value = "#user")
    public void updateUser(User user) {
        System.out.println("CacheUpdate---" + user.toString());
    }

    @CacheInvalidate(name = "userCache.", key = "#id")
    public void deleteUser(long userId) {
        System.out.println("CacheInvalidate---" + userId);
    }

    @Cached(name = "mapCache.", key = "#id", expire = 3600, cacheType = CacheType.REMOTE)
    public Map getMap(long id) {
        System.out.println("Cached---" + System.currentTimeMillis());
        HashMap map = new HashMap();
        map.put(id, System.currentTimeMillis());
        return map;
    }
}
