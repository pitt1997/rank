package com.lijs.rank.ali;

/**
 * @author author
 * @date 2025-06-12
 * @description
 */
public class ThreadSafeLazySingleton {
    private static ThreadSafeLazySingleton instance;

    private ThreadSafeLazySingleton() {
        System.out.println("线程安全懒汉式单例实例被创建");
    }

    // 同步方法，线程安全但性能较低
    public static synchronized ThreadSafeLazySingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeLazySingleton();
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("这是线程安全懒汉式单例的方法");
    }
}