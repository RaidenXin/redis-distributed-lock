package com.huihuang.lock;

import com.huihuang.RedisUtils;
import java.util.Objects;

public class RedisDistributedLock extends Sycn{

    private static final Long RELEASE_SUCCESS = 1L;


    protected String tryAcquire(Integer arg) {
        RedisUtils redisUtils = RedisUtils.newInstance();
        String value = getLockValue();
        String result = redisUtils.setnx(LOCK_KEY, value, DEFAULT_LOCK_TIME);
        if (LOCK_SUCCESS.equals(result)){
            return value;
        }
        return null;
    }

    protected boolean tryRelease(String value,Integer arg) {
        RedisUtils redisUtils = RedisUtils.newInstance();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//        System.err.println("开始解锁！");
        if (RELEASE_SUCCESS.equals(redisUtils.eval(script, LOCK_KEY, value))) {
//            System.err.println("解锁成功！！");
            return true;
        }
        return false;
    }

    private String getLockValue() {
        int random = (int) ((Math.random() * 9 + 1) * 100000);
        long now = System.currentTimeMillis();
        return String.valueOf(now) + String.valueOf(random);
    }
}
