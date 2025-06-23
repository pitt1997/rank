package com.lijs.rank.threadpool.dynamic_thread_pool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolRefreshListener {

    @Autowired
    private DynamicThreadPoolManager threadPoolManager;

    @EventListener
    public void onRefresh(RefreshEvent event) {
        threadPoolManager.refresh();
    }
}