package com.mapledsl.core.session;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.exception.MapleDslException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

public class MapleDslSessionTemplate implements MapleDslSession, MapleDslSessionFactory {
    private final MapleDslSession sessionProxy;
    private final MapleDslSessionFactory sessionFactory;

    private MapleDslSessionTemplate(MapleDslSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.sessionProxy = (MapleDslSession) Proxy.newProxyInstance(MapleDslSession.class.getClassLoader(), new Class[]{ MapleDslSession.class }, new MapleDslSessionInterceptor());
    }

    public static MapleDslSessionTemplate newInstance(MapleDslSessionFactory sessionFactory) {
        return new MapleDslSessionTemplate(sessionFactory);
    }

    @Override
    public <T> T selectOne(String stmt, Class<T> mappedEntityType) {
        return sessionProxy.selectOne(stmt, mappedEntityType);
    }

    @Override
    public <T> List<T> selectList(String stmt, Class<T> mappedEntityType) {
        return sessionProxy.selectList(stmt, mappedEntityType);
    }

    @Override
    public Map<String, Object> selectMap(String stmt) {
        return sessionProxy.selectMap(stmt);
    }

    @Override
    public List<Map<String, Object>> selectMaps(String stmt) {
        return sessionProxy.selectMaps(stmt);
    }

    @Override
    public boolean execute(String stmt) throws MapleDslException {
        return sessionProxy.execute(stmt);
    }

    @Override
    public void close() {
        sessionProxy.close();
    }

    @Override
    public MapleDslConfiguration configuration() {
        return sessionFactory.configuration();
    }

    @Override
    public MapleDslSession openSession() {
        return sessionFactory.openSession();
    }

    class MapleDslSessionInterceptor implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            try (MapleDslSession autoSession = openSession()) {
                return method.invoke(autoSession, args);
            } catch (Throwable t) {
                throw new MapleDslException(t);
            }
        }
    }
}
