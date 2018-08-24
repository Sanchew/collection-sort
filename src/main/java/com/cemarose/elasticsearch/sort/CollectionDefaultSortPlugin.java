package com.cemarose.elasticsearch.sort;

import com.cemarose.elasticsearch.engine.CollectionDefaultSortScriptEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.ScriptPlugin;
import org.elasticsearch.script.ScriptContext;
import org.elasticsearch.script.ScriptEngine;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.watcher.ResourceWatcherService;

import java.util.Collection;

/**
 * Created by Sanchew on 2018/8/20.
 */
public class CollectionDefaultSortPlugin extends Plugin implements ScriptPlugin {

    Logger logger = LogManager.getLogger(CollectionDefaultSortPlugin.class);

    private Client client;
    private CollectionDefaultSortScriptEngine engine = new CollectionDefaultSortScriptEngine();

    @Override
    public Collection<Object> createComponents(Client client, ClusterService clusterService, ThreadPool threadPool, ResourceWatcherService resourceWatcherService, ScriptService scriptService, NamedXContentRegistry xContentRegistry, Environment environment, NodeEnvironment nodeEnvironment, NamedWriteableRegistry namedWriteableRegistry) {
        logger.info("createComponents => {}", client);
        this.client = client;
        engine.client = client;
        return super.createComponents(client, clusterService, threadPool, resourceWatcherService, scriptService, xContentRegistry, environment, nodeEnvironment, namedWriteableRegistry);
    }

    public ScriptEngine getScriptEngine(Settings settings, Collection<ScriptContext<?>> contexts) {
        logger.info("getScriptEngine => {}", contexts);
        return engine;
    }

}
