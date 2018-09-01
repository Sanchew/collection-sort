package com.cemarose.elasticsearch.script;

import com.cemarose.elasticsearch.factory.CollectionSortLeafFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.LeafReaderContext;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.lookup.SearchLookup;

import java.util.List;
import java.util.Map;

public class CollectionSortSearchScript extends SearchScript {
    private final static Logger logger = LogManager.getLogger(CollectionSortSearchScript.class);

    private CollectionSortLeafFactory leafFactory;

    public CollectionSortSearchScript(Map<String, Object> params, SearchLookup lookup, LeafReaderContext leafContext, CollectionSortLeafFactory leafFactory) {
        super(params, lookup, leafContext);
//        logger.info("search script created, lookup => {}, ctx => {}", lookup, leafContext);
        this.leafFactory = leafFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public double runAsDouble() {
//        logger.info("run as double => {}, thread => {}, id => {}", getParams(), Thread.currentThread().getId(), getDoc().get("_id"));
        Integer score = leafFactory.getScores().get(getDoc().get("_id").get(0));
//        Double score = Math.random();
        return score;
    }
}
