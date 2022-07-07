package com.szzcs.quickpay_device_workingv2.utils.cache;

/**
 * Created by wanglei on 2016/11/27.
 */

public interface ICache {
    void put(String key, Object value);

    Object get(String key);

    void remove(String key);

    boolean contains(String key);

    void clear();

}
