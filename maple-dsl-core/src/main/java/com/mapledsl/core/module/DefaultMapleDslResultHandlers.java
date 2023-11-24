package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.Date;

enum DefaultMapleDslResultHandlers {
    BOOLEAN(new MapleDslResultHandler<Object, Boolean>() {
        @Override
        public Class<Boolean> resultType() {
            return Boolean.class;
        }

        @Override
        public Boolean apply(Object source, MapleDslConfiguration configuration) {
            if (source == null) return false;
            if (source instanceof Boolean) return (Boolean) source;
            if (source instanceof Number) return ((Number) source).intValue() == 0;
            if (source instanceof String) return Boolean.parseBoolean((String) source);
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Boolean expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    _BOOLEAN(boolean.class, BOOLEAN),

    CHAR(new MapleDslResultHandler<Object, Character>() {
        @Override
        public Character apply(Object source, MapleDslConfiguration configuration) {
            if (source == null) return null;
            if (source instanceof Boolean) return (Boolean) source ? (char) 0 : 1;
            if (source instanceof Number) return (char) ((Number) source).intValue();
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Character expected conversation: {}, it will be deprecated.", source);
            return null;
        }

        @Override
        public Class<Character> resultType() {
            return Character.class;
        }
    }),
    _CHAR(char.class, CHAR),

    BYTE(new MapleDslResultHandler<Object, Byte>() {
        @Override
        public Class<java.lang.Byte> resultType() {
            return java.lang.Byte.class;
        }

        @Override
        public java.lang.Byte apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof Boolean) return (Boolean) source ? (byte) 0 : 1;
            if (source instanceof Number) return ((Number) source).byteValue();
            if (source instanceof String) return java.lang.Byte.parseByte((String) source);
            if (source instanceof LocalDate) return (byte) ((LocalDate) source)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalTime) return (byte) ((LocalTime) source)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalDateTime) return (byte) ((LocalDateTime) source)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();

            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Byte expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    _BYTE(byte.class, BYTE),

    SHORT(new MapleDslResultHandler<Object, Short>() {
        @Override
        public Class<Short> resultType() {
            return Short.class;
        }

        @Override
        public Short apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof Boolean) return (Boolean) source ? (short) 0 : 1;
            if (source instanceof Number) return ((Number) source).shortValue();
            if (source instanceof String) return java.lang.Short.parseShort((String) source);
            if (source instanceof LocalDate) return (short) ((LocalDate) source)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalTime) return (short) ((LocalTime) source)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalDateTime) return (short) ((LocalDateTime) source)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();

            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Short expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    _SHORT(short.class, SHORT),

    INT(new MapleDslResultHandler<Object, Integer>() {
        @Override
        public Class<Integer> resultType() {
            return Integer.class;
        }

        @Override
        public Integer apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof Boolean) return (Boolean) source ? 0 : 1;
            if (source instanceof Number) return ((Number) source).intValue();
            if (source instanceof String) return java.lang.Integer.parseInt((String) source);
            if (source instanceof LocalDate) return (int) ((LocalDate) source)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalTime) return (int) ((LocalTime) source)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalDateTime) return (int) ((LocalDateTime) source)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();

            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Int expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    _INT(int.class, INT),

