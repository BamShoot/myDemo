package com.bamboo.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;


@Aspect
@Component
public class DemoAspect {

    //类和方法各需一个*
    @Pointcut("execution(public * com.bamboo.demo.aspect.controller.*.*(..))")
    private void demo() {
    }

    @Around("demo()")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();//让拦截的方法执行
        Signature signature = joinPoint.getSignature();
        String toString = signature.toString();//拦截的方法
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();//拦截的方法形参列表
        String name = parameters[0].getName();//拦截方法的形参
        //将RequestParam注解修饰的请求形参
        parameters[0].getAnnotation(RequestParam.class);
        Object[] args = joinPoint.getArgs();//拦截的方法参数列表

        return result;
    }


    public String findReplaceString(String S, int[] indexes, String[] sources, String[] targets) {
        int len=indexes.length;
        HashMap map=new HashMap();
        for(int i=0;i<len;i++){

            String s1 = S.substring(indexes[i], sources[i].length());
            S = S.replaceFirst(s1, i + "");
            map.put(i,s1);

        }

        return null;
    }
}
