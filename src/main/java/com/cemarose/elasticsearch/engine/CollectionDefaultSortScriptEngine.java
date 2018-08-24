package com.cemarose.elasticsearch.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.LeafReaderContext;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.script.ScriptContext;
import org.elasticsearch.script.ScriptEngine;
import org.elasticsearch.script.SearchScript;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Sanchew on 2018/8/20.
 */
public class CollectionDefaultSortScriptEngine implements ScriptEngine {

    private final static Logger logger = LogManager.getLogger(CollectionDefaultSortScriptEngine.class);

    public Client client;

    public String getType() {
        return "native";
    }

    public <FactoryType> FactoryType compile(String name, String code, final ScriptContext<FactoryType> context, Map<String, String> params) {
        SearchScript.Factory factory = (pm, lookup) -> new SearchScript.LeafFactory() {
            {
//                logger.info("factory => {}, thread => {} ",this, Thread.currentThread().getId());
            }
            public SearchScript newInstance(LeafReaderContext ctx) throws IOException {
//                logger.info("call newInstance => {}, thread => {}", this, Thread.currentThread().getId());
                return new SearchScript(pm, lookup, ctx) {
                    @Override
                    public double runAsDouble() {
//                        logger.info("runAsDouble => {}, thread => {}", this, Thread.currentThread().getId());
//                        logger.info(lookup.doc().getLeafDocLookup(ctx).keySet());
//                        logger.info(lookup.doc().getTypes());
                        logger.info("client => {}", client);
                        GetRequestBuilder builder = client.prepareGet("collections", "collection", pm.get("id").toString());
                        logger.info("builder => {}", builder);
                        GetResponse response = builder.get();
                        List<Object> collects = ((List<Object>) response.getSource().get("collects"));
                        for(Object obj: collects) {
                            Map<String, Object> collect = (Map<String, Object>) obj;
                            logger.info("compare _id => {}, productId => {}", getDoc().get("_id").get(0).toString(), collect.get("productId").toString());
                            if (collect.get("productId").toString().equals(getDoc().get("_id").get(0).toString())) {
                                return Double.parseDouble(collect.get("position").toString());
                            }
                        }
                        return 1;
                    }
                };
            }

            public boolean needs_score() {
                return false;
            }
        };
        return context.factoryClazz.cast(factory);
    }

    public void close() throws IOException {

        logger.info("close => ");
    }
}
