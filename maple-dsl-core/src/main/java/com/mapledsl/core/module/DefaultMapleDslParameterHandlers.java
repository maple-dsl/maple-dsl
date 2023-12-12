package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.MapleDslDialectRenderHelper;
import com.mapledsl.core.model.Model;
import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.util.*;

import static com.mapledsl.core.MapleDslDialectRenderHelper.quote;

enum DefaultMapleDslParameterHandlers implements MapleDslDialectRenderHelper {
    @SuppressWarnings({"rawtypes", "unchecked"})
    CLASS(new MapleDslParameterHandler<Class>() {
        @Override
        public Class<Class> parameterType() {
            return Class.class;
        }

        @Override
        public @NotNull String apply(Class param, MapleDslConfiguration context) {
            return quote(Model.class.isAssignableFrom(param) ?
                    context.label(((Class<? extends Model>) param)) :
                    param.getSimpleName().toLowerCase(context.globalLocale()));
        }
    }),
    STRING(new MapleDslParameterHandler<String>() {
        @Override
        public Class<String> parameterType() {
            return String.class;
        }

        @Override
        public @NotNull String apply(String value, MapleDslConfiguration ctx) {
            return quote(value);
        }
    }),
    _BOOLEAN(MapleDslParameterHandler.identify(boolean.class)),
    BOOLEAN(MapleDslParameterHandler.identify(Boolean.class)),
    _CHAR(MapleDslParameterHandler.identify(char.class)),
    CHAR(MapleDslParameterHandler.identify(Character.class)),
    _BYTE(MapleDslParameterHandler.identify(byte.class)),
    BYTE(MapleDslParameterHandler.identify(Byte.class)),
    _SHORT(MapleDslParameterHandler.identify(short.class)),
    SHORT(MapleDslParameterHandler.identify(Short.class)),
    _INT(MapleDslParameterHandler.identify(int.class)),
    INT(MapleDslParameterHandler.identify(Integer.class)),
    _LONG(MapleDslParameterHandler.identify(long.class)),
    LONG(MapleDslParameterHandler.identify(Long.class)),
    _FLOAT(MapleDslParameterHandler.identify(float.class)),
    FLOAT(MapleDslParameterHandler.identify(Float.class)),
    _DOUBLE(MapleDslParameterHandler.identify(double.class)),
    DOUBLE(MapleDslParameterHandler.identify(Double.class)),
    DATE(new MapleDslParameterHandler<Date>() {
        @Override
        public Class<Date> parameterType() {
            return java.util.Date.class;
        }

        @Override
        public @NotNull String apply(Date date, MapleDslConfiguration ctx) {
            return date.toInstant().toEpochMilli() + "";
        }
    }),
    SQL_DATE(new MapleDslParameterHandler<java.sql.Date>() {
        @Override
        public Class<java.sql.Date> parameterType() {
            return java.sql.Date.class;
        }

        @Override
        public @NotNull String apply(java.sql.Date date, MapleDslConfiguration ctx) {
            return date.toInstant().toEpochMilli()  + "";
        }
    }),
    LOCAL_DATE_TIME(new MapleDslParameterHandler<LocalDateTime>() {
        @Override
        public Class<LocalDateTime> parameterType() {
            return LocalDateTime.class;
        }

        @Override
        public @NotNull String apply(LocalDateTime localDateTime, MapleDslConfiguration ctx) {
            return localDateTime
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()  + "";
        }
    }),
    LOCAL_DATE(new MapleDslParameterHandler<LocalDate>() {
        @Override
        public Class<LocalDate> parameterType() {
            return LocalDate.class;
        }

        @Override
        public @NotNull String apply(LocalDate localDate, MapleDslConfiguration ctx) {
            return localDate
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli() + "";
        }
    }),
    LOCAL_TIME(new MapleDslParameterHandler<LocalTime>() {
        @Override
        public Class<LocalTime> parameterType() {
            return LocalTime.class;
        }

        @Override
        public @NotNull String apply(LocalTime localTime, MapleDslConfiguration ctx) {
            return localTime
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli() + "";
        }
    }),
    OFFSET_DATE_TIME(new MapleDslParameterHandler<OffsetDateTime>() {
        @Override
        public Class<OffsetDateTime> parameterType() {
            return OffsetDateTime.class;
        }

        @Override
        public @NotNull String apply(OffsetDateTime offsetDateTime, MapleDslConfiguration mapleDslConfiguration) {
            return offsetDateTime
                    .toInstant()
                    .toEpochMilli() + "";
        }
    }),
    OFFSET_TIME(new MapleDslParameterHandler<OffsetTime>() {
        @Override
        public Class<OffsetTime> parameterType() {
            return OffsetTime.class;
        }

        @Override
        public @NotNull String apply(OffsetTime offsetTime, MapleDslConfiguration mapleDslConfiguration) {
            return offsetTime
                    .atDate(LocalDate.now())
                    .toInstant()
                    .toEpochMilli() + "";
        }
    }),
    ZONED_DATE_TIME(new MapleDslParameterHandler<ZonedDateTime>() {
        @Override
        public Class<ZonedDateTime> parameterType() {
            return ZonedDateTime.class;
        }

        @Override
        public @NotNull String apply(ZonedDateTime zonedDateTime, MapleDslConfiguration mapleDslConfiguration) {
            return zonedDateTime
                    .toInstant()
                    .toEpochMilli() + "";
        }
    }),
    @SuppressWarnings("rawtypes")
    LIST(new MapleDslParameterHandler<List>() {
        @Override
        public Class<List> parameterType() {
            return List.class;
        }

        @Override
        public @NotNull String apply(List param, MapleDslConfiguration context) {
            if (param == null) return "[]";
            final StringJoiner joiner = new StringJoiner(",", "[", "]");
            for (Object it : (List<?>) param) {
                joiner.add(context.parameterized(it));
            }

            return joiner.toString();
        }
    }),
    @SuppressWarnings("rawtypes")
    SET(new MapleDslParameterHandler<Set>() {
        @Override
        public Class<Set> parameterType() {
            return Set.class;
        }

        @Override
        public @NotNull String apply(Set param, MapleDslConfiguration context) {
            if (param == null) return "[]";
            final StringJoiner joiner = new StringJoiner(",", "[", "]");
            for (Object it : (Set<?>) param) {
                joiner.add(context.parameterized(it));
            }

            return joiner.toString();
        }
    }),
    @SuppressWarnings("rawtypes")
    MAP(new MapleDslParameterHandler<Map>() {
        @Override
        public Class<Map> parameterType() {
            return Map.class;
        }

        @Override
        public @NotNull String apply(Map param, MapleDslConfiguration context) {
            if (param == null) return "{}";
            final StringJoiner joiner = new StringJoiner(",", "{", "}");
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) param).entrySet()) {
                final Object key = entry.getKey();
                final Object value = entry.getValue();
                if (key == null) continue;

                joiner.add("\"" + key + "\"" + ":" + quoteEscaper.escape(context.parameterized(value)));
            }

            return joiner.toString();
        }
    });

    final MapleDslParameterHandler<?> parameterHandler;
    <R> DefaultMapleDslParameterHandlers(MapleDslParameterHandler<R> parameterHandler) {
        this.parameterHandler = parameterHandler;
    }

    MapleDslParameterHandler<?> parameterHandler() {
        return parameterHandler;
    }
}
