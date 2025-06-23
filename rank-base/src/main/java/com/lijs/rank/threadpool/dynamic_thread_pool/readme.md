## ✅ 一句话总结方案：

> 将线程池的核心参数（核心线程数、最大线程数、队列容量、拒绝策略等）放在 Nacos 中监听，当配置发生变化时，动态更新线程池实例。

* * *

## ✅ 核心思路步骤

1.  **使用 Nacos 动态配置线程池参数（如 JSON）**
1.  **注册配置监听器（如 Spring Cloud Alibaba Nacos 提供的监听机制）**
1.  **创建可变参数的线程池（线程池参数必须支持修改）**
1.  **配置变更后，更新线程池参数而不重建线程池对象**

* * *

## 🧩 示例实现（Spring Boot + Nacos）

### 1️⃣ Nacos 中配置如下（Data ID: `thread-pool-config.yaml`）

```
yaml
复制编辑
thread.pool:
  coreSize: 2
  maxSize: 10
  queueCapacity: 100
  keepAliveSeconds: 60
  threadNamePrefix: "biz-thread-"
```

* * *

### 2️⃣ 配置类（自动刷新）

```
java
复制编辑
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolProperties {
    private int coreSize;
    private int maxSize;
    private int queueCapacity;
    private int keepAliveSeconds;
    private String threadNamePrefix;
}
```

* * *

### 3️⃣ 创建一个可变参数线程池（包装 ThreadPoolExecutor）

```
java
复制编辑
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

@Component
public class DynamicThreadPoolManager {

    private ThreadPoolExecutor executor;

    @Autowired
    private ThreadPoolProperties properties;

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    @PostConstruct
    public void init() {
        executor = new ThreadPoolExecutor(
                properties.getCoreSize(),
                properties.getMaxSize(),
                properties.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getQueueCapacity()),
                new NamedThreadFactory(properties.getThreadNamePrefix()),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    // 刷新线程池参数
    public void refresh() {
        System.out.println("线程池参数刷新...");
        executor.setCorePoolSize(properties.getCoreSize());
        executor.setMaximumPoolSize(properties.getMaxSize());
        executor.setKeepAliveTime(properties.getKeepAliveSeconds(), TimeUnit.SECONDS);
        // ⚠️ 队列容量不能直接修改（可创建新队列替换）
    }

    public static class NamedThreadFactory implements ThreadFactory {
        private final String prefix;
        private int count = 1;

        public NamedThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, prefix + count++);
        }
    }
}
```

* * *

### 4️⃣ 监听配置变化并动态刷新线程池

```
java
复制编辑
import com.alibaba.cloud.nacos.refresh.event.RefreshEvent;
import org.springframework.beans.factory.annotation.Autowired;
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
```

* * *

## ⚠️ 注意事项

| 点                | 说明                                        |
| ---------------- | ----------------------------------------- |
| 核心线程数、最大线程数、存活时间 | 可动态调整                                     |
| 队列容量             | `LinkedBlockingQueue` 不支持直接修改大小，若需修改需替换队列 |
| 拒绝策略             | 可实现动态修改，用反射或策略注册表                         |
| 线程名/前缀           | 对已有线程不生效，仅影响新线程                           |

* * *

## ✅ 总结流程图

```
text
复制编辑
   [Nacos配置变更]
           ↓
  [Spring Cloud刷新事件]
           ↓
   [@EventListener监听]
           ↓
  [调用 ThreadPoolExecutor.setXXX 方法]
           ↓
    [线程池参数动态更新]
```

* * *

## 🧪 测试方法建议

1.  初始化线程池 → 提交任务
1.  修改 Nacos 配置，比如调整 coreSize、maxSize
1.  看日志 `线程池参数刷新...`，验证 `corePoolSize` 变化
1.  提交更多任务 → 验证扩容生效

* * *

## ✅ 如需更强功能

可引入成熟框架 [**dynamic-threadpool**](https://github.com/opengoofy/dynamic-thread-pool)（Goofy 出品），支持：

-   多线程池注册管理
-   配置中心同步
-   监控指标
-   Web 控制台