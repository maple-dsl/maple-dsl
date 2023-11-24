package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.MapleDslDialectRenderHelper;
import com.mapledsl.core.model.ID;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.*;
import java.util.*;
import java.util.function.Function;

enum DefaultMapleDslParameterHandlers implements MapleDslParameterHandler {
    NULL(void.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public String apply(@Nullable Object parameter, MapleDslConfiguration ctx) {
            return "NULL";
        }
    },
    _ID(ID.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (parameter instanceof ID) {
                final ID id = (ID) parameter;
                if (id.getNumberValue() != null) return MapleDslDialectRenderHelper.numeric(id.getNumberValue());
                return MapleDslDialectRenderHelper.escaped(id.getTextValue());
            }
            if (parameter instanceof String) {
                return MapleDslDialectRenderHelper.escaped(parameter);
            }
            if (parameter instanceof Number) {
                return MapleDslDialectRenderHelper.identify(parameter);
            }
            return NULL.apply(parameter, ctx);
        }
    },
    Boolean(boolean.class, MapleDslDialectRenderHelper::identify),
    BOOLEAN(Boolean.class, MapleDslDialectRenderHelper::identify),
    Char(char.class, MapleDslDialectRenderHelper::identify),
    CHAR(Character.class, MapleDslDialectRenderHelper::identify),
    Byte(byte.class, MapleDslDialectRenderHelper::identify),
    BYTE(Byte.class, MapleDslDialectRenderHelper::identify),
    Short(short.class, MapleDslDialectRenderHelper::identify),
    SHORT(Short.class, MapleDslDialectRenderHelper::identify),
    Int(int.class, MapleDslDialectRenderHelper::identify),
    INT(Integer.class, MapleDslDialectRenderHelper::identify),
    Long(long.class, MapleDslDialectRenderHelper::identify),
    LONG(Long.class, MapleDslDialectRenderHelper::identify),
    Float(float.class, MapleDslDialectRenderHelper::identify),
    FLOAT(Float.class, MapleDslDialectRenderHelper::identify),
    Double(double.class, MapleDslDialectRenderHelper::identify),
    DOUBLE(Double.class, MapleDslDialectRenderHelper::identify),
    Class(Class.class, MapleDslDialectRenderHelper::escaped) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (!(parameter instanceof Class)) throw new IllegalArgumentException(String.format("parameter:%s, cast as CLASS error.", parameter));
            Class<?> it = (Class<?>) parameter;
            final String label = ctx.label(it.asSubclass(Model.class));
            return label == null ? it.getSimpleName().toLowerCase(ctx.globalLocale()) : label;
        }
    },
    STRING(String.class, MapleDslDialectRenderHelper::escaped) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (!(parameter instanceof String)) throw new IllegalArgumentException(String.format("parameter:%s, cast as STRING error.", parameter));
            String it = (String) parameter;
            it = it.replaceAll("\\\\", "\\\\\\\\");
            return it;
        }
    },
    DATE(java.util.Date.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (!(parameter instanceof java.util.Date)) throw new IllegalArgumentException(String.format("parameter:%s, cast as DATE error.", parameter));
            return ((java.util.Date) parameter).toInstant().toEpochMilli();
        }
    },
    SQL_DATE(java.sql.Date.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (!(parameter instanceof java.sql.Date)) throw new IllegalArgumentException(String.format("parameter:%s, cast as DATE error.", parameter));
            return ((java.sql.Date) parameter).toInstant().toEpochMilli();
        }
    },
    LOCAL_DATE_TIME(LocalDateTime.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (!(parameter instanceof LocalDateTime)) throw new IllegalArgumentException(String.format("parameter:%s, cast as LOCAL_DATE_TIME error.", parameter));
            return ((LocalDateTime) parameter)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
    },
    LOCAL_DATE(LocalDate.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (!(parameter instanceof LocalDate)) throw new IllegalArgumentException(String.format("parameter:%s, cast as LOCAL_DATE error.", parameter));
            return ((LocalDate) parameter)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
    },
    LOCAL_TIME(LocalTime.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (!(parameter instanceof LocalTime)) throw new IllegalArgumentException(String.format("parameter:%s, cast as LOCAL_TIME error.", parameter));
            return ((LocalTime) parameter)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
    },
    OFFSET_DATE_TIME(OffsetDateTime.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (!(parameter instanceof OffsetDateTime)) throw new IllegalArgumentException(String.format("parameter:%s, cast as OFFSET_DATE_TIME error.", parameter));
            return ((OffsetDateTime) parameter)
                    .toInstant()
                    .toEpochMilli();
        }
    },
    OFFSET_TIME(OffsetTime.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (!(parameter instanceof OffsetTime)) throw new IllegalArgumentException(String.format("parameter:%s, cast as OFFSET_TIME error.", parameter));
            return ((OffsetTime) parameter)
                    .atDate(LocalDate.now())
                    .toInstant()
                    .toEpochMilli();
        }
    },
    ZONED_DATE_TIME(ZonedDateTime.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public Object compose(@NotNull Object parameter, @NotNull MapleDslConfiguration ctx) {
            if (!(parameter instanceof ZonedDateTime)) throw new IllegalArgumentException(String.format("parameter:%s, cast as ZONED_DATE_TIME error.", parameter));
            return ((ZonedDateTime) parameter)
                    .toInstant()
                    .toEpochMilli();
        }
    },
    LIST(List.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public String apply(@Nullable Object parameter, MapleDslConfiguration ctx) {
            if (parameter == null) return "[]";
            final StringJoiner joiner = new StringJoiner(",", "[", "]");
            for (Object it : (List<?>) parameter) {
                MapleDslParameterHandler parameterHandler = ctx.parameterHandler(it.getClass());
                joiner.add(parameterHandler.apply(it, ctx));
            }

            return suffixProcessor.apply(joiner);
        }
    },
    SET(Set.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public String apply(@Nullable Object parameter, MapleDslConfiguration ctx) {
            if (parameter == null) return "[]";
            final StringJoiner joiner = new StringJoiner(",", "[", "]");
            for (Object it : (Set<?>) parameter) {
                MapleDslParameterHandler parameterHandler = ctx.parameterHandler(it.getClass());
                joiner.add(parameterHandler.apply(it, ctx));
            }

            return suffixProcessor.apply(joiner);
        }
    },
    MAP(Map.class, MapleDslDialectRenderHelper::identify) {
        @Override
        public String apply(@Nullable Object parameter, MapleDslConfiguration ctx) {
            if (parameter == null) return "{}";
            final StringJoiner joiner = new StringJoiner(",", "{", "}");
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) parameter).entrySet()) {
                final Object key = entry.getKey();
                final Object value = entry.getValue();
                if (key == null) continue;

                final MapleDslParameterHandler parameterHandler = ctx.parameterHandler(value.getClass());
                joiner.add(key + ":" + parameterHandler.apply(value, ctx));
            }

            return suffixProcessor.apply(joiner);
        }
    };

    final Class<?> parameterType;
    final Function<Object, String> suffixProcessor;

    <R> DefaultMapleDslParameterHandlers(Class<R> parameterType, Function<Object, String> suffixProcessor) {
        this.parameterType = parameterType;
        this.suffixProcessor = suffixProcessor;
    }

    @Override
    public String apply(@Nullable Object parameter, MapleDslConfiguration ctx) {
        if (parameter == null) return NULL.apply(parameter, ctx);
        if (parameter instanceof List) return LIST.apply(parameter, ctx);
        if (parameter instanceof Set) return SET.apply(parameter, ctx);
        if (parameter instanceof Collection) return SET.apply(new LinkedHashSet<>(((Collection<?>) parameter)), ctx);
        if (parameter instanceof Map) return MAP.apply(parameter, ctx);

        return this.suffixProcessor.apply(compose(parameter, ctx));
    }

    @Override
    public java.lang.Class<?> parameterType() {
        return parameterType;
    }
}
