package com.mnnu.examine.common.utils;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * todo 测试
 *
 * @author qiaoh
 */
@Component
public class RedisUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;


    public static final Gson gson = new Gson();


    /**
     * 不过期的键值对
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, toJson(value));
    }

    /**
     * 带有过期时间的键值对
     *
     * @param key      键
     * @param value    值
     * @param expire   过期时间
     * @param timeUnit 时间单位
     */
    public void setExpire(String key, Object value, long expire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, toJson(value), expire, timeUnit);
    }


    public void setExpire(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, toJson(value), expire, TimeUnit.SECONDS);
    }

    public <T> T get(String key, Class<T> clazz) {
        String obj = stringRedisTemplate.opsForValue().get(key);
        return obj == null ? null : fromJson(key, clazz);
    }

    /**
     * 获取后销毁，expire -1表示不过期
     *
     * @param key      键
     * @param clazz    value 类型
     * @param expire   过期时间
     * @param timeUnit 时间单位
     * @param <T>
     * @return
     */
    public <T> T getAndExpire(String key, Class<T> clazz, long expire, TimeUnit timeUnit) {
        if (expire == -1) {
            get(key, clazz);
        }
        String obj = stringRedisTemplate.opsForValue().get(key);
        stringRedisTemplate.expire(key, expire, timeUnit);
        return obj == null ? null : fromJson(key, clazz);

    }

    public String get(String key) {
        return String.valueOf(redisTemplate.opsForValue().get(key));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }


    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

}
