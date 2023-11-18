package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.MapleDslDialectRenderHelper;
import com.mapledsl.core.model.ID;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.*;
import java.util.*;

enum DefaultMapleDslParameterHandlers implements MapleDslParameterHandler {
    NULL(void.class, MapleDslParameterHandler.identity()) {
        @Override
        public String apply(@Nullable Object parameter, MapleDslConfiguration ctx) {
            return "NULL";
        }
    },
    _ID(ID.class, (it, configuration) -> {
        final ID id = (ID) it;
        if (id.getNumberValue() != null) return MapleDslDialectRenderHelper.numeric(id.getNumberValue());
        return MapleDslDialectRenderHelper.escaped(id.getTextValue());
    }),
    Boolean(boolean.class, MapleDslParameterHandler.identity()),
    BOOLEAN(Boolean.class, MapleDslParameterHandler.identity()),
    Char(char.class, MapleDslParameterHandler.identity()),
    CHAR(Character.class, MapleDslParameterHandler.identity()),
    Byte(byte.class, MapleDslParameterHandler.identity()),
    BYTE(Byte.class, MapleDslParameterHandler.identity()),
    Short(short.class, MapleDslParameterHandler.identity()),
    SHORT(Short.class, MapleDslParameterHandler.identity()),
    Int(int.class, MapleDslParameterHandler.identity()),
    INT(Integer.class, MapleDslParameterHandler.identity()),
    Long(long.class, MapleDslParameterHandler.identity()),
    LONG(Long.class, MapleDslParameterHandler.identity()),
    Float(float.class, MapleDslParameterHandler.identity()),
    FLOAT(Float.class, MapleDslParameterHandler.identity()),
    Double(double.class, MapleDslParameterHandler.identity()),
    DOUBLE(Double.class, MapleDslParameterHandler.identity()),
    Class(Class.class, MapleDslParameterHandler.escaped()) {
        @Override
        public Object compose(@NotNull Object parameter, MapleDslConfiguration ctx) {
            Class<?> it = (Class<?>) parameter;
            final String label = ctx.label(it.asSubclass(Model.class));
            return label == null ? it.getSimpleName().toLowerCase(ctx.globalLocale()) : label;
        }
    },
    STRING(String.class, MapleDslParameterHandler.escaped()) {
        @Override
        public Object compose(@NotNull Object parameter, MapleDslConfiguration ctx) {
            String it = (String) parameter;
            it = it.replaceAll("\\\\", "\\\\\\\\");
            return it;
        }
    },
    DATE(java.util.Date.class, MapleDslParameterHandler.identity()) {
        @Override
        public Object compose(@NotNull Object parameter, MapleDslConfiguration ctx) {
            return ((java.util.Date) parameter).toInstant().toEpochMilli();
        }
    },
    SQL_DATE(java.sql.Date.class, MapleDslParameterHandler.identity()) {
        @Override
        public Object compose(@NotNull Object parameter, MapleDslConfiguration ctx) {
            return ((java.sql.Date) parameter).toInstant().toEpochMilli();
        }
    },
    LOCAL_DATE_TIME(LocalDateTime.class, MapleDslParameterHandler.identity()) {
        @Override
        public Object compose(@NotNull Object parameter, MapleDslConfiguration ctx) {
            return ((LocalDateTime) parameter)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
    },
    LOCAL_DATE(LocalDate.class, MapleDslParameterHandler.identity()) {
        @Override
        public Object compose(@NotNull Object parameter, MapleDslConfiguration ctx) {
            return ((LocalDate) parameter)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
    },
    LOCAL_TIME(LocalTime.class, MapleDslParameterHandler.identity()) {
        @Override
        public Object compose(@NotNull Object parameter, MapleDslConfiguration ctx) {
            return ((LocalTime) parameter)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
    },
    OFFSET_DATE_TIME(OffsetDateTime.class, MapleDslParameterHandler.identity()) {
        @Override
        public Object compose(@NotNull Object parameter, MapleDslConfiguration ctx) {
            return ((OffsetDateTime) parameter)
                    .toInstant()
                    .toEpochMilli();
        }
    },
    OFFSET_TIME(OffsetTime.class, MapleDslParameterHandler.identity()) {
        @Override
        public Object compose(@NotNull Object parameter, MapleDslConfiguration ctx) {
            return ((OffsetTime) parameter)
                    .atDate(LocalDate.now())
                    .toInstant()
                    .toEpochMilli();
        }
    },
    ZONED_DATE_TIME(ZonedDateTime.class, MapleDslParameterHandler.identity()) {
        @Override
        public Object compose(@NotNull Object parameter, MapleDslConfiguration ctx) {
            return ((ZonedDateTime) parameter)
                    .toInstant()
                    .toEpochMilli();
        }
    },
    LIST(List.class, MapleDslParameterHandler.identity()) {
        @Override
        public String apply(@Nullable Object parameter, MapleDslConfiguration ctx) {
            if (parameter == null) return "[]";
            final StringJoiner joiner = new StringJoiner(",", "[", "]");
            for (Object it : (List<?>) parameter) {
                MapleDslParameterHandler parameterHandler = ctx.parameterHandler(it.getClass());
                joiner.add(parameterHandler.apply(it, ctx));
            }

            return delegate.apply(joiner, ctx);
        }
    },
    SET(Set.class, MapleDslParameterHandler.identity()) {
        @Override
        public String apply(@Nullable Object parameter, MapleDslConfiguration ctx) {
            if (parameter == null) return "[]";
            final StringJoiner joiner = new StringJoiner(",", "[", "]");
            for (Object it : (Set<?>) parameter) {
                MapleDslParameterHandler parameterHandler = ctx.parameterHandler(it.getClass());
                joiner.add(parameterHandler.apply(it, ctx));
            }

            return delegate.apply(joiner, ctx);
        }
    },
    MAP(Map.class, MapleDslParameterHandler.identity()) {
        @Override
        public String apply(@Nullable Object parameter, MapleDslConfiguration ctx) {
            if (parameter == null) return NULL.apply(parameter, ctx);
            final StringJoiner joiner = new StringJoiner(",", "{", "}");
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) parameter).entrySet()) {
                final Object key = entry.getKey();
                final Object value = entry.getValue();
                if (key == null) continue;

                final MapleDslParameterHandler parameterHandler = ctx.parameterHandler(value.getClass());
                joiner.add(key + ":" + parameterHandler.apply(value, ctx));
            }

            return delegate.apply(joiner, ctx);
        }
    };

    final Class<?> parameterType;
    final MapleDslParameterHandler delegate;

    <R> DefaultMapleDslParameterHandlers(Class<R> parameterType, MapleDslParameterHandler delegate) {
        this.parameterType = parameterType;
        this.delegate = delegate;
    }

    @Override
    public String apply(@Nullable Object parameter, MapleDslConfiguration ctx) {
        if (parameter == null) return NULL.apply(parameter, ctx);
        if (parameter instanceof List) return LIST.apply(parameter, ctx);
        if (parameter instanceof Set) return SET.apply(parameter, ctx);
        if (parameter instanceof Collection) return SET.apply(new LinkedHashSet<>(((Collection<?>) parameter)), ctx);
        if (parameter instanceof Map) return MAP.apply(parameter, ctx);

        final Object composed = compose(parameter, ctx);
        return this.delegate.apply(composed, ctx);
    }
}
