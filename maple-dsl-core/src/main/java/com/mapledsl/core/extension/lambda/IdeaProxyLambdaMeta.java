package com.mapledsl.core.extension.lambda;

import com.mapledsl.core.exception.MapleDslReflectionException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static com.mapledsl.core.extension.lambda.LambdaMeta.setAccessible;

/**
 * @author bofa1ex
 * @since 2023/8/14
 */
class IdeaProxyLambdaMeta implements LambdaMeta {
    private static final Field FIELD_MEMBER_NAME;
    private static final Field FIELD_MEMBER_NAME_CLAZZ;
    private static final Field FIELD_MEMBER_NAME_NAME;

    static {
        try {
            Class<?> classDirectMethodHandle = Class.forName("java.lang.invoke.DirectMethodHandle");
            FIELD_MEMBER_NAME = setAccessible(classDirectMethodHandle.getDeclaredField("member"));
            Class<?> classMemberName = Class.forName("java.lang.invoke.MemberName");
            FIELD_MEMBER_NAME_CLAZZ = setAccessible(classMemberName.getDeclaredField("clazz"));
            FIELD_MEMBER_NAME_NAME = setAccessible(classMemberName.getDeclaredField("name"));
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new MapleDslReflectionException(e);
        }
    }

    private final Class<?> clazz;
    private final String name;

    public IdeaProxyLambdaMeta(Proxy func) {
        InvocationHandler handler = Proxy.getInvocationHandler(func);
        try {
            Object dmh = setAccessible(handler.getClass().getDeclaredField("val$target")).get(handler);
            Object member = FIELD_MEMBER_NAME.get(dmh);
            clazz = (Class<?>) FIELD_MEMBER_NAME_CLAZZ.get(member);
            name = (String) FIELD_MEMBER_NAME_NAME.get(member);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new MapleDslReflectionException(e);
        }
    }

    @Override
    public String getImplMethodName() {
        return name;
    }

    @Override
    public Class<?> getInstantiatedClass() {
        return clazz;
    }

    @Override
    public String toString() {
        return clazz.getSimpleName() + "::" + name;
    }

}
