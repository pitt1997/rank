package com.lijs.rank.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author ljs
 * @date 2024-12-30
 * @description
 */
interface HelloService {
    void sayHello(String name);
}

class HelloServiceImpl implements HelloService {
    public void sayHello(String name) {
        System.out.println("Hello, " + name);
    }
}

class HelloServiceInvocationHandler implements InvocationHandler {
    private final Object target;

    public HelloServiceInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before method call");
        Object result = method.invoke(target, args);
        System.out.println("After method call");
        return result;
    }
}

public class ProxyExample {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        HelloService proxy = (HelloService) Proxy.newProxyInstance(
                HelloService.class.getClassLoader(),
                new Class<?>[] {HelloService.class},
                new HelloServiceInvocationHandler(helloService)
        );
        proxy.sayHello("World"); // 输出: Before method call, Hello, World, After method call
    }
}
