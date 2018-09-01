package com.cemarose.elasticsearch.Data;

import java.util.HashMap;

public class CacheMap extends HashMap<String, LiveMap> {
    private Long validityPeriod;


    public CacheMap(Long validityPeriod) {
        super();
        this.validityPeriod = validityPeriod;
    }

    @Override
    public LiveMap put(String key, LiveMap value) {
        return super.put(key, value);
    }

    @Override
    public LiveMap get(Object key) {
        LiveMap map = super.get(key);
        if (map != null && map.isLived()) {
            return map;
        }
        return null;
    }
}
