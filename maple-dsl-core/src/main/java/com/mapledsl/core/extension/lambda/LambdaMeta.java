package com.mapledsl.core.extension.lambda;

import com.mapledsl.core.exception.MapleDslReflectionException;
import com.mapledsl.core.extension.func.SerializableFunction;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author bofa1ex
 * @since 2023/8/14
 */
public interface LambdaMeta {
    String getImplMethodName();

    Class<?> getInstantiatedClass();

    static <T extends AccessibleObject> T setAccessible(T object) {
        return AccessController.doPrivileged((PrivilegedAction<T>) () -> {
            object.setAccessible(true);
            return object;
        });
    }

    static LambdaMeta extract(SerializableFunction<?, ?> func) {
        // idea evaluate mode using proxy
        if (func instanceof Proxy) {
            return new IdeaProxyLambdaMeta((Proxy) func);
        }
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            return new ReflectLambdaMeta((java.lang.invoke.SerializedLambda) setAccessible(method).invoke(func));
        } catch (Throwable e) {
            return new ShadowLambdaMeta(SerializedLambda.extract(func));
        }
    }

    default Class<?> toClassConfident(String clazzName, ClassLoader classLoader) {
        try {
            return loadClass(clazzName, getClassLoaders(classLoader));
        } catch (ClassNotFoundException e) {
            throw new MapleDslReflectionException(e);
        }
    }

    default Class<?> loadClass(String clazzName, ClassLoader[] classLoaders) throws ClassNotFoundException {
        for (ClassLoader classLoader : classLoaders) {
            try {
                return Class.forName(clazzName, true, classLoader);
            } catch (ClassNotFoundException ignored) {}
        }
        throw new ClassNotFoundException("Cannot find class: " + clazzName);
    }

    default ClassLoader[] getClassLoaders(ClassLoader cur){
        return new ClassLoader[]{
                cur,
                Thread.currentThread().getContextClassLoader(),
                LambdaMeta.class.getClassLoader(),
                ClassLoader.getSystemClassLoader()
        };
    }
}
