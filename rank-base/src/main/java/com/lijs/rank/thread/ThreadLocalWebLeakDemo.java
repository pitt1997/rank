package com.lijs.rank.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal 内存泄漏 —— 真实 Web 场景版
 * <p>
 * 模拟一个最常见的业务写法（Spring 的 RequestContextHolder、PageHelper 分页插件都是这套用法）：
 * 1. 登录拦截器把"当前登录用户"放进 ThreadLocal，Controller 里随时能取，不用层层传参 —— 这是它"能存数据"的价值；
 * 2. 审计日志收集器用 ThreadLocal&lt;List&gt; 做请求级缓冲，业务代码里随手 add，请求结束统一上报。
 * <p>
 * 隐患：请求结束后没有 remove()，而 Tomcat 工作线程是线程池、长期存活，
 * 引用链 Thread(不死) -> ThreadLocalMap -> Entry -> List/User 一直强可达，
 * GC 只认"不可达"，不认"业务上已经用完了"，于是：
 * 场景1: 下一个请求复用线程，读到上一个请求的用户 —— 数据串号；
 * 场景2: 每个请求只 add 不 clear，列表无限增长 —— 内存持续上涨，跑几天后 OOM。
 *
 * @author ljs
 * @date 2026-10-10
 * @description
 */
public class ThreadLocalWebLeakDemo {

    /**
     * 当前登录用户上下文（登录拦截器的典型用法）
     */
    static class UserContext {
        static final ThreadLocal<String> CURRENT = new ThreadLocal<>();
    }

    /**
     * 一条 1KB 的日志内容（JDK 8 没有 String.repeat，用 replace 填充）
     */
    static final String BIG_LOG_LINE = new String(new char[1000]).replace('\0', 'x');

    /**
     * 审计日志缓冲区（请求级累积器的典型用法）
     */
    static class AuditCollector {
        static final ThreadLocal<List<String>> LOGS = ThreadLocal.withInitial(ArrayList::new);

        static void add(String operator) {
            LOGS.get().add(operator + " 执行了下单操作, 请求参数: " + BIG_LOG_LINE);
        }
    }

    /**
     * 一次请求的完整处理（clean=false 就是埋雷的写法，clean=true 是正确写法）
     */
    static void handleRequest(String userName, boolean clean) {
        try {
            if (userName != null) {
                UserContext.CURRENT.set(userName); // 拦截器: 登录用户放进上下文
            }
            AuditCollector.add(currentUser());     // 业务代码随手记审计日志，很方便
        } finally {
            if (clean) {
                UserContext.CURRENT.remove();      // 拦截器 afterCompletion: 必须清理
                AuditCollector.LOGS.remove();
            }
        }
    }

    static String currentUser() {
        String user = UserContext.CURRENT.get();
        return user != null ? user : "未登录";
    }

    public static void main(String[] args) throws Exception {
        dirtyDataDemo();
        memoryLeakDemo(false); // 不 remove
        memoryLeakDemo(true);  // finally 里 remove
    }

    /**
     * 场景1：脏数据 —— 线程复用导致"串号"。
     * 真实表现：线上偶现"未登录用户看到了别人的订单"，且无法稳定复现（取决于请求落在哪个线程）。
     */
    static void dirtyDataDemo() throws Exception {
        System.out.println("========== 场景1: 不 remove 导致数据串号 ==========");
        ExecutorService tomcat = Executors.newSingleThreadExecutor(r -> new Thread(r, "tomcat-worker"));

        tomcat.submit(() -> {
            handleRequest("张三", false); // 请求1: 张三已登录, 处理完没清理
            System.out.println("请求1: 用户[张三]登录并下单, 处理完成, 未清理上下文");
        }).get();

        tomcat.submit(() -> {
            String user = UserContext.CURRENT.get(); // 请求2: 这个用户根本没登录!
            System.out.println("请求2: 未登录请求查到的当前操作人 = "
                    + (user == null ? "null (正常)" : user + "  <-- 串号! 读到了张三的数据"));
        }).get();

        tomcat.shutdown();
    }

    /**
     * 场景2/3：内存泄漏 —— 请求全处理完了，内存却下不来。
     */
    static void memoryLeakDemo(boolean clean) throws Exception {
        System.out.println();
        System.out.println("========== 场景" + (clean ? "3: finally 里 remove() (正确)" : "2: 只 add 不 remove (泄漏)") + " ==========");
        ExecutorService tomcat = Executors.newFixedThreadPool(8); // 模拟 Tomcat 的 8 个工作线程

        long before = usedMemoryAfterGc();
        final int REQUESTS = 30000;
        for (int i = 1; i <= REQUESTS; i++) {
            final int id = i;
            tomcat.submit(() -> handleRequest("用户-" + (id % 100), clean));
        }
        // 窥探某个 tomcat 线程内部滞留了多少"已结束请求"的日志
        // (探针任务排在所有请求之后, 由 8 个复用线程之一执行, 读的是它自己的 ThreadLocal)
        tomcat.submit(() -> System.out.println("处理 " + REQUESTS + " 个请求(全部已完成)后, "
                + "某个 tomcat 线程内滞留的审计日志: " + AuditCollector.LOGS.get().size() + " 条")).get();

        // 必须在线程还活着时测量: full GC 后内存依然下不来, 才能证明这些对象"可达但无用"
        long after = usedMemoryAfterGc();
        System.out.println("full GC 后堆内存: " + before + "MB -> " + after + "MB"
                + (clean ? "  <-- 干净, 请求结束即回收" : "  <-- 请求都结束了内存却下不来, 这就是泄漏, 跑几天就是 OOM"));

        // 注: 线程死亡时 Thread.exit() 会顺带清空它的 ThreadLocalMap,
        //     但线程池里的线程恰恰永不退出, 所以不能指望这个兜底, 只能靠 remove()
        tomcat.shutdown();
        tomcat.awaitTermination(60, TimeUnit.SECONDS);
    }

    private static long usedMemoryAfterGc() throws InterruptedException {
        System.gc();
        Thread.sleep(200);
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
    }

}