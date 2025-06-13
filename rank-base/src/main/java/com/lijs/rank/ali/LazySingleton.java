package com.lijs.rank.ali;

/**
 * @author author
 * @date 2025-06-12
 * @description
 */
public class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton() {
        System.out.println("懒汉式单例实例被创建");
    }

    // 非线程安全版本
    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("这是懒汉式单例的方法");
    }
}