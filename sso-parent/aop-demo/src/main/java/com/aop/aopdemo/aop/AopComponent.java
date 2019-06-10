package com.aop.aopdemo.aop;

import com.aop.aopdemo.annotation.LoginLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@Aspect
public class AopComponent {
    @Pointcut("execution(public * com.aop.aopdemo.rest.*.*(..))")
    public void pointCut(){

    }
    @Around(value = "pointCut()")
    public void doLog(ProceedingJoinPoint point) throws Throwable {
        System.out.println("环绕切面 前");
        point.proceed();
        System.out.println("环绕切面 后");
    }

    @Before(value = "pointCut()")
    public void check() throws Throwable {
        System.out.println("前置切面");
    }

    @After(value = "pointCut()")
    public void log() throws Throwable {
        System.out.println("后置切面");
    }
    @Around(value = "pointCut()")
    public void aroundDemo(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        for (Object arg : args) {
            System.out.println("getArgs:"+arg);
        }
        //获取方法签名
        Signature signature = point.getSignature();
        MethodSignature methodSignature=(MethodSignature)signature;
        //获取方法
        Method method = methodSignature.getMethod();
        //获取参数列表
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            String name = parameter.getName();
            Executable declaringExecutable = parameter.getDeclaringExecutable();
            String name1 = declaringExecutable.getName();
            System.out.println(name);
        }
        String name = signature.getName();
        System.out.println("getSignature().getName():"+name);

        Class declaringType = signature.getDeclaringType();
        String declaringTypeName = signature.getDeclaringTypeName();
        int modifiers = signature.getModifiers();

        String kind = point.getKind();
        Object target = point.getTarget();
        Object aThis = point.getThis();
        point.proceed();
    }
    @Pointcut("@annotation(com.aop.aopdemo.annotation.LoginLimit)")
    public void annotationAop(){

    }
    @Around("annotationAop()")
    public void checkAuth(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        LoginLimit annotation = method.getAnnotation(LoginLimit.class);
        boolean value = annotation.value();
        System.out.println("注解切面 前");
        proceedingJoinPoint.proceed();
        System.out.println("注解切面 后");
    }
}
