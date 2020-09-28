package com.auto_logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MyProxifier {

    public static Object getProxifiedObject(Object objectForProxying) {
        InvocationHandler invocationHandler = new MyInvocationHandler(objectForProxying);
        return Proxy.newProxyInstance(MyProxifier.class.getClassLoader(), objectForProxying.getClass().getInterfaces(), invocationHandler);
    }

    private static class MyInvocationHandler implements InvocationHandler {
        private final Object objectForProxying;
        private final Set<Method> annotatedMethods;

        public MyInvocationHandler(Object objectForProxying) {
            this.objectForProxying = objectForProxying;
            Class<?>[] interfaces = objectForProxying.getClass().getInterfaces();
            annotatedMethods = Arrays.stream(interfaces).map(Class::getMethods).flatMap(Arrays::stream).filter(method -> method.getDeclaredAnnotation(Log.class) != null).collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
            if (annotatedMethods.contains(method)) {
                System.out.print("executed method: " + method.getName() + " with params: ");
                String params = Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "));
                System.out.println(params);
            }
            return method.invoke(objectForProxying, args);
        }
    }
}
