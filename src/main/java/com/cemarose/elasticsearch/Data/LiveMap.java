package com.cemarose.elasticsearch.Data;

import java.util.HashMap;

public class LiveMap extends HashMap<String, Integer> {
    private Long created;
    private Long validityPeriod;

    public LiveMap(Long validityPeriod) {
        super();
        created = System.currentTimeMillis();
        this.validityPeriod = validityPeriod;
    }

    public boolean isLived() {
        return System.currentTimeMillis() < created + validityPeriod;
    }
}
