package com.cemarose.elasticsearch.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.lookup.SearchLookup;

import java.util.Map;

public class CollectionSortFactory implements SearchScript.Factory {
    private final static Logger logger = LogManager.getLogger(CollectionSortFactory.class);

    private Client client;

    public CollectionSortFactory(Client client) {
//        logger.info("factory created");
        this.client = client;
    }

    @Override
    public SearchScript.LeafFactory newFactory(Map<String, Object> params, SearchLookup lookup) {
//        logger.info("run factory => {}, lookup => {}", params, lookup);
        return new CollectionSortLeafFactory(params, lookup, client);
    }
}
