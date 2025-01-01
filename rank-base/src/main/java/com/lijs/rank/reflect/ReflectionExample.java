package com.lijs.rank.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射（Reflection） 是 Java 提供的一种机制，它允许程序在运行时动态地获取类的结构信息（如类名、方法、字段、构造函数等），并对类的成员（包括私有成员）进行操作。
 * <p>
 * 通过反射，程序可以在运行时：
 * <p>
 * 加载类：根据类名动态加载类。
 * 获取类信息：如类的字段、方法、构造函数等。
 * 操作类成员：包括访问和修改字段值，调用方法，创建对象实例等。
 * 反射主要通过 java.lang.reflect 包中的类实现，如 Class、Field、Method 和 Constructor。
 * <p>
 * 注意事项
 * 性能开销：反射比直接调用慢，不建议频繁使用。
 * 访问控制：反射可以绕过访问修饰符的限制，但在实际使用中要遵守规范，避免滥用。
 * 安全性：可能引发安全漏洞，如访问或修改敏感字段。
 *
 * @author ljs
 * @date 2024-12-30
 * @description
 */
public class ReflectionExample {

    // 定义一个示例类
    static class Example {
        private String message = "Hello, Reflection!";

        private void privateMethod() {
            System.out.println("Private method called: " + message);
        }

        public void publicMethod(String additionalMessage) {
            System.out.println("Public method called: " + message + ", " + additionalMessage);
        }
    }

    public static void main(String[] args) {
        try {
            // 1. 获取 Class 对象
            Class<?> exampleClass = Example.class;

            // 2. 创建对象实例
            Example exampleInstance = (Example) exampleClass.getDeclaredConstructor().newInstance();

            // 3. 获取字段并修改值
            Field field = exampleClass.getDeclaredField("message");
            field.setAccessible(true); // 绕过访问限制
            field.set(exampleInstance, "Reflection is powerful!");

            // 4. 调用私有方法
            Method privateMethod = exampleClass.getDeclaredMethod("privateMethod");
            privateMethod.setAccessible(true); // 绕过访问限制
            privateMethod.invoke(exampleInstance);

            // 5. 调用公共方法
            Method publicMethod = exampleClass.getMethod("publicMethod", String.class);
            publicMethod.invoke(exampleInstance, "Additional info");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}