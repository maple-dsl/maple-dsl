package com.mapledsl.core.extension.lambda;

/**
 * The ShadowLambdaMeta class is an implementation of the LambdaMeta interface that represents metadata about a lambda function.
 * It provides methods to retrieve the implementation method name and the instantiated class for the lambda function.
 */
class ShadowLambdaMeta implements LambdaMeta {
    private final SerializedLambda lambda;

    public ShadowLambdaMeta(SerializedLambda lambda) {
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
        return toClassConfident(instantiatedType, lambda.getCapturingClass().getClassLoader());
    }
}

