package com.mapledsl.core.extension.introspect;

import com.mapledsl.core.exception.MapleDslBindingException;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

import static java.lang.invoke.MethodType.methodType;

/**
 * @author bofa1ex
 * @since 2022/08/03
 */
public class BeanCreator<BEAN> {
    final Supplier<BEAN> supplier;

    /**
     * init the target model clazz of creation method handle as supplier.
     * (diff call the reflection constructor)
     *
     * @throws MapleDslBindingException missing the target model default constructor(non arguments).
     *                               or default constructor modified with private access.
     *                               or method handle conversion to lambda failed.
     */
    @SuppressWarnings("unchecked")
    BeanCreator(MethodHandles.Lookup lookup, Class<BEAN> clazz) {
        try {
            MethodHandle nonArgConstructor = lookup.unreflectConstructor(
                    clazz.getDeclaredConstructor()
            );
            this.supplier = (Supplier<BEAN>) LambdaMetafactory.metafactory(
                            lookup,
                            "get",
                            methodType(Supplier.class),
                            methodType(Object.class),
                            nonArgConstructor,
                            nonArgConstructor.type())
                    .getTarget().invokeExact();
        } catch (NoSuchMethodException e) {
            throw new MapleDslBindingException("Not found default constructor for " + clazz, e);
        } catch (Throwable e) {
            throw new MapleDslBindingException("Initialization supplier failed for " + clazz);
        }
    }

    public BEAN newInstance() {
        return supplier.get();
    }
}
