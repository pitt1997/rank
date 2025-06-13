package com.lijs.rank.ali;

/**
 * @author author
 * @date 2025-06-12
 * @description
 */
public class EagerSingleton {
    // 在类加载时就创建实例
    private static final EagerSingleton instance = new EagerSingleton();

    // 私有构造函数，防止外部实例化
    private EagerSingleton() {
        System.out.println("饿汉式单例实例被创建");
    }

    // 提供全局访问点
    public static EagerSingleton getInstance() {
        return instance;
    }

    // 示例方法
    public void showMessage() {
        System.out.println("这是饿汉式单例的方法");
    }

    public static void main(String[] args) {
        EagerSingleton.getInstance().showMessage();
    }
}