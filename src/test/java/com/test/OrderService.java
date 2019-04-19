package com.test;

import com.huihuang.lock.Lock;
import com.huihuang.lock.RedisDistributedLock;

// 订单生成调用业务逻辑
public class OrderService implements Runnable {
	// 生成订单号
	OrderNumGenerator orderNumGenerator = new OrderNumGenerator();
//	private Lock lock = new DistributedLock();
	private Lock lock = new RedisDistributedLock();

	public void run() {
        String value = lock.lock();
        try {
            // 上锁
			// synchronized (this) {
			// 模拟用户生成订单号
			getNumber();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 釋放鎖資源
			lock.unlock(value);
		}
	}

	public void getNumber() {
		String number = orderNumGenerator.getNumber();
		System.out.println(Thread.currentThread().getName() + ",##number:" + number);
	}
}
