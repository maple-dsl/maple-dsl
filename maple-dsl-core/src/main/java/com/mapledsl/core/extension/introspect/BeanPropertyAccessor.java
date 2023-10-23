package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.extension.KeyPolicyStrategies;
import com.mapledsl.core.extension.KeyPolicyStrategy;
import com.mapledsl.core.extension.func.ToBooleanFunction;
import com.mapledsl.core.extension.func.ToCharFunction;
import com.mapledsl.core.extension.func.ToFloatFunction;
import com.mapledsl.core.extension.func.ToShortFunction;
import com.mapledsl.core.module.MapleDslParameterHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author bofa1ex
 * @since 2022/08/02
 */
@SuppressWarnings({"unchecked", "rawtypes"})
class BeanPropertyAccessor {
    protected final MapleDslConfiguration context;
    protected final AtomicReference<MapleDslParameterHandler> parameterHandlerRef;
    Function<Object, Object> delegate;

    BeanPropertyAccessor(MapleDslConfiguration context, AtomicReference<MapleDslParameterHandler> parameterHandlerRef) {
        this.context = context;
        this.parameterHandlerRef = parameterHandlerRef;
    }

    BeanPropertyAccessor(MapleDslConfiguration context, MethodHandles.Lookup lookup, Class propertyType, Method getterMethod) {
        this(context, new AtomicReference<>(context.parameterHandler(propertyType)));

        final Class<?> returnType = getterMethod.getReturnType();
        if (!returnType.isPrimitive()) {
            this.delegate = new BeanObjPropertyAccessor(lookup, getterMethod);
            return;
        }

        if (returnType == Integer.TYPE) {
            this.delegate = new BeanToIntPropertyAccessor(lookup, getterMethod);
        } else if (returnType == Long.TYPE) {
            this.delegate = new BeanToLongPropertyAccessor(lookup, getterMethod);
        } else if (returnType == Boolean.TYPE) {
            this.delegate = new BeanToBooleanPropertyAccessor(lookup, getterMethod);
        } else if (returnType == Short.TYPE) {
            this.delegate = new BeanToShortPropertyAccessor(lookup, getterMethod);
        } else if (returnType == Float.TYPE) {
            this.delegate = new BeanToFloatPropertyAccessor(lookup, getterMethod);
        } else if (returnType == Double.TYPE) {
            this.delegate = new BeanToDoublePropertyAccessor(lookup, getterMethod);
        } else if (returnType == Character.TYPE) {
            this.delegate = new BeanToCharPropertyAccessor(lookup, getterMethod);
        } else {
            MapleDslException.throwEX("Unsupported type: " + returnType);
        }
    }

    final Object getter(Object bean) {
        if (bean == null) return null;
        return delegate.apply(bean);
    }

    /**
     * Thread safe for parameter handler complete in runtime.
     *
     * @param value before parameterized value.
     * @return after parameterized value.
     */
    final String parameterized(Object value) {
        // if bean property type is not sure in compile stage,
        // it will complete in runtime automatically.
        @NotNull final MapleDslParameterHandler parameterHandler = parameterHandlerRef.updateAndGet(it -> {
            if (it == null) it = context.parameterHandler(value.getClass());
            if (it == null) it = context.nullParameterHandler();
            return it;
        });

        return parameterHandler.apply(value, context);
    }

    void override(KeyPolicyStrategy keyPolicyStrategy) {
        if (keyPolicyStrategy == KeyPolicyStrategies.MANUAL) return;
        this.delegate = delegate.andThen(keyPolicyStrategy::generate);
    }

    static class BeanObjPropertyAccessor implements UnaryOperator<Object> {
        final Function<Object, Object> function;

        BeanObjPropertyAccessor(MethodHandles.Lookup lookup, Method getterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(getterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "apply",
                        methodType(Function.class), methodType(Object.class, Object.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (Function) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for getter method: " + getterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversion for getter method: " + getterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for getter method: " + getterMethod, e);
            }
        }

        @Override
        public Object apply(Object target) {
            return function.apply(target);
        }
    }

