package com.lijs.rank.nacos;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * @author ljs
 * @date 2024-12-30
 * @description
 */
@Component
public class NacosConfigUpdateListener {

    @Autowired
    private DynamicThreadPoolManager threadPoolManager;

    @NacosConfigListener(dataId = "threadpool-config.properties", timeout = 3000)
    public void onChange(String newConfig) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(newConfig));
            int corePoolSize = Integer.parseInt(properties.getProperty("threadpool.corePoolSize", "10"));
            int maximumPoolSize = Integer.parseInt(properties.getProperty("threadpool.maximumPoolSize", "20"));
            int queueCapacity = Integer.parseInt(properties.getProperty("threadpool.queueCapacity", "100"));
            // 更新线程池配置
            threadPoolManager.updateThreadPool(corePoolSize, maximumPoolSize, queueCapacity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
