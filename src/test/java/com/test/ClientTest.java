package com.test;

import com.huihuang.RedisUtils;
import com.huihuang.lock.RedisDistributedLock;
import com.huihuang.utils.ObjectUtils;
import com.test.model.User;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ClientTest {

    @Test
    public void testLock() throws InterruptedException {
        System.out.println("##模拟生成订单号开始...");
        for (int i = 0; i < 100; i++) {
            new Thread(new OrderService()).start();
        }
        Thread.sleep(35000);
        assert OrderNumGenerator.getCount() == 100;
    }

    @Test
    public void testGet() throws InterruptedException {
        RedisDistributedLock lock = new RedisDistributedLock();
        String value = lock.lock();
        Thread.sleep(1000);
        lock.unlock(value);
        String value2 = lock.lock();
        Thread.sleep(1000);
        lock.unlock(value2);
    }
    @Test
    public void testUtils(){
        User user = new User();
        user.setAge(16);
        user.setName("张三");
        user.setId("1");
        RedisUtils redisUtils = new RedisUtils();
        redisUtils.set("1", user);
        User value = redisUtils.get("1");
        System.err.println(value);
    }
    @Test
    public void test(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("3", "张三");
        map.put("1", "李一一");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            int weight = Integer.valueOf(entry.getKey());
            for (int i = 0; i < weight; i++) {
                System.err.println(entry.getValue());
            }
        }
    }
}
