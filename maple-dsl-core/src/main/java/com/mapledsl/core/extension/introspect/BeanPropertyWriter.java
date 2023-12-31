package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.exception.MapleDslBindingException;
import com.mapledsl.core.exception.MapleDslException;
import com.mapledsl.core.extension.func.*;

import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.function.*;

import static java.lang.invoke.MethodType.methodType;

/**
 * The BeanPropertyWriter class is responsible for writing property values to a Java Bean object using a setter method.
 * It utilizes LambdaMetafactory to create function interfaces for different types of properties, such as int, long, boolean, short, float, and double.
 * The writer utilizes the delegate field to invoke the appropriate function interface for the property type.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
class BeanPropertyWriter {
    BiConsumer<Object, Object> delegate;

    BeanPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
        final Class<?> returnType = setterMethod.getReturnType();
        final Class<?>[] parameterTypes = setterMethod.getParameterTypes();

        if (parameterTypes.length > 1) throw new MapleDslBindingException("Illegal parameter size for setter " + setterMethod);
        final Class<?> parameterType = parameterTypes[0];

        if (Object.class.isAssignableFrom(returnType)) {
            if (!parameterType.isPrimitive()) {
                this.delegate = new BeanUnaryPropertyWriter(lookup, setterMethod);
                return;
            }
            if (parameterType == Integer.TYPE) {
                this.delegate = new BeanUnaryIntPropertyWriter(lookup, setterMethod);
            } else if (parameterType == Long.TYPE) {
                this.delegate = new BeanUnaryLongPropertyWriter(lookup, setterMethod);
            } else if (parameterType == Boolean.TYPE) {
                this.delegate = new BeanUnaryBooleanPropertyWriter(lookup, setterMethod);
            } else if (parameterType == Short.TYPE) {
                this.delegate = new BeanUnaryShortPropertyWriter(lookup, setterMethod);
            } else if (parameterType == Float.TYPE) {
                this.delegate = new BeanUnaryFloatPropertyWriter(lookup, setterMethod);
            } else if (parameterType == Double.TYPE) {
                this.delegate = new BeanUnaryDoublePropertyWriter(lookup, setterMethod);
            } else if (parameterType == Character.class) {
                this.delegate = new BeanUnaryCharPropertyWriter(lookup, setterMethod);
            } else {
                MapleDslException.throwEX("Unsupported type: " + parameterType);
            }
        }

        if (void.class.isAssignableFrom(returnType)) {
            if (!parameterType.isPrimitive()) {
                this.delegate = new BeanObjPropertyWriter(lookup, setterMethod);
                return;
            }

            if (parameterType == Integer.TYPE) {
                this.delegate = new BeanObjIntPropertyWriter(lookup, setterMethod);
            } else if (parameterType == Long.TYPE) {
                this.delegate = new BeanObjLongPropertyWriter(lookup, setterMethod);
            } else if (parameterType == Boolean.TYPE) {
                this.delegate = new BeanObjBooleanPropertyWriter(lookup, setterMethod);
            } else if (parameterType == Short.TYPE) {
                this.delegate = new BeanObjShortPropertyWriter(lookup, setterMethod);
            } else if (parameterType == Float.TYPE) {
                this.delegate = new BeanObjFloatPropertyWriter(lookup, setterMethod);
            } else if (parameterType == Double.TYPE) {
                this.delegate = new BeanObjDoublePropertyWriter(lookup, setterMethod);
            } else if (parameterType == Character.class) {
                this.delegate = new BeanObjCharPropertyWriter(lookup, setterMethod);
            } else {
                MapleDslException.throwEX("Unsupported type: " + parameterType);
            }

            MapleDslException.throwEX("Unsupported type: " + returnType);
        }
    }
    
    static class BeanObjPropertyWriter implements BiConsumer {
        final BiConsumer<Object, Object> consumer;

        BeanObjPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "accept",
                        methodType(BiConsumer.class), methodType(void.class, Object.class, Object.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.consumer = (BiConsumer) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }
        
        @Override
        public void accept(Object target, Object propertyValue) {
            consumer.accept(target, propertyValue);
        }
    }
    

    static class BeanObjIntPropertyWriter implements BiConsumer {
        final ObjIntConsumer<Object> consumer;

        BeanObjIntPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "accept",
                        methodType(ObjIntConsumer.class), methodType(void.class, Object.class, int.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.consumer = (ObjIntConsumer) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            consumer.accept(target, propertyValue == null ? 0 : (int) propertyValue);
        }
    }

    static class BeanObjShortPropertyWriter implements BiConsumer {
        final ObjShortConsumer<Object> consumer;

        BeanObjShortPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "accept",
                        methodType(ObjShortConsumer.class), methodType(void.class, Object.class, short.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.consumer = (ObjShortConsumer) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            consumer.accept(target, propertyValue == null ? 0 : (short) propertyValue);
        }
    }

    static class BeanObjFloatPropertyWriter implements BiConsumer {
        final ObjFloatConsumer<Object> consumer;

        BeanObjFloatPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "accept",
                        methodType(ObjFloatConsumer.class), methodType(void.class, Object.class, float.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.consumer = (ObjFloatConsumer) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            consumer.accept(target, propertyValue == null ? 0f : (float) propertyValue);
        }
    }

    static class BeanObjDoublePropertyWriter implements BiConsumer {
        final ObjDoubleConsumer<Object> consumer;

        BeanObjDoublePropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "accept",
                        methodType(ObjDoubleConsumer.class), methodType(void.class, Object.class, double.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.consumer = (ObjDoubleConsumer) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            consumer.accept(target, propertyValue == null ? 0.0d : (double) propertyValue);
        }
    }


    static class BeanObjCharPropertyWriter implements BiConsumer {
        final ObjCharConsumer<Object> consumer;

        BeanObjCharPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "accept",
                        methodType(ObjCharConsumer.class), methodType(void.class, Object.class, char.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.consumer = (ObjCharConsumer) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            consumer.accept(target, propertyValue == null ? '\0' : (char) propertyValue);
        }
    }

    static class BeanObjLongPropertyWriter implements BiConsumer {
        final ObjLongConsumer<Object> consumer;

        BeanObjLongPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "accept",
                        methodType(ObjLongConsumer.class), methodType(void.class, Object.class, long.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.consumer = (ObjLongConsumer) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            consumer.accept(target, propertyValue == null ? 0L : (long) propertyValue);
        }
    }

    static class BeanObjBooleanPropertyWriter implements BiConsumer {
        final ObjBooleanConsumer consumer;

        BeanObjBooleanPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "accept",
                        methodType(ObjBooleanConsumer.class), methodType(void.class, Object.class, boolean.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.consumer = (ObjBooleanConsumer) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            consumer.accept(target, propertyValue != null && (boolean) propertyValue);
        }
    }

    static class BeanUnaryPropertyWriter<T> implements BiConsumer {
        final BiFunction<Object, Object, Object> function;

        BeanUnaryPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "apply",
                        methodType(BiFunction.class), methodType(Object.class, Object.class, Object.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (BiFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object purePropertyValue) {
            function.apply(target, purePropertyValue);
        }
    }

    static class BeanUnaryIntPropertyWriter implements BiConsumer {
        final ObjIntBiFunction function;

        BeanUnaryIntPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "apply",
                        methodType(ObjIntBiFunction.class), methodType(Object.class, Object.class, int.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ObjIntBiFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            function.apply(target, propertyValue == null ? 0 : (int) propertyValue);
        }
    }

    static class BeanUnaryShortPropertyWriter implements BiConsumer {
        final ObjShortBiFunction function;

        BeanUnaryShortPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "apply",
                        methodType(ObjIntBiFunction.class), methodType(Object.class, Object.class, short.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ObjShortBiFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            function.apply(target, propertyValue == null ? 0 : (short) propertyValue);
        }
    }

    static class BeanUnaryFloatPropertyWriter implements BiConsumer {
        final ObjFloatBiFunction function;

        BeanUnaryFloatPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "apply",
                        methodType(ObjIntBiFunction.class), methodType(Object.class, Object.class, float.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ObjFloatBiFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            function.apply(target, propertyValue == null ? 0f : (float) propertyValue);
        }
    }

    static class BeanUnaryDoublePropertyWriter implements BiConsumer {
        final ObjDoubleBiFunction function;

        BeanUnaryDoublePropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "apply",
                        methodType(ObjIntBiFunction.class), methodType(Object.class, Object.class, double.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ObjDoubleBiFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            function.apply(target, propertyValue == null ? 0.0d : (double) propertyValue);
        }
    }

    static class BeanUnaryCharPropertyWriter implements BiConsumer {
        final ObjCharBiFunction function;

        BeanUnaryCharPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "apply",
                        methodType(ObjIntBiFunction.class), methodType(Object.class, Object.class, char.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ObjCharBiFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            function.apply(target, propertyValue == null ? '\0': (char) propertyValue);
        }
    }

    static class BeanUnaryLongPropertyWriter implements BiConsumer {
        final ObjLongBiFunction function;

        BeanUnaryLongPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "apply",
                        methodType(ObjLongBiFunction.class), methodType(Object.class, Object.class, long.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ObjLongBiFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            function.apply(target, propertyValue == null ? 0L: ((long) propertyValue));
        }
    }


    static class BeanUnaryBooleanPropertyWriter implements BiConsumer {
        final ObjBooleanBiFunction function;

        BeanUnaryBooleanPropertyWriter(MethodHandles.Lookup lookup, Method setterMethod) {
            try {
                MethodHandle methodHandle = lookup.unreflect(setterMethod);
                Object functionInterface = LambdaMetafactory.metafactory(lookup, "apply",
                        methodType(ObjBooleanBiFunction.class), methodType(Object.class, Object.class, boolean.class),
                        methodHandle, methodHandle.type()
                ).getTarget().invoke();
                this.function = (ObjBooleanBiFunction) functionInterface;
            } catch (IllegalAccessException e) {
                throw new MapleDslBindingException("Illegal access for setter method: " + setterMethod, e);
            } catch (LambdaConversionException e) {
                throw new MapleDslBindingException("Lambda conversation for setter method: " + setterMethod, e);
            } catch (Throwable e) {
                throw new MapleDslBindingException("Initialization for setter method: " + setterMethod, e);
            }
        }

        @Override
        public void accept(Object target, Object propertyValue) {
            function.apply(target, propertyValue != null && (boolean) propertyValue);
        }
    }
}
