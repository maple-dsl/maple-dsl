package com.mapledsl.core.module;

import com.mapledsl.core.MapleDslConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

enum DefaultMapleDslResultHandlers implements MapleDslResultHandler<Object, Object> {
    Bool(boolean.class){
        @Override
        public Boolean apply(Object source, MapleDslConfiguration configuration) {
            if (source == null) return false;
            if (source instanceof Boolean) return (Boolean) source;
            if (source instanceof Number) return ((Number) source).intValue() == 0;
            if (source instanceof String) return Boolean.parseBoolean((String) source);
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Boolean expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    },
    BOOLEAN(Boolean.class){
        @Override
        public Object apply(Object source, MapleDslConfiguration configuration) {
            return Bool.apply(source, configuration);
        }
    },
    Char(char.class){
        @Override
        public Character apply(Object source, MapleDslConfiguration configuration) {
            if (source == null) return null;
            if (source instanceof Boolean) return (Boolean) source ? (char) 0 : 1;
            if (source instanceof Number) return (char) ((Number) source).intValue();
            if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Character expected conversation: {}, it will be deprecated.", source);
            return null;
        }
    },
    CHAR(Character.class){
        @Override
        public Object apply(Object source, MapleDslConfiguration ctx) {
            return Char.apply(source, ctx);
        }
    },
    Byte(byte.class){
        @Override
        public Byte apply(Object source, MapleDslConfiguration ctx) {
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
    },
    BYTE(Byte.class){
        @Override
        public Object apply(Object source, MapleDslConfiguration ctx) {
            return Byte.apply(source, ctx);
        }
    },
    Short(short.class){
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
    },
    SHORT(Short.class){
        @Override
        public Object apply(Object source, MapleDslConfiguration ctx) {
            return Short.apply(source, ctx);
        }
    },
    Int(int.class){
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
    },
    INT(Integer.class) {
        @Override
        public Object apply(Object source, MapleDslConfiguration ctx) {
            return Int.apply(source, ctx);
        }
    },
    Long(long.class){
        @Override
        public Long apply(Object source, MapleDslConfiguration ctx) {
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
    },
    LONG(Long.class){
        @Override
        public Object apply(Object source, MapleDslConfiguration ctx) {
            return LONG.apply(source, ctx);
        }
    },
    Float(float.class){
        @Override
        public Float apply(Object source, MapleDslConfiguration ctx) {
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
    },
    FLOAT(Float.class){
        @Override
        public Object apply(Object source, MapleDslConfiguration ctx) {
            return Float.apply(source, ctx);
        }
    },
    Double(double.class){
        @Override
        public Double apply(Object source, MapleDslConfiguration ctx) {
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
    },
    DOUBLE(Double.class){
        @Override
        public Object apply(Object source, MapleDslConfiguration ctx) {
            return Double.apply(source, ctx);
        }
    },
    STRING(String.class) {
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
    },
    DATE(java.util.Date.class){
        @Override
        public java.util.Date apply(Object source, MapleDslConfiguration ctx) {
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
    },
    SQL_DATE(java.sql.Date.class){
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
    },
    LOCAL_DATE_TIME(LocalDateTime.class){
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
    },
    LOCAL_DATE(LocalDate.class){
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
    },
    LOCAL_TIME(LocalTime.class){
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
    },
    OFFSET_DATE_TIME(OffsetDateTime.class){
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
    },
    OFFSET_TIME(OffsetTime.class){
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
    },
    ZONED_DATE_TIME(ZonedDateTime.class) {
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
    },
    ANY(Object.class){
        @Override
        public Object apply(Object source, MapleDslConfiguration ctx) {
            return source;
        }
    };

    final Class<?> resultType;

    static final Logger LOG = LoggerFactory.getLogger(DefaultMapleDslResultHandlers.class);

    DefaultMapleDslResultHandlers(Class<?> resultType) {
        this.resultType = resultType;
    }
}
