package com.lijs.rank.thread;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadLocal 内存泄漏演示
 * <p>
 * 存储原理: 每个 Thread 内部有一个 ThreadLocalMap
 * Thread -> ThreadLocalMap -> Entry{ key: WeakReference&lt;ThreadLocal&gt;, value: 强引用 }
 * 调用 threadLocal.set(v) 就是往"当前线程自己的 map"里存了一条 Entry，所以数据是线程私有的。
 * <p>
 * 泄漏原因:
 * 1. Entry 的 key 是弱引用，外部不再强引用 ThreadLocal 对象时，key 会被 GC 回收；
 * 2. 但 value 依然被 Entry 强引用着，引用链 Thread -> Map -> Entry -> value 一直可达；
 * 3. 只要线程不死（线程池核心线程几乎永不销毁），value 就永远回收不掉 —— 这就是泄漏。
 * <p>
 * 运行方式（反射窥探 ThreadLocalMap 需要打开 java.lang 的访问权限）:
 * java --add-opens java.base/java.lang=ALL-UNNAMED ThreadLocalMemoryLeak.java
 *
 * @author ljs
 * @date 2026-10-10
 * @description
 */
public class ThreadLocalMemoryLeak {

    static class BigObject {
        final byte[] data = new byte[1024 * 1024]; // 1MB
    }

    public static void main(String[] args) throws Exception {
        // 线程池里的核心线程基本不销毁，相当于 Thread 一直存活，是泄漏的高发场景
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(ThreadLocalMemoryLeak::leakDemo).get(); // 在池线程上演示泄漏
        pool.shutdown();

        correctDemo(); // 在主线程上演示正确用法（两个线程的 ThreadLocalMap 相互独立）
    }

    /**
     * 泄漏场景：模拟"每次请求都 new 一个 ThreadLocal"（比如 ThreadLocal 定义在随请求重建的对象里），
     * 用完不 remove()。每个 ThreadLocal 对应 map 里的一条 Entry，key 被 GC 后 Entry 变成僵尸条目，
     * value 永久滞留 —— 泄漏随请求次数无上限增长。
     */
    private static void leakDemo() {
        List<WeakReference<ThreadLocal<BigObject>>> refs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ThreadLocal<BigObject> tl = new ThreadLocal<>();
            tl.set(new BigObject());            // Entry: key=tl(弱引用), value=1MB(强引用)
            refs.add(new WeakReference<>(tl)); // 用于观测 ThreadLocal 对象本身是否被回收
            tl = null;                          // 模拟方法结束，外部强引用消失
        }

        System.gc();
        sleep(100);

        long alive = refs.stream().filter(r -> r.get() != null).count();
        System.out.println("[泄漏场景] 5 个 ThreadLocal 对象被 GC 后存活数(弱引用观测): " + alive);
        inspect(); // 预期: 5 条僵尸 Entry，约 5MB value 回收不掉
    }

    /**
     * 正确用法：try/finally 中 remove()，Entry 整条被删掉，value 失去强引用，可被正常回收。
     * （即使 ThreadLocal 是 static 的、set 会覆盖旧值，不 remove 也会让上一个 value 滞留到下次覆盖为止）
     */
    private static void correctDemo() {
        ThreadLocal<BigObject> tl = new ThreadLocal<>();
        try {
            tl.set(new BigObject());
            // ... 业务逻辑 ...
        } finally {
            tl.remove();
        }

        System.gc();
        sleep(100);
        System.out.println("[正确用法] remove() 之后:");
        inspect(); // 预期: 0 条僵尸 Entry
    }

    /**
     * 反射窥探当前线程的 ThreadLocalMap，统计"key 已被 GC 但 value 仍存活"的僵尸 Entry。
     */
    private static void inspect() {
        try {
            Field mapField = Thread.class.getDeclaredField("threadLocals");
            mapField.setAccessible(true);
            Object map = mapField.get(Thread.currentThread());
            Field tableField = map.getClass().getDeclaredField("table");
            tableField.setAccessible(true);
            Object[] table = (Object[]) tableField.get(map);
            Field valueField = table.getClass().getComponentType().getDeclaredField("value");
            valueField.setAccessible(true);

            int stale = 0, live = 0, leakedMb = 0;
            for (Object entry : table) {
                if (entry == null) {
                    continue;
                }
                Object key = ((java.lang.ref.Reference<?>) entry).get(); // Entry 继承 WeakReference
                Object value = valueField.get(entry);
                if (key == null && value instanceof BigObject) {
                    stale++;
                    leakedMb += ((BigObject) value).data.length / (1024 * 1024);
                } else if (key != null) {
                    live++;
                }
            }
            System.out.println("    ThreadLocalMap: 正常 Entry = " + live + ", 僵尸 Entry(key已回收但value滞留) = "
                    + stale + ", 泄漏约 " + leakedMb + " MB");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}