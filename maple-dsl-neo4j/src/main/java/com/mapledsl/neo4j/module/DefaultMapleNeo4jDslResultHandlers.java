package com.mapledsl.neo4j.module;

import com.mapledsl.core.module.MapleDslResultHandler;

import java.time.*;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;

enum DefaultMapleNeo4jDslResultHandlers {
    BOOLEAN(MapleNeo4jDslResultHandler.identify(Boolean.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.BOOLEAN.type) return value.asBoolean();
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return value.asInt() == 0;
        if (value.type() == MapleNeo4jDslType.STRING.type) return Boolean.parseBoolean(value.asString());
        return false;
    })),
    CHAR(MapleNeo4jDslResultHandler.identify(Character.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return (char) value.asInt();
        if (value.type() == MapleNeo4jDslType.BOOLEAN.type) return value.asBoolean() ? (char) 0 : (char) 1;
        return null;
    })),
    BYTE(MapleNeo4jDslResultHandler.identify(Byte.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return (byte) value.asInt();
        if (value.type() == MapleNeo4jDslType.BYTES.type) return value.asByteArray(new byte[] {0})[0];
        if (value.type() == MapleNeo4jDslType.BOOLEAN.type) return value.asBoolean() ? (byte) 0 : (byte) 1;
        if (value.type() == MapleNeo4jDslType.STRING.type) return java.lang.Byte.parseByte(value.asString());
        if (value.type() == MapleNeo4jDslType.DATE.type) {
            return (byte) value.asLocalDate()
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.TIME.type) {
            return (byte) MapleNeo4jDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.DATETIME.type) {
            return (byte) MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) {
            return (byte) MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) {
            return (byte) MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        return null;
    })),
    SHORT(MapleNeo4jDslResultHandler.identify(Short.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return (short) value.asInt();
        if (value.type() == MapleNeo4jDslType.BYTES.type) return (short) value.asByteArray(new byte[] {0})[0];
        if (value.type() == MapleNeo4jDslType.BOOLEAN.type) return value.asBoolean() ? (short) 0 : (short) 1;
        if (value.type() == MapleNeo4jDslType.STRING.type) return java.lang.Short.parseShort(value.asString());
        if (value.type() == MapleNeo4jDslType.DATE.type) {
            return (short) value.asLocalDate()
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.TIME.type) {
            return (short) MapleNeo4jDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.DATETIME.type) {
            return (short) MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) {
            return (short) MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) {
            return (short) MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        return null;
    })),
    INT(MapleNeo4jDslResultHandler.identify(Integer.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return value.asInt();
        if (value.type() == MapleNeo4jDslType.BYTES.type) return (int) value.asByteArray(new byte[] {0})[0];
        if (value.type() == MapleNeo4jDslType.BOOLEAN.type) return value.asBoolean() ? 0 : 1;
        if (value.type() == MapleNeo4jDslType.STRING.type) return java.lang.Integer.parseInt(value.asString());
        if (value.type() == MapleNeo4jDslType.DATE.type) {
            return (int) value.asLocalDate()
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.TIME.type) {
            return (int) MapleNeo4jDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.DATETIME.type) {
            return (int) MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) {
            return (int) MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) {
            return (int) MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        return null;
    })),
    LONG(MapleNeo4jDslResultHandler.identify(Long.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return value.asLong();
        if (value.type() == MapleNeo4jDslType.BYTES.type) return (long) value.asByteArray(new byte[]{0})[0];
        if (value.type() == MapleNeo4jDslType.BOOLEAN.type) return value.asBoolean() ? 0L : 1L;
        if (value.type() == MapleNeo4jDslType.STRING.type) return java.lang.Long.parseLong(value.asString());
        if (value.type() == MapleNeo4jDslType.DATE.type) {
            return value.asLocalDate()
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.TIME.type) {
            return MapleNeo4jDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.DATETIME.type) {
            return MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) {
            return MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) {
            return MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        return null;
    })),
    FLOAT(MapleNeo4jDslResultHandler.identify(Float.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type) return value.asFloat();
        if (value.type() == MapleNeo4jDslType.BYTES.type) return (float) value.asByteArray(new byte[]{0})[0];
        if (value.type() == MapleNeo4jDslType.BOOLEAN.type) return value.asBoolean() ? 0.0f : 1.0f;
        if (value.type() == MapleNeo4jDslType.STRING.type) return java.lang.Float.parseFloat(value.asString());
        if (value.type() == MapleNeo4jDslType.DATE.type) {
            return (float) value.asLocalDate()
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.TIME.type) {
            return (float) MapleNeo4jDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.DATETIME.type) {
            return (float) MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) {
            return (float) MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) {
            return (float) MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        return null;
    })),
    DOUBLE(MapleNeo4jDslResultHandler.identify(Double.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return value.asDouble();
        if (value.type() == MapleNeo4jDslType.BYTES.type) return (double) value.asByteArray(new byte[]{0})[0];
        if (value.type() == MapleNeo4jDslType.BOOLEAN.type) return value.asBoolean() ? 0.0d : 1.0d;
        if (value.type() == MapleNeo4jDslType.STRING.type) return java.lang.Double.parseDouble(value.asString());
        if (value.type() == MapleNeo4jDslType.DATE.type) {
            return (double) value.asLocalDate()
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.TIME.type) {
            return (double) MapleNeo4jDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.DATETIME.type) {
            return (double) MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) {
            return (double) MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) {
            return (double) MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        return null;
    })),
    STRING(MapleNeo4jDslResultHandler.identify(String.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.STRING.type) return value.asString();
        if (value.type() == MapleNeo4jDslType.BYTES.type) return new String(value.asByteArray(new byte[0])).intern();
        if (value.type() == MapleNeo4jDslType.BOOLEAN.type) return String.valueOf(value.asBoolean());
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return value.asNumber().toString();
        if (value.type() == MapleNeo4jDslType.DATE.type) return MapleNeo4jDslType.DATE.<LocalDate>apply(value).format(ctx.globalDateFormatter());
        if (value.type() == MapleNeo4jDslType.TIME.type) return MapleNeo4jDslType.TIME.<LocalTime>apply(value).format(ctx.globalTimeFormatter());
        if (value.type() == MapleNeo4jDslType.DATETIME.type) return MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value).format(ctx.globalDateTimeFormatter());
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) return MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value).format(ctx.globalTimeFormatter());
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) return MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value).format(ctx.globalDateTimeFormatter());
        return null;
    })),
    DATE(MapleNeo4jDslResultHandler.identify(java.util.Date.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) {
            return new java.util.Date(value.asNumber().longValue());
        }
        if (value.type() == MapleNeo4jDslType.STRING.type) {
            return new java.util.Date(LocalDateTime.parse(value.asString(), ctx.globalDateFormatter())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.BYTES.type) {
            return new java.util.Date(LocalDateTime.parse(new String(value.asByteArray(new byte[0])).intern(), ctx.globalDateFormatter())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.DATE.type) {
            return new java.util.Date(MapleNeo4jDslType.DATE.<LocalDate>apply(value)
                    .atTime(0,0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.TIME.type) {
            return new java.util.Date(MapleNeo4jDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.DATETIME.type) {
            return new java.util.Date(MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) {
            return new java.util.Date(MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) {
            return new java.util.Date(MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }

        return null;
    })),
    SQL_DATE(MapleNeo4jDslResultHandler.identify(java.sql.Date.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.STRING.type) {
            return new java.sql.Date(LocalDateTime.parse(value.asString(), ctx.globalDateFormatter())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.BYTES.type) {
            return new java.sql.Date(LocalDateTime.parse(new String(value.asByteArray(new byte[0])).intern(), ctx.globalDateFormatter())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) {
            return new java.sql.Date(value.asNumber().longValue());
        }
        if (value.type() == MapleNeo4jDslType.DATE.type) {
            return new java.sql.Date(MapleNeo4jDslType.DATE.<LocalDate>apply(value)
                    .atTime(0,0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.TIME.type) {
            return new java.sql.Date(MapleNeo4jDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.DATETIME.type) {
            return new java.sql.Date(MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) {
            return new java.sql.Date(MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) {
            return new java.sql.Date(MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }

        return null;
    })),
    LOCAL_DATE_TIME(MapleNeo4jDslResultHandler.identify(LocalDateTime.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.STRING.type) return LocalDateTime.parse(value.asString(), ctx.globalDateTimeFormatter());
        if (value.type() == MapleNeo4jDslType.BYTES.type) return LocalDateTime.parse(new String(value.asByteArray(new byte[0])).intern(), ctx.globalDateTimeFormatter());
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return ofInstant(ofEpochMilli(value.asNumber().longValue()), ctx.globalZoneId());
        if (value.type() == MapleNeo4jDslType.DATE.type) return MapleNeo4jDslType.DATE.<LocalDate>apply(value).atTime(0,0);
        if (value.type() == MapleNeo4jDslType.TIME.type) return MapleNeo4jDslType.TIME.<LocalTime>apply(value).atDate(LocalDate.now());
        if (value.type() == MapleNeo4jDslType.DATETIME.type) return MapleNeo4jDslType.DATETIME.apply(value);
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) return MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value).atDate(LocalDate.now());
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) return MapleNeo4jDslType.LOCAL_DATETIME.apply(value);

        return null;

    })),
    LOCAL_DATE(MapleNeo4jDslResultHandler.identify(LocalDate.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.STRING.type) return LocalDate.parse(value.asString(), ctx.globalDateFormatter());
        if (value.type() == MapleNeo4jDslType.BYTES.type) return LocalDate.parse(new String(value.asByteArray(new byte[0])).intern(), ctx.globalDateFormatter());
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return ofInstant(ofEpochMilli(value.asNumber().longValue()), ctx.globalZoneId()).toLocalDate();
        if (value.type() == MapleNeo4jDslType.DATE.type) return MapleNeo4jDslType.DATE.<LocalDate>apply(value);
        if (value.type() == MapleNeo4jDslType.DATETIME.type) return MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value).toLocalDate();
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) return MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value).toLocalDate();

        return null;
    })),
    LOCAL_TIME(MapleNeo4jDslResultHandler.identify(LocalTime.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.STRING.type) return LocalTime.parse(value.asString(), ctx.globalDateFormatter());
        if (value.type() == MapleNeo4jDslType.BYTES.type) return LocalTime.parse(new String(value.asByteArray(new byte[0])).intern(), ctx.globalTimeFormatter());
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return ofInstant(ofEpochMilli(value.asNumber().longValue()), ctx.globalZoneId()).toLocalTime();
        if (value.type() == MapleNeo4jDslType.TIME.type) return MapleNeo4jDslType.TIME.<LocalTime>apply(value);
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) return MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value);
        if (value.type() == MapleNeo4jDslType.DATETIME.type) return MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value).toLocalTime();
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) return MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value).toLocalTime();

        return null;
    })),
    OFFSET_DATE_TIME(MapleNeo4jDslResultHandler.identify(OffsetDateTime.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.STRING.type) return LocalDateTime.parse(value.asString(), ctx.globalDateTimeFormatter()).atZone(ctx.globalZoneId()).toOffsetDateTime();
        if (value.type() == MapleNeo4jDslType.BYTES.type) return LocalDateTime.parse(new String(value.asByteArray(new byte[0])).intern(), ctx.globalDateTimeFormatter()).atZone(ctx.globalZoneId()).toOffsetDateTime();
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return OffsetDateTime.ofInstant(ofEpochMilli(value.asNumber().longValue()), ctx.globalZoneId());
        if (value.type() == MapleNeo4jDslType.DATE.type) return MapleNeo4jDslType.DATE.<LocalDate>apply(value).atTime(0,0).atZone(ctx.globalZoneId()).toOffsetDateTime();
        if (value.type() == MapleNeo4jDslType.TIME.type) return MapleNeo4jDslType.TIME.<LocalTime>apply(value).atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toOffsetDateTime();
        if (value.type() == MapleNeo4jDslType.DATETIME.type) return MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value).atZone(ctx.globalZoneId()).toOffsetDateTime();
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) return MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value).atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toOffsetDateTime();
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) return MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value).atZone(ctx.globalZoneId()).toOffsetDateTime();

        return null;
    })),
    OFFSET_TIME(MapleNeo4jDslResultHandler.identify(OffsetTime.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.STRING.type) return OffsetTime.parse(value.asString(), ctx.globalTimeFormatter());
        if (value.type() == MapleNeo4jDslType.BYTES.type) return OffsetTime.parse(new String(value.asByteArray(new byte[0])).intern(), ctx.globalTimeFormatter());
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return OffsetTime.ofInstant(ofEpochMilli(value.asNumber().longValue()), ctx.globalZoneId());
        if (value.type() == MapleNeo4jDslType.TIME.type) return MapleNeo4jDslType.TIME.<LocalTime>apply(value).atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toOffsetDateTime().toOffsetTime();
        if (value.type() == MapleNeo4jDslType.DATETIME.type) return MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value).atZone(ctx.globalZoneId()).toOffsetDateTime().toOffsetTime();
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) return MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value).atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toOffsetDateTime().toOffsetTime();
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) return MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value).atZone(ctx.globalZoneId()).toOffsetDateTime().toOffsetTime();

        return null;
    })),
    ZONED_DATE_TIME(MapleNeo4jDslResultHandler.identify(ZonedDateTime.class, (value, ctx) -> {
        if (value.type() == MapleNeo4jDslType.STRING.type) return ZonedDateTime.parse(value.asString(), ctx.globalTimeFormatter());
        if (value.type() == MapleNeo4jDslType.BYTES.type) return ZonedDateTime.parse(new String(value.asByteArray(new byte[0])).intern(), ctx.globalTimeFormatter());
        if (value.type() == MapleNeo4jDslType.NUMBER.type || value.type() == MapleNeo4jDslType.INT.type || value.type() == MapleNeo4jDslType.FLOAT.type) return ZonedDateTime.ofInstant(ofEpochMilli(value.asNumber().longValue()), ctx.globalZoneId());
        if (value.type() == MapleNeo4jDslType.TIME.type) return MapleNeo4jDslType.TIME.<LocalTime>apply(value).atDate(LocalDate.now()).atZone(ctx.globalZoneId());
        if (value.type() == MapleNeo4jDslType.DATETIME.type) return MapleNeo4jDslType.DATETIME.<LocalDateTime>apply(value).atZone(ctx.globalZoneId());
        if (value.type() == MapleNeo4jDslType.LOCAL_TIME.type) return MapleNeo4jDslType.LOCAL_TIME.<LocalTime>apply(value).atDate(LocalDate.now()).atZone(ctx.globalZoneId());
        if (value.type() == MapleNeo4jDslType.LOCAL_DATETIME.type) return MapleNeo4jDslType.LOCAL_DATETIME.<LocalDateTime>apply(value).atZone(ctx.globalZoneId());

        return null;
    })),
    ANY(MapleNeo4jDslResultHandler.identify(Object.class, (value, ctx) -> MapleNeo4jDslType.any(value)));

    final MapleDslResultHandler<?,?> handler;

    DefaultMapleNeo4jDslResultHandlers(MapleDslResultHandler<?, ?> handler) {
        this.handler = handler;
    }
}
