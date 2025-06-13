package com.lijs.rank.ali;

/**
 * @author author
 * @date 2025-06-12
 * @description
 */
public class DoubleCheckedLockingSingleton {
    // 使用volatile保证可见性和禁止指令重排序
    private static volatile DoubleCheckedLockingSingleton instance;

    private DoubleCheckedLockingSingleton() {
        System.out.println("双重检查锁定单例实例被创建");
    }

    public static DoubleCheckedLockingSingleton getInstance() {
        if (instance == null) { // 第一次检查
            synchronized (DoubleCheckedLockingSingleton.class) {
                if (instance == null) { // 第二次检查
                    instance = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return instance;
    }

    public void showMessage() {
        System.out.println("这是双重检查锁定单例的方法");
    }
}