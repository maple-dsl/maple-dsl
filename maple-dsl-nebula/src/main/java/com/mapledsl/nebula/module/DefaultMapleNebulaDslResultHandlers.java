package com.mapledsl.nebula.module;

import com.mapledsl.core.module.MapleDslResultHandler;
import com.vesoft.nebula.Value;

import java.time.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;

/**
 * Enumeration of default result handlers for Maple Nebula DSL.
 * <p></p>
 * This enumeration provides default result handlers for converting specific value types from
 * Maple Nebula DSL format to Java objects. Each enum constant represents a value type and
 * provides a lambda expression that performs the conversion.
 */
enum DefaultMapleNebulaDslResultHandlers {
    BOOLEAN(MapleNebulaDslResultHandler.identify(Boolean.class, (value, ctx) -> {
        switch (value.getSetField()) {
            case Value.BVAL: return value.isBVal();
            case Value.IVAL: return value.getIVal() == 0;
            case Value.FVAL: return value.getFVal() == 0;
            case Value.SVAL: return Boolean.parseBoolean(MapleNebulaDslType.STRING.apply(value));
            default: return false;
        }
    })),
    CHAR(MapleNebulaDslResultHandler.identify(Character.class, (value, ctx) -> {
        switch (value.getSetField()) {
            case Value.BVAL: return value.isBVal() ? (char) 0 : (char) 1;
            case Value.IVAL: return (char) value.getIVal();
            case Value.FVAL: return (char) value.getFVal();
            default: return null;
        }
    })),
    BYTE(MapleNebulaDslResultHandler.identify(Byte.class, (value, ctx) -> {
        switch (value.getSetField()) {
            case Value.BVAL: return value.isBVal() ? (byte) 0 : (byte) 1;
            case Value.IVAL: return (byte) value.getIVal();
            case Value.FVAL: return (byte) value.getFVal();
            case Value.SVAL: return java.lang.Byte.parseByte(MapleNebulaDslType.STRING.apply(value));
            case Value.DVAL: return (byte) MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.TVAL: return (byte) MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.DTVAL: return (byte) MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            default: return null;
        }
    })),
    SHORT(MapleNebulaDslResultHandler.identify(Short.class, (value, ctx) -> {
        switch (value.getSetField()) {
            case Value.BVAL: return value.isBVal() ? (short) 0 : (short) 1;
            case Value.IVAL: return (short) value.getIVal();
            case Value.FVAL: return (short) value.getFVal();
            case Value.SVAL: return java.lang.Short.parseShort(MapleNebulaDslType.STRING.apply(value));
            case Value.DVAL: return (short) MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.TVAL: return (short) MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.DTVAL: return (short) MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            default: return null;
        }
    })),
    INT(MapleNebulaDslResultHandler.identify(Integer.class, (value, ctx) -> {
        switch (value.getSetField()) {
            case Value.BVAL: return value.isBVal() ? 0 : 1;
            case Value.IVAL: return (int) value.getIVal();
            case Value.FVAL: return (int) value.getFVal();
            case Value.SVAL: return Integer.parseInt(MapleNebulaDslType.STRING.apply(value));
            case Value.DVAL: return (int) MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.TVAL: return (int) MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.DTVAL: return (int) MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        return null;
    })),
    LONG(MapleNebulaDslResultHandler.identify(Long.class, (value, ctx) -> {
        switch (value.getSetField()) {
            case Value.BVAL: return value.isBVal() ? 0L : 1L;
            case Value.IVAL: return value.getIVal();
            case Value.FVAL: return (long) value.getFVal();
            case Value.SVAL: return java.lang.Long.parseLong(MapleNebulaDslType.STRING.apply(value));
            case Value.DVAL: return MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.TVAL: return MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.DTVAL: return MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        return null;
    })),
    FLOAT(MapleNebulaDslResultHandler.identify(Float.class, (value, ctx) -> {
        switch (value.getSetField()) {
            case Value.BVAL: return value.isBVal() ? 0f : 1f;
            case Value.IVAL: return (float) value.getIVal();
            case Value.FVAL: return (float) value.getFVal();
            case Value.SVAL: return java.lang.Float.parseFloat(MapleNebulaDslType.STRING.apply(value));
            case Value.DVAL: return (float) MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.TVAL: return (float) MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.DTVAL: return (float) MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }
        return null;
    })),
    DOUBLE(MapleNebulaDslResultHandler.identify(Double.class, (value, ctx) -> {
        switch (value.getSetField()) {
            case Value.NVAL: return null;
            case Value.BVAL: return value.isBVal() ? 0d : 1d;
            case Value.IVAL: return (double) value.getIVal();
            case Value.FVAL: return value.getFVal();
            case Value.SVAL: return java.lang.Double.parseDouble(MapleNebulaDslType.STRING.apply(value));
            case Value.DVAL: return (double) MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.TVAL: return (double) MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
            case Value.DTVAL: return (double) MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli();
        }

        return null;
    })),
    STRING(MapleNebulaDslResultHandler.identify(String.class, (value, ctx) -> {
        switch (value.getSetField()) {
            case Value.SVAL: return MapleNebulaDslType.STRING.apply(value);
            case Value.BVAL: return String.valueOf(value.isBVal());
            case Value.IVAL: return String.valueOf(value.getIVal());
            case Value.FVAL: return String.valueOf(value.getFVal());
            case Value.DVAL: return MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .format(ctx.globalDateTimeFormatter());
            case Value.TVAL: return MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .format(ctx.globalDateTimeFormatter());
            case Value.DTVAL:return MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .format(ctx.globalDateTimeFormatter());
        }

        return null;
    })),
    DATE(MapleNebulaDslResultHandler.identify(java.util.Date.class, (value, ctx) -> {
        switch (value.getSetField()){
            case Value.IVAL: return new java.util.Date(value.getIVal());
            case Value.FVAL: return new java.util.Date((long) value.getFVal());
            case Value.SVAL: return new java.util.Date(LocalDateTime.parse(MapleNebulaDslType.STRING.apply(value), ctx.globalDateFormatter())
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            );
            case Value.DVAL: return new java.util.Date(MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0,0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            case Value.TVAL: return new java.util.Date(MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            case Value.DTVAL:return new java.util.Date(MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }

        return null;
    })),
    SQL_DATE(MapleNebulaDslResultHandler.identify(java.sql.Date.class, (value, ctx) -> {
        switch (value.getSetField()){
            case Value.IVAL: return new java.sql.Date(value.getIVal());
            case Value.FVAL: return new java.sql.Date((long) value.getFVal());
            case Value.SVAL: return new java.sql.Date(LocalDateTime.parse(MapleNebulaDslType.STRING.apply(value), ctx.globalDateFormatter())
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            );
            case Value.DVAL: return new java.sql.Date(MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0,0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            case Value.TVAL: return new java.sql.Date(MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
            case Value.DTVAL:return new java.sql.Date(MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            );
        }

        return null;
    })),
    LOCAL_DATE_TIME(MapleNebulaDslResultHandler.identify(LocalDateTime.class, (value, ctx) -> {
        switch (value.getSetField()){
            case Value.NVAL: return null;
            case Value.IVAL: return ofInstant(ofEpochMilli(value.getIVal()), ctx.globalZoneId());
            case Value.FVAL: return ofInstant(ofEpochMilli((long) value.getFVal()), ctx.globalZoneId());
            case Value.SVAL: return ctx.globalDateTimeFormatter().parse(new String(value.getSVal(), UTF_8).intern(), LocalDateTime::from);
            case Value.DVAL: return MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0, 0);
            case Value.TVAL: return MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now());
            case Value.DTVAL:return MapleNebulaDslType.DATETIME.apply(value);
        }

        return null;
    })),
    LOCAL_DATE(MapleNebulaDslResultHandler.identify(LocalDate.class, (value, ctx) -> {
        switch (value.getSetField()){
            case Value.NVAL: return null;
            case Value.IVAL: return ofInstant(ofEpochMilli(value.getIVal()), ctx.globalZoneId()).toLocalDate();
            case Value.FVAL: return ofInstant(ofEpochMilli((long) value.getFVal()), ctx.globalZoneId()).toLocalDate();
            case Value.SVAL: return ctx.globalDateFormatter().parse(new String(value.getSVal(), UTF_8).intern(), LocalDate::from);
            case Value.DVAL: return MapleNebulaDslType.DATE.apply(value);
            case Value.DTVAL:return MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value).toLocalDate();
        }

        return null;
    })),
    LOCAL_TIME(MapleNebulaDslResultHandler.identify(LocalTime.class, (value, ctx) -> {
        switch (value.getSetField()){
            case Value.NVAL: return null;
            case Value.IVAL: return ofInstant(ofEpochMilli(value.getIVal()), ctx.globalZoneId()).toLocalTime();
            case Value.FVAL: return ofInstant(ofEpochMilli((long) value.getFVal()), ctx.globalZoneId()).toLocalTime();
            case Value.SVAL: return ctx.globalTimeFormatter().parse(new String(value.getSVal(), UTF_8).intern(), LocalTime::from);
            case Value.TVAL: return MapleNebulaDslType.TIME.apply(value);
            case Value.DTVAL:return MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value).toLocalTime();
        }

        return null;
    })),
    OFFSET_DATE_TIME(MapleNebulaDslResultHandler.identify(OffsetDateTime.class, (value, ctx) -> {
        switch (value.getSetField()){
            case Value.NVAL: return null;
            case Value.IVAL: return OffsetDateTime.ofInstant(ofEpochMilli(value.getIVal()), ctx.globalZoneId());
            case Value.FVAL: return OffsetDateTime.ofInstant(ofEpochMilli((long) value.getFVal()), ctx.globalZoneId());
            case Value.SVAL: return ctx.globalDateTimeFormatter().parse(new String(value.getSVal(), UTF_8).intern(), LocalDateTime::from)
                    .atZone(ctx.globalZoneId())
                    .toOffsetDateTime();
            case Value.DVAL: return OffsetDateTime.ofInstant(MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0,0)
                    .atZone(ctx.globalZoneId())
                    .toInstant(), ctx.globalZoneId()
            );
            case Value.TVAL: return OffsetDateTime.ofInstant(MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant(), ctx.globalZoneId()
            );
            case Value.DTVAL:return OffsetDateTime.ofInstant(MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant(), ctx.globalZoneId()
            );
        }

        return null;
    })),
    OFFSET_TIME(MapleNebulaDslResultHandler.identify(OffsetTime.class, (value, ctx) -> {
        switch (value.getSetField()){
            case Value.NVAL: return null;
            case Value.IVAL: return OffsetTime.ofInstant(ofEpochMilli(value.getIVal()), ctx.globalZoneId());
            case Value.FVAL: return OffsetTime.ofInstant(ofEpochMilli((long) value.getFVal()), ctx.globalZoneId());
            case Value.SVAL: return ctx.globalTimeFormatter().parse(new String(value.getSVal(), UTF_8).intern(), LocalTime::from)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toOffsetDateTime()
                    .toOffsetTime();
            case Value.TVAL: return OffsetTime.ofInstant(MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant(), ctx.globalZoneId()
            );
            case Value.DTVAL:return OffsetTime.ofInstant(MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant(), ctx.globalZoneId()
            );
        }

        return null;
    })),
    ZONED_DATE_TIME(MapleNebulaDslResultHandler.identify(ZonedDateTime.class, (value, ctx) -> {
        switch (value.getSetField()){
            case Value.NVAL: return null;
            case Value.IVAL: return ZonedDateTime.ofInstant(ofEpochMilli(value.getIVal()), ctx.globalZoneId());
            case Value.FVAL: return ZonedDateTime.ofInstant(ofEpochMilli((long) value.getFVal()), ctx.globalZoneId());
            case Value.SVAL: return ctx.globalDateTimeFormatter().parse(new String(value.getSVal(), UTF_8).intern(), LocalDateTime::from)
                    .atZone(ctx.globalZoneId());
            case Value.DVAL: return ZonedDateTime.ofInstant(MapleNebulaDslType.DATE.<LocalDate>apply(value)
                    .atTime(0,0)
                    .atZone(ctx.globalZoneId())
                    .toInstant(), ctx.globalZoneId()
            );
            case Value.TVAL: return ZonedDateTime.ofInstant(MapleNebulaDslType.TIME.<LocalTime>apply(value)
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant(), ctx.globalZoneId()
            );
            case Value.DTVAL:return ZonedDateTime.ofInstant(MapleNebulaDslType.DATETIME.<LocalDateTime>apply(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant(), ctx.globalZoneId()
            );
        }

        return null;
    })),
    ANY(MapleNebulaDslResultHandler.identify(Object.class, (value, ctx) -> MapleNebulaDslType.any(value)));

    final MapleDslResultHandler<?,?> handler;

    DefaultMapleNebulaDslResultHandlers(MapleDslResultHandler<?, ?> handler) {
        this.handler = handler;
    }
}
