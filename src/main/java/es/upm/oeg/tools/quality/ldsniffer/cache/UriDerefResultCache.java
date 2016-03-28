package es.upm.oeg.tools.quality.ldsniffer.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Caching;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Copyright 2014-2016 Ontology Engineering Group, Universidad Politécnica de Madrid, Spain
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Nandana Mihindukulasooriya
 * @since 1.0.0
 */
public class UriDerefResultCache implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(UriDerefResultCache.class);

    private static Cache<String, Integer> cache;

    private CacheManager cacheManager;

    /**
     * * Notification that the web application initialization process is starting.
     * All ServletContextListeners are notified of context initialization before
     * any filter or servlet in the web application is initialized.
     *
     * @param sce Information about the ServletContext that was initialized
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        cacheManager
                = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("uriCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Integer.class))
                .build();
        cacheManager.init();

        cache = cacheManager.getCache("uriCache", String.class, Integer.class);

    }

    /**
     * * Notification that the servlet context is about to be shut down. All
     * servlets and filters have been destroy()ed before any
     * ServletContextListeners are notified of context destruction.
     *
     * @param sce Information about the ServletContext that was destroyed
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        cacheManager.removeCache("uriCache");
        cacheManager.close();;

    }


    public static void put(String uri, int code) {
        if (cache != null) {
            cache.put(uri, code);
        } else {
            throw new IllegalStateException("Cache is null ...");
        }
    }

    public static int get(String uri) {
        if (cache != null) {
            cache.get()
        } else {
            throw new IllegalStateException("Cache is null ...");
        }
    }
}
