package com.huihuang.lock;

public interface Lock {

    public String lock();

    public void unlock(String value);
}