    static class BeanToIntPropertyAccessor implements UnaryOperator<Object> {
        final ToIntFunction<Object> function;

        BeanToIntPropertyAccessor(MethodHandles.Lookup lookup, Method getterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(getterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "applyAsInt",
                        methodType(ToIntFunction.class), methodType(int.class, Object.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ToIntFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for getter method: " + getterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversion for getter method: " + getterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for getter method: " + getterMethod, e);
            }
        }

        @Override
        public Object apply(Object target) {
            return function.applyAsInt(target);
        }
    }

    static class BeanToShortPropertyAccessor implements UnaryOperator<Object> {
        final ToShortFunction function;

        BeanToShortPropertyAccessor(MethodHandles.Lookup lookup, Method getterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(getterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "applyAsInt",
                        methodType(ToShortFunction.class), methodType(short.class, Object.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ToShortFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for getter method: " + getterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversion for getter method: " + getterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for getter method: " + getterMethod, e);
            }
        }

        @Override
        public Object apply(Object target) {
            return function.applyAsShort(target);
        }
    }

    static class BeanToFloatPropertyAccessor implements UnaryOperator<Object> {
        final ToFloatFunction function;

        BeanToFloatPropertyAccessor(MethodHandles.Lookup lookup, Method getterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(getterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "applyAsInt",
                        methodType(ToFloatFunction.class), methodType(float.class, Object.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ToFloatFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for getter method: " + getterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversion for getter method: " + getterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for getter method: " + getterMethod, e);
            }
        }

        @Override
        public Object apply(Object target) {
            return function.applyAsFloat(target);
        }
    }

    static class BeanToDoublePropertyAccessor implements UnaryOperator<Object> {
        final ToDoubleFunction function;

        BeanToDoublePropertyAccessor(MethodHandles.Lookup lookup, Method getterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(getterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "applyAsInt",
                        methodType(ToDoubleFunction.class), methodType(double.class, Object.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ToDoubleFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for getter method: " + getterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversion for getter method: " + getterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for getter method: " + getterMethod, e);
            }
        }

        @Override
        public Object apply(Object target) {
            return function.applyAsDouble(target);
        }
    }


    static class BeanToCharPropertyAccessor implements UnaryOperator<Object> {
        final ToCharFunction function;

        BeanToCharPropertyAccessor(MethodHandles.Lookup lookup, Method getterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(getterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "applyAsInt",
                        methodType(ToCharFunction.class), methodType(char.class, Object.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ToCharFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for getter method: " + getterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversion for getter method: " + getterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for getter method: " + getterMethod, e);
            }
        }

        @Override
        public Object apply(Object target) {
            return function.applyAsChar(target);
        }
    }

    static class BeanToLongPropertyAccessor implements UnaryOperator<Object> {
        final ToLongFunction<Object> function;

        BeanToLongPropertyAccessor(MethodHandles.Lookup lookup, Method getterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(getterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "applyAsLong",
                        methodType(ToLongFunction.class), methodType(long.class, Object.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ToLongFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for getter method: " + getterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversion for getter method: " + getterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for getter method: " + getterMethod, e);
            }
        }

        @Override
        public Object apply(Object target) {
            return function.applyAsLong(target);
        }
    }

    static class BeanToBooleanPropertyAccessor implements UnaryOperator<Object> {
        final ToBooleanFunction function;

        BeanToBooleanPropertyAccessor(MethodHandles.Lookup lookup, Method getterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(getterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "applyAsBoolean",
                        methodType(ToBooleanFunction.class), methodType(boolean.class, Object.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ToBooleanFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for getter method: " + getterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversion for getter method: " + getterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for getter method: " + getterMethod, e);
            }
        }

        @Override
        public Object apply(Object target) {
            return function.applyAsBoolean(target);
        }
    }
}
