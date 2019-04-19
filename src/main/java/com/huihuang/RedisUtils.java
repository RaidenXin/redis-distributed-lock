package com.huihuang;

import com.huihuang.config.RedisConfigration;
import com.huihuang.utils.ObjectUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

public class RedisUtils {

    private static final JedisPool jedisPool = new JedisPool(new GenericObjectPoolConfig(), RedisConfigration.url, RedisConfigration.port, 100, "123456");
    private static final RedisUtils redisUtils = new RedisUtils();
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    public void set(String key,Object value){
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, (String) value);
    }

    public <T> T get(String key){
        Jedis jedis = jedisPool.getResource();
        try {
            return (T) jedis.get(key);
        }finally {
            jedis.close();
        }
    }

    public String setnx(String key, String value, Integer expireMillis){
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireMillis);
        }finally {
            jedis.close();
        }
    }

    public Object eval(String script,String fullKey,String value){
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.eval(script, Collections.singletonList(fullKey), Collections.singletonList(value));
        }finally {
            jedis.close();
        }
    }

    public static RedisUtils newInstance(){
        return redisUtils;
    }
}
