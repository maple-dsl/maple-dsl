package com.mapledsl.core.extension.lambda;

import com.mapledsl.core.exception.MapleDslReflectionException;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;

/**
 * The ReflectLambdaMeta class is an implementation of the LambdaMeta interface.
 * It is used to extract metadata about a lambda function using reflection.
 */
class ReflectLambdaMeta implements LambdaMeta {
    private static final Field FIELD_CAPTURING_CLASS;

    static {
        Field fieldCapturingClass;
        try {
            Class<SerializedLambda> aClass = SerializedLambda.class;
            fieldCapturingClass = LambdaMeta.setAccessible(aClass.getDeclaredField("capturingClass"));
        } catch (Throwable e) {
            fieldCapturingClass = null;
        }
        FIELD_CAPTURING_CLASS = fieldCapturingClass;
    }

    private final SerializedLambda lambda;

    public ReflectLambdaMeta(SerializedLambda lambda) {
        this.lambda = lambda;
    }

    @Override
    public String getImplMethodName() {
        return lambda.getImplMethodName();
    }

    @Override
    public Class<?> getInstantiatedClass() {
        String instantiatedMethodType = lambda.getInstantiatedMethodType();
        String instantiatedType = instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(";")).replace("/", ".");
        return toClassConfident(instantiatedType, getCapturingClassClassLoader());
    }

    private ClassLoader getCapturingClassClassLoader() {
        if (FIELD_CAPTURING_CLASS == null) {
            return null;
        }
        try {
            return ((Class<?>) FIELD_CAPTURING_CLASS.get(lambda)).getClassLoader();
        } catch (IllegalAccessException e) {
            throw new MapleDslReflectionException(e);
        }
    }
}
