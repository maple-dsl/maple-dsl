package com.mapledsl.core.extension.lambda;

import com.mapledsl.core.exception.MapleDslSerializedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;

/**
 * Mirror of {@link java.lang.invoke.SerializedLambda }
 * @author bofa1ex
 * @since 2023/8/14
 */
@SuppressWarnings("unused")
class SerializedLambda implements Serializable {
    private static final long serialVersionUID = 8025925345765570181L;

    private Class<?> capturingClass;
    private String functionalInterfaceClass;
    private String functionalInterfaceMethodName;
    private String functionalInterfaceMethodSignature;
    private String implClass;
    private String implMethodName;
    private String implMethodSignature;
    private int implMethodKind;
    private String instantiatedMethodType;
    private Object[] capturedArgs;

    public static SerializedLambda extract(Serializable serializable) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(serializable);
            oos.flush();
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    Class<?> clazz = super.resolveClass(desc);
                    return clazz == java.lang.invoke.SerializedLambda.class ? SerializedLambda.class : clazz;
                }

            }) {
                return (SerializedLambda) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new MapleDslSerializedException("Function class must be synthetic");
        }
    }

    public String getInstantiatedMethodType() {
        return instantiatedMethodType;
    }

    public Class<?> getCapturingClass() {
        return capturingClass;
    }

    public String getImplMethodName() {
        return implMethodName;
    }

}