    LONG(new MapleDslResultHandler<Object, Long>() {
        @Override
        public Class<java.lang.Long> resultType() {
            return Long.class;
        }

        @Override
        public java.lang.Long apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof Boolean) return (Boolean) source ? 0L : 1L;
            if (source instanceof Number) return ((Number) source).longValue();
            if (source instanceof String) return java.lang.Long.parseLong((String) source);
            if (source instanceof LocalDate) return ((LocalDate) source)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalTime) return ((LocalTime) source)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalDateTime) return ((LocalDateTime) source)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();

            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Long expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    _LONG(long.class, LONG),

    FLOAT(new MapleDslResultHandler<Object, Float>() {
        @Override
        public Class<java.lang.Float> resultType() {
            return Float.class;
        }

        @Override
        public java.lang.Float apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof Boolean) return (Boolean) source ? 0f : 1f;
            if (source instanceof Number) return ((Number) source).floatValue();
            if (source instanceof String) return java.lang.Float.parseFloat((String) source);
            if (source instanceof LocalDate) return (float) ((LocalDate) source)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalTime) return (float) ((LocalTime) source)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalDateTime) return (float) ((LocalDateTime) source)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();

            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Float expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    _FLOAT(float.class, FLOAT),

    DOUBLE(new MapleDslResultHandler<Object, Double>() {
        @Override
        public Class<java.lang.Double> resultType() {
            return Double.class;
        }

        @Override
        public java.lang.Double apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof Boolean) return (Boolean) source ? 0.0d : 1.0d;
            if (source instanceof Number) return ((Number) source).doubleValue();
            if (source instanceof String) return java.lang.Double.parseDouble((String) source);
            if (source instanceof LocalDate) return (double) ((LocalDate) source)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalTime) return (double) ((LocalTime) source)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            if (source instanceof LocalDateTime) return (double) ((LocalDateTime) source)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();

            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Double expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    _DOUBLE(double.class, DOUBLE),

    STRING(new MapleDslResultHandler<Object, String>() {
        @Override
        public Class<String> resultType() {
            return String.class;
        }

        @Override
        public String apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof String) return ((String) source);
            if (source instanceof Boolean || source instanceof Number) return String.valueOf(source);
            if (source instanceof LocalDate) return ((LocalDate) source).format(ctx.globalDateFormatter());
            if (source instanceof LocalTime) return ((LocalTime) source).format(ctx.globalTimeFormatter());
            if (source instanceof LocalDateTime) return ((LocalDateTime) source).format(ctx.globalDateTimeFormatter());
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for String expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    DATE(new MapleDslResultHandler<Object, Date>() {
        @Override
        public Class<Date> resultType() {
            return java.util.Date.class;
        }

        @Override
        public Date apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof Number) return new java.util.Date(((Number) source).longValue());
            if (source instanceof String) return new java.util.Date(LocalDateTime.parse(((String) source))
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            if (source instanceof LocalDate) return new java.util.Date(((LocalDate) source)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            if (source instanceof LocalTime) return new java.util.Date(((LocalTime) source)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            if (source instanceof LocalDateTime) return new java.util.Date(((LocalDateTime) source)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for java.util.Date expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    SQL_DATE(new MapleDslResultHandler<Object, java.sql.Date>() {
        @Override
        public Class<java.sql.Date> resultType() {
            return java.sql.Date.class;
        }

        @Override
        public java.sql.Date apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof Number) return new java.sql.Date(((Number) source).longValue());
            if (source instanceof String) return new java.sql.Date(LocalDateTime.parse(((String) source))
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            if (source instanceof LocalDate) return new java.sql.Date(((LocalDate) source)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            if (source instanceof LocalTime) return new java.sql.Date(((LocalTime) source)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            if (source instanceof LocalDateTime) return new java.sql.Date(((LocalDateTime) source)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for java.sql.Date expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    LOCAL_DATE_TIME(new MapleDslResultHandler<Object, LocalDateTime>() {
        @Override
        public Class<LocalDateTime> resultType() {
            return LocalDateTime.class;
        }

        @Override
        public LocalDateTime apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof LocalDateTime) return ((LocalDateTime) source);
            if (source instanceof Number) return LocalDateTime.ofInstant(Instant.ofEpochMilli(((Number) source).longValue()), ctx.globalZoneId());
            if (source instanceof String) return LocalDateTime.parse(((String) source).intern(), ctx.globalDateTimeFormatter());
            if (source instanceof LocalDate) return ((LocalDate) source).atTime(0, 0);
            if (source instanceof LocalTime) return ((LocalTime) source).atDate(LocalDate.now());
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for LocalDateTime expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    LOCAL_DATE(new MapleDslResultHandler<Object, LocalDate>() {
        @Override
        public Class<LocalDate> resultType() {
            return LocalDate.class;
        }

        @Override
        public LocalDate apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof LocalDate) return ((LocalDate) source);
            if (source instanceof Number) return LocalDateTime.ofInstant(Instant.ofEpochMilli(((Number) source).longValue()), ctx.globalZoneId()).toLocalDate();
            if (source instanceof String) return LocalDateTime.parse(((String) source).intern(), ctx.globalDateTimeFormatter()).toLocalDate();
            if (source instanceof LocalDateTime) return ((LocalDateTime) source).toLocalDate();
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for LocalDate expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    LOCAL_TIME(new MapleDslResultHandler<Object, LocalTime>() {
        @Override
        public Class<LocalTime> resultType() {
            return LocalTime.class;
        }

        @Override
        public LocalTime apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof LocalTime) return ((LocalTime) source);
            if (source instanceof Number) return LocalDateTime.ofInstant(Instant.ofEpochMilli(((Number) source).longValue()), ctx.globalZoneId()).toLocalTime();
            if (source instanceof String) return LocalDateTime.parse(((String) source).intern(), ctx.globalDateTimeFormatter()).toLocalTime();
            if (source instanceof LocalDateTime) return ((LocalDateTime) source).toLocalTime();
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for LocalTime expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    OFFSET_DATE_TIME(new MapleDslResultHandler<Object, OffsetDateTime>() {
        @Override
        public Class<OffsetDateTime> resultType() {
            return OffsetDateTime.class;
        }

        @Override
        public OffsetDateTime apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof OffsetDateTime) return ((OffsetDateTime) source);
            if (source instanceof Number) return OffsetDateTime.ofInstant(Instant.ofEpochMilli(((Number) source).longValue()), ctx.globalZoneId());
            if (source instanceof String) return OffsetDateTime.parse(((String) source).intern(), ctx.globalDateTimeFormatter());
            if (source instanceof LocalDateTime) return ((LocalDateTime) source).atOffset(ZoneOffset.of(ctx.globalTimeZone().getID()));
            if (source instanceof LocalDate) return ((LocalDate) source).atTime(0, 0).atOffset(ZoneOffset.of(ctx.globalTimeZone().getID()));
            if (source instanceof LocalTime) return ((LocalTime) source).atDate(LocalDate.now()).atOffset(ZoneOffset.of(ctx.globalTimeZone().getID()));
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for OffsetDateTime expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    OFFSET_TIME(new MapleDslResultHandler<Object, OffsetTime>() {
        @Override
        public OffsetTime apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof OffsetTime) return ((OffsetTime) source);
            if (source instanceof Number) return OffsetTime.ofInstant(Instant.ofEpochMilli(((Number) source).longValue()), ctx.globalZoneId());
            if (source instanceof String) return OffsetTime.parse(((String) source).intern(), ctx.globalDateTimeFormatter());
            if (source instanceof LocalDateTime) return ((LocalDateTime) source).toLocalTime().atOffset(ZoneOffset.of(ctx.globalTimeZone().getID()));
            if (source instanceof LocalTime) return ((LocalTime) source).atOffset(ZoneOffset.of(ctx.globalTimeZone().getID()));
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for OffsetTime expected conversation: {}, it will be deprecated.", source);
            return null;
        }

        @Override
        public Class<OffsetTime> resultType() {
            return OffsetTime.class;
        }
    }),
    ZONED_DATE_TIME(new MapleDslResultHandler<Object, ZonedDateTime>() {
        @Override
        public Class<ZonedDateTime> resultType() {
            return ZonedDateTime.class;
        }

        @Override
        public ZonedDateTime apply(Object source, MapleDslConfiguration ctx) {
            if (source == null) return null;
            if (source instanceof ZonedDateTime) return ((ZonedDateTime) source);
            if (source instanceof Number) return ZonedDateTime.ofInstant(Instant.ofEpochMilli(((Number) source).longValue()), ctx.globalZoneId());
            if (source instanceof String) return ZonedDateTime.parse(((String) source).intern(), ctx.globalDateTimeFormatter());
            if (source instanceof LocalDateTime) return ((LocalDateTime) source).atZone(ctx.globalZoneId());
            if (source instanceof LocalDate) return ((LocalDate) source).atTime(0,0).atZone(ctx.globalZoneId());
            if (source instanceof LocalTime) return ((LocalTime) source).atDate(LocalDate.now()).atZone(ctx.globalZoneId());
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for ZonedDateTime expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    }),
    ANY(new MapleDslResultHandler<Object, Object>() {
        @Override
        public Class<Object> resultType() {
            return Object.class;
        }

        @Override
        public Object apply(Object source, MapleDslConfiguration configuration) {
            return source;
        }
    });

    final MapleDslResultHandler<?, ?> resultHandler;
    final Class<?> resultType;

    static final Logger LOG = LoggerFactory.getLogger(DefaultMapleDslResultHandlers.class);

    <R> DefaultMapleDslResultHandlers(MapleDslResultHandler<?, R> resultHandler) {
        this.resultHandler = resultHandler;
        this.resultType = resultHandler.resultType();
    }

    <R> DefaultMapleDslResultHandlers(Class<R> resultType, DefaultMapleDslResultHandlers delegate) {
        this.resultType = resultType;
        this.resultHandler = delegate.resultHandler;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public MapleDslResultHandler<?, ?> getResultHandler() {
        return resultHandler;
    }
}
