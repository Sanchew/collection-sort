package com.cemarose.elasticsearch.factory;

import com.cemarose.elasticsearch.Data.CacheMap;
import com.cemarose.elasticsearch.Data.LiveMap;
import com.cemarose.elasticsearch.script.CollectionSortSearchScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.LeafReaderContext;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.lookup.SearchLookup;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionSortLeafFactory implements SearchScript.LeafFactory {
    private final static Logger logger = LogManager.getLogger(CollectionSortLeafFactory.class);

    private Map<String, Object> params;
    private SearchLookup lookup;
    private Client client;
    private static CacheMap scoresCache = new CacheMap(600000l);

    public CollectionSortLeafFactory(Map<String, Object> params, SearchLookup lookup, Client client) {
//        logger.info("leaf factory created");
        this.params = params;
        this.lookup = lookup;
        this.client = client;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SearchScript newInstance(LeafReaderContext ctx) throws IOException {
        return new CollectionSortSearchScript(params, lookup, ctx, this);
    }

    @Override
    public boolean needs_score() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Integer> getScores() {
        String id = params.get("id").toString();
        LiveMap scores = scoresCache.get(id);
        if (scores == null) {
            synchronized (id) {
                scores = scoresCache.get(id);
                if (scores != null) {
                    return scores;
                }
                long st = System.currentTimeMillis();
                GetRequestBuilder builder = client.prepareGet("collections", "collection", params.get("id").toString());
                GetResponse response = builder.get();
                List<Object> collects = ((List<Object>) response.getSource().get("collects"));
                scores = new LiveMap(600000l);
                for(Object obj: collects) {
                    Map<String, Object> collect = (Map<String, Object>) obj;
                    scores.put(collect.get("productId").toString(), (Integer)collect.get("position"));
                }
                scoresCache.put(id, scores);
                logger.info("get scores use => {}", (System.currentTimeMillis() - st));
            }
        }
        return scores;
    }
}
