package com.lijs.rank.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * AOP
 * 1. AOP (面向切面编程)
 * AOP（Aspect-Oriented Programming，面向切面编程）是一种编程范式，它关注的是如何将横切关注点（cross-cutting concerns）从业务逻辑中
 * 分离出来。横切关注点是指在多个模块之间共享的功能，比如日志记录、事务管理、权限校验等，这些功能通常不直接影响核心业务逻辑。
 * 在 Spring 中，AOP 主要通过代理模式实现，可以通过 JDK 动态代理 或 CGLIB 代理 来实现对目标对象的增强。
 * <p>
 * 2. Aspect（切面）
 * Aspect 是 AOP 的核心概念，代表一个横切关注点的模块化。它包含了增强逻辑（所谓的“切面逻辑”），也就是你希望在特定的地方（如方法调用之前、之后
 * 或者异常抛出时等）执行的代码。Aspect 是在 AOP 中应用的核心构建块。
 * Aspect 的组成：
 *      1.Advice（增强）：增强功能，定义了“在何时、如何增强”目标方法。常见的增强类型包括：
 *          "@Before：方法执行前。"
 *          "@After：方法执行后。"
 *          "@Around：方法执行前后。"
 *          "@AfterReturning：方法成功执行后。"
 *          "@AfterThrowing：方法抛出异常后。 "
 *      2.Pointcut（切点）：定义在哪些方法上应用增强。切点定义了切面要“切入”的地方（例如某些方法的调用）。
 *      3.JoinPoint（连接点）：指的是方法执行的某个点，通常是方法的调用。
 * 在 Spring 中，@Aspect 注解用于定义一个切面类，@Pointcut 用于定义切点，@Before、@After、@Around 等注解则用于指定具体的增强逻辑。
 *
 * @date 2024-12-30
 * @description
 */
@Aspect  // 1、切面!!! 定义一个切面
public class LoggingAspect {

    // 2、切点!!! 定义了在哪些方法上应用增强
    // 匹配所有 public 方法
    @Pointcut("execution(public * *(..))")
    public void publicMethods() {
    }

    // 匹配带 @Transactional 的方法
    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionalMethods() {
    }

    // 组合切点
    @Pointcut("publicMethods() && transactionalMethods()")
    public void publicAndTransactionalMethods() {
    }

    // 匹配接口中的所有方法
    @Pointcut("execution(* com.example.service.*.*(..))")
    public void allMethodsInServiceInterface() {
    }

    // 匹配某个接口的特定方法
    @Pointcut("execution(* com.example.service.UserService.find*(..))")
    public void findMethodsInUserService() {
    }

    // 定义切点，匹配 UserService 类的所有方法
    @Pointcut("execution(* com.example.service.UserService.*(..))")
    public void userServiceMethods() {
    }

    // 3、增强!!!定义了“在何时、如何增强”目标方法的逻辑。包括 @Before、@After、@Around 等。
    // 在目标方法执行前执行增强逻辑
    @Before("userServiceMethods()")
    public void logBefore(JoinPoint joinPoint) { // 4、连接点!!! JoinPoint连接点，指方法执行的某个点，通常是方法的调用。
        System.out.println("Before method: " + joinPoint.getSignature().getName());
    }

    // 在目标方法执行后执行增强逻辑
    @After("userServiceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("After method: " + joinPoint.getSignature().getName());
    }

}