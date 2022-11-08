package com.liuyun.swiftmcweb.demo.biz.dal;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存 Dao
 *
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
@Component
public class CacheDao {

    private final static Map<String, CacheValueEntry> db0 = new HashMap<>();

    public void set(String key, Object val, long timeoutSeconds) {
        CacheValueEntry entry = new CacheValueEntry();
        entry.key = key;
        entry.value = val;
        entry.expireTime = currentTs() + timeoutSeconds;
        db0.put(key, entry);
    }

    public Object get(String key) {
        if(isExpire(key)) {
            return null;
        }
        return db0.get(key).value;
    }

    public void delete(String key) {
        db0.remove(key);
    }

    public boolean isExpire(String key) {
        CacheValueEntry entry = db0.get(key);
        return entry == null || entry.expireTime < currentTs();
    }

    private int currentTs() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    private static class CacheValueEntry {
        String key;
        Object value;
        long expireTime;
    }

}
