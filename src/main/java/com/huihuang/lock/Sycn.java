package com.huihuang.lock;

public abstract class Sycn implements Lock{

    protected static final String LOCK_KEY = "LOCK_KEY";
    protected static final String LOCK_SUCCESS = "OK";
    protected static final Integer DEFAULT_LOCK_TIME = 20000;// 默认锁定时间毫秒
    private static final Long DEFAULT_SLEEP_TIME = 50L;// 默认sleep时间,100毫秒

    public final String lock() {
        //自旋去获取锁
        String result = null;
        while (null == (result = tryAcquire(1))){
            try {
                Thread.sleep(DEFAULT_SLEEP_TIME);
            }catch (Exception e){
            }
        }
        return result;
    }

    protected abstract String tryAcquire(Integer arg);

    public final void unlock(String value) {
        tryRelease(value, 1);
    }

    protected abstract boolean tryRelease(String value,Integer arg);
}
