//package com.mapledsl.nebula.module;
//
//import com.mapledsl.core.MapleDslConfiguration;
//import com.mapledsl.core.MapleDslMapleDslConfiguration;
//import com.mapledsl.core.module.MapleDslResultHandler;
//import com.vesoft.nebula.Value;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.*;
//import java.util.Arrays;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//import static java.nio.charset.StandardCharsets.UTF_8;
//import static java.time.Instant.ofEpochMilli;
//import static java.time.LocalDateTime.ofInstant;
//
//public enum MapleNebulaDslResultHandlers implements MapleNebulaDslResultHandler<Object> {
//    Bool(boolean.class){
//        @Override
//        public Boolean apply(Value source, MapleDslConfiguration configuration) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return false;
//                case Value.BVAL: return source.isBVal();
//                case Value.IVAL: return source.getIVal() == 0;
//                case Value.FVAL: return source.getFVal() == 0;
//                case Value.SVAL: return Boolean.parseBoolean(AlephNebulaType.STRING.apply(source));
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Boolean expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    BOOLEAN(Boolean.class){
//        @Override
//        public Boolean apply(Value source, MapleDslConfiguration configuration) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return false;
//                case Value.BVAL: return source.isBVal();
//                case Value.IVAL: return source.getIVal() == 0;
//                case Value.FVAL: return source.getFVal() == 0;
//                case Value.SVAL: return Boolean.parseBoolean(AlephNebulaType.STRING.apply(source));
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Boolean expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    Char(char.class){
//        @Override
//        public Character apply(Value source, MapleDslConfiguration configuration) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? (char) 0 : (char) 1;
//                case Value.IVAL: return (char) source.getIVal();
//                case Value.FVAL: return (char) source.getFVal();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Character expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    CHAR(Character.class){
//        @Override
//        public Character apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? (char) 0 : (char) 1;
//                case Value.IVAL: return (char) source.getIVal();
//                case Value.FVAL: return (char) source.getFVal();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Character expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    Byte(byte.class){
//        @Override
//        public Byte apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? (byte) 0 : (byte) 1;
//                case Value.IVAL: return (byte) source.getIVal();
//                case Value.FVAL: return (byte) source.getFVal();
//                case Value.SVAL: return java.lang.Byte.parseByte(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return (byte) AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return (byte) AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return (byte) AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Byte expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    BYTE(Byte.class){
//        @Override
//        public Byte apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? (byte) 0 : (byte) 1;
//                case Value.IVAL: return (byte) source.getIVal();
//                case Value.FVAL: return (byte) source.getFVal();
//                case Value.SVAL: return java.lang.Byte.parseByte(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return (byte) AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return (byte) AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return (byte) AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Byte expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    Short(short.class){
//        @Override
//        public Short apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? (short) 0 : (short) 1;
//                case Value.IVAL: return (short) source.getIVal();
//                case Value.FVAL: return (short) source.getFVal();
//                case Value.SVAL: return java.lang.Short.parseShort(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return (short) AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return (short) AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return (short) AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Short expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    SHORT(Short.class){
//        @Override
//        public Short apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? (short) 0 : (short) 1;
//                case Value.IVAL: return (short) source.getIVal();
//                case Value.FVAL: return (short) source.getFVal();
//                case Value.SVAL: return java.lang.Short.parseShort(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return (short) AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return (short) AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return (short) AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Short expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    Int(int.class){
//        @Override
//        public Integer apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? 0 : 1;
//                case Value.IVAL: return (int) source.getIVal();
//                case Value.FVAL: return (int) source.getFVal();
//                case Value.SVAL: return Integer.parseInt(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return (int) AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return (int) AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return (int) AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Int expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    INT(Integer.class) {
//        @Override
//        public Integer apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? 0 : 1;
//                case Value.IVAL: return (int) source.getIVal();
//                case Value.FVAL: return (int) source.getFVal();
//                case Value.SVAL: return Integer.parseInt(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return (int) AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return (int) AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return (int) AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Int expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    Long(long.class){
//        @Override
//        public Long apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? 0L : 1L;
//                case Value.IVAL: return source.getIVal();
//                case Value.FVAL: return (long) source.getFVal();
//                case Value.SVAL: return java.lang.Long.parseLong(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Long expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    LONG(Long.class){
//        @Override
//        public Long apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? 0L : 1L;
//                case Value.IVAL: return source.getIVal();
//                case Value.FVAL: return (long) source.getFVal();
//                case Value.SVAL: return java.lang.Long.parseLong(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Long expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    Float(float.class){
//        @Override
//        public Float apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? 0f : 1f;
//                case Value.IVAL: return (float) source.getIVal();
//                case Value.FVAL: return (float) source.getFVal();
//                case Value.SVAL: return java.lang.Float.parseFloat(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return (float) AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return (float) AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return (float) AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Float expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    FLOAT(Float.class){
//        @Override
//        public Float apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? 0f : 1f;
//                case Value.IVAL: return (float) source.getIVal();
//                case Value.FVAL: return (float) source.getFVal();
//                case Value.SVAL: return java.lang.Float.parseFloat(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return (float) AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return (float) AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return (float) AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Float expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    Double(double.class){
//        @Override
//        public Double apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? 0d : 1d;
//                case Value.IVAL: return (double) source.getIVal();
//                case Value.FVAL: return source.getFVal();
//                case Value.SVAL: return java.lang.Double.parseDouble(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return (double) AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return (double) AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return (double) AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Double expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    DOUBLE(Double.class){
//        @Override
//        public Double apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.BVAL: return source.isBVal() ? 0d : 1d;
//                case Value.IVAL: return (double) source.getIVal();
//                case Value.FVAL: return source.getFVal();
//                case Value.SVAL: return java.lang.Double.parseDouble(AlephNebulaType.STRING.apply(source));
//                case Value.DVAL: return (double) AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.TVAL: return (double) AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                case Value.DTVAL: return (double) AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for Double expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    STRING(String.class) {
//        @Override
//        public String apply(Value source, MapleDslConfiguration configuration) {
//            switch (source.getSetField()) {
//                case Value.NVAL: return null;
//                case Value.SVAL: return AlephNebulaType.STRING.apply(source);
//                case Value.BVAL: return String.valueOf(source.isBVal());
//                case Value.IVAL: return String.valueOf(source.getIVal());
//                case Value.FVAL: return String.valueOf(source.getFVal());
//                case Value.DVAL: return AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .format(configuration.globalDateTimeFormatter());
//                case Value.TVAL: return AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .format(configuration.globalDateTimeFormatter());
//                case Value.DTVAL:return AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .format(configuration.globalDateTimeFormatter());
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for String expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    DATE(java.util.Date.class){
//        @Override
//        public java.util.Date apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()){
//                case Value.NVAL: return null;
//                case Value.IVAL: return new java.util.Date(source.getIVal());
//                case Value.FVAL: return new java.util.Date((long) source.getFVal());
//                case Value.SVAL: return new java.util.Date(((LocalDateTime) LOCAL_DATE_TIME.apply(source, ctx))
//                        .atZone(ZoneId.systemDefault())
//                        .toInstant()
//                        .toEpochMilli()
//                );
//                case Value.DVAL: return new java.util.Date(AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0,0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli()
//                );
//                case Value.TVAL: return new java.util.Date(AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli()
//                );
//                case Value.DTVAL:return new java.util.Date(AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli()
//                );
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for java.util.Date expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    SQL_DATE(java.sql.Date.class){
//        @Override
//        public java.sql.Date apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()){
//                case Value.NVAL: return null;
//                case Value.IVAL: return new java.sql.Date(source.getIVal());
//                case Value.FVAL: return new java.sql.Date((long) source.getFVal());
//                case Value.SVAL: return new java.sql.Date(((LocalDateTime) LOCAL_DATE_TIME.apply(source, ctx))
//                        .atZone(ZoneId.systemDefault())
//                        .toInstant()
//                        .toEpochMilli()
//                );
//                case Value.DVAL: return new java.sql.Date(AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0,0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli()
//                );
//                case Value.TVAL: return new java.sql.Date(AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli()
//                );
//                case Value.DTVAL:return new java.sql.Date(AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant()
//                        .toEpochMilli()
//                );
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for java.sql.Date expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    LOCAL_DATE_TIME(LocalDateTime.class){
//        @Override
//        public LocalDateTime apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()){
//                case Value.NVAL: return null;
//                case Value.IVAL: return ofInstant(ofEpochMilli(source.getIVal()), ctx.globalZoneId());
//                case Value.FVAL: return ofInstant(ofEpochMilli((long) source.getFVal()), ctx.globalZoneId());
//                case Value.SVAL: return ctx.globalDateTimeFormatter().parse(new String(source.getSVal(), UTF_8).intern(), LocalDateTime::from);
//                case Value.DVAL: return AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0, 0);
//                case Value.TVAL: return AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now());
//                case Value.DTVAL:return AlephNebulaType.DATETIME.<LocalDateTime>apply(source);
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for LocalDateTime expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    LOCAL_DATE(LocalDate.class){
//        @Override
//        public LocalDate apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()){
//                case Value.NVAL: return null;
//                case Value.IVAL: return ofInstant(ofEpochMilli(source.getIVal()), ctx.globalZoneId()).toLocalDate();
//                case Value.FVAL: return ofInstant(ofEpochMilli((long) source.getFVal()), ctx.globalZoneId()).toLocalDate();
//                case Value.SVAL: return ctx.globalDateFormatter().parse(new String(source.getSVal(), UTF_8).intern(), LocalDate::from);
//                case Value.DVAL: return AlephNebulaType.DATE.apply(source);
//                case Value.DTVAL:return AlephNebulaType.DATETIME.<LocalDateTime>apply(source).toLocalDate();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for LocalDate expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    LOCAL_TIME(LocalTime.class){
//        @Override
//        public LocalTime apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()){
//                case Value.NVAL: return null;
//                case Value.IVAL: return ofInstant(ofEpochMilli(source.getIVal()), ctx.globalZoneId()).toLocalTime();
//                case Value.FVAL: return ofInstant(ofEpochMilli((long) source.getFVal()), ctx.globalZoneId()).toLocalTime();
//                case Value.SVAL: return ctx.globalTimeFormatter().parse(new String(source.getSVal(), UTF_8).intern(), LocalTime::from);
//                case Value.TVAL: return AlephNebulaType.TIME.<LocalTime>apply(source);
//                case Value.DTVAL:return AlephNebulaType.DATETIME.<LocalDateTime>apply(source).toLocalTime();
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for LocalTime expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    OFFSET_DATE_TIME(OffsetDateTime.class){
//        @Override
//        public OffsetDateTime apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()){
//                case Value.NVAL: return null;
//                case Value.IVAL: return OffsetDateTime.ofInstant(ofEpochMilli(source.getIVal()), ctx.globalZoneId());
//                case Value.FVAL: return OffsetDateTime.ofInstant(ofEpochMilli((long) source.getFVal()), ctx.globalZoneId());
//                case Value.SVAL: return ctx.globalDateTimeFormatter().parse(new String(source.getSVal(), UTF_8).intern(), LocalDateTime::from)
//                        .atZone(ctx.globalZoneId())
//                        .toOffsetDateTime();
//                case Value.DVAL: return OffsetDateTime.ofInstant(AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0,0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant(), ctx.globalZoneId()
//                );
//                case Value.TVAL: return OffsetDateTime.ofInstant(AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant(), ctx.globalZoneId()
//                );
//                case Value.DTVAL:return OffsetDateTime.ofInstant(AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant(), ctx.globalZoneId()
//                );
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for OffsetDateTime expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    OFFSET_TIME(OffsetTime.class){
//        @Override
//        public OffsetTime apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()){
//                case Value.NVAL: return null;
//                case Value.IVAL: return OffsetTime.ofInstant(ofEpochMilli(source.getIVal()), ctx.globalZoneId());
//                case Value.FVAL: return OffsetTime.ofInstant(ofEpochMilli((long) source.getFVal()), ctx.globalZoneId());
//                case Value.SVAL: return ctx.globalTimeFormatter().parse(new String(source.getSVal(), UTF_8).intern(), LocalTime::from)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toOffsetDateTime()
//                        .toOffsetTime();
//                case Value.TVAL: return OffsetTime.ofInstant(AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant(), ctx.globalZoneId()
//                );
//                case Value.DTVAL:return OffsetTime.ofInstant(AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant(), ctx.globalZoneId()
//                );
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for OffsetDateTime expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    ZONED_DATE_TIME(ZonedDateTime.class) {
//        @Override
//        public ZonedDateTime apply(Value source, MapleDslConfiguration ctx) {
//            switch (source.getSetField()){
//                case Value.NVAL: return null;
//                case Value.IVAL: return ZonedDateTime.ofInstant(ofEpochMilli(source.getIVal()), ctx.globalZoneId());
//                case Value.FVAL: return ZonedDateTime.ofInstant(ofEpochMilli((long) source.getFVal()), ctx.globalZoneId());
//                case Value.SVAL: return ctx.globalDateTimeFormatter().parse(new String(source.getSVal(), UTF_8).intern(), LocalDateTime::from)
//                        .atZone(ctx.globalZoneId());
//                case Value.DVAL: return ZonedDateTime.ofInstant(AlephNebulaType.DATE.<LocalDate>apply(source)
//                        .atTime(0,0)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant(), ctx.globalZoneId()
//                );
//                case Value.TVAL: return ZonedDateTime.ofInstant(AlephNebulaType.TIME.<LocalTime>apply(source)
//                        .atDate(LocalDate.now())
//                        .atZone(ctx.globalZoneId())
//                        .toInstant(), ctx.globalZoneId()
//                );
//                case Value.DTVAL:return ZonedDateTime.ofInstant(AlephNebulaType.DATETIME.<LocalDateTime>apply(source)
//                        .atZone(ctx.globalZoneId())
//                        .toInstant(), ctx.globalZoneId()
//                );
//                default: if (LOG.isWarnEnabled()) LOG.warn("Illegal type for OffsetDateTime expected conversation: {}, it will be deprecated.", source);
//            }
//            return null;
//        }
//    },
//    ANY(Object.class){
//        @Override
//        public Object apply(Value source, MapleDslConfiguration ctx) {
//            AlephNebulaType nebulaType = NEBULA_TYPE_MAPPINGS.get(source.getSetField());
//            return nebulaType == null ? null : nebulaType.apply(source);
//        }
//    };
//
//    final Class<?> resultType;
//
//    static final Logger LOG = LoggerFactory.getLogger(AlephNebulaResultHandlers.class);
//
//    static final Map<Integer, AlephNebulaType> NEBULA_TYPE_MAPPINGS = Arrays.stream(AlephNebulaType.values()).collect(Collectors.toMap(
//            it -> it.setField, Function.identity()
//    ));
//
//    AlephNebulaResultHandlers(Class<?> resultType) {
//        this.resultType = resultType;
//    }
//
//}
