package com.mapledsl.core.module;

import java.time.*;

/**
 * The DefaultMapleDslResultHandlers enum contains a set of default result handlers for different types in the Maple DSL.
 * Each result handler provides a method for applying the conversion from the source object to the specified result type.
 * <p>
 * The enum also contains a set of corresponding fields for primitive types, which are just shorthand for the corresponding wrapper type result handlers.
 * <p>
 * The enum includes the following result handlers:
 * - BOOLEAN: Handles conversion to Boolean type.
 * - CHAR: Handles conversion to Character type.
 * - BYTE: Handles conversion to Byte type.
 * - SHORT: Handles conversion to Short type.
 * - INT: Handles conversion to Integer type.
 * - LONG: Handles conversion to Long type.
 * - FLOAT: Handles conversion to Float type.
 * - DOUBLE: Handles conversion to Double type.
 * <p>
 * Each result handler implements the MapleDslResultHandler interface, which defines the resultType() and apply() methods.
 * The resultType() method returns the class of the result type.
 * The apply() method applies the conversion from the source object to the result type.
 * If the conversion is not supported or fails, it returns null.
 * <p>
 * Usage example:
 * ```
 * Object source = ...; // The source object to convert
 * MapleDslConfiguration configuration = ...; // The configuration for the conversion
 * Boolean result = DefaultMapleDslResultHandlers.BOOLEAN.apply(source, configuration); // Apply the Boolean result handler
 * ```
 */
enum DefaultMapleDslResultHandlers {
    BOOLEAN(
            MapleDslResultHandler.identify(Byte.class, Boolean.class, (value, ctx) -> value == 0),
            MapleDslResultHandler.identify(Integer.class, Boolean.class, (value, ctx) -> value == 0),
            MapleDslResultHandler.identify(Short.class, Boolean.class, (value, ctx) -> value == 0),
            MapleDslResultHandler.identify(Long.class, Boolean.class, (value, ctx) -> value == 0),
            MapleDslResultHandler.identify(Float.class, Boolean.class, (value, ctx) -> value == 0),
            MapleDslResultHandler.identify(Double.class, Boolean.class, (value, ctx) -> value == 0),
            MapleDslResultHandler.identify(Number.class, Boolean.class, (value, ctx) -> value.intValue() == 0),
            MapleDslResultHandler.identify(Character.class, Boolean.class, (value, ctx) -> value == 0),
            MapleDslResultHandler.identify(String.class, Boolean.class, (value, ctx) -> Boolean.parseBoolean(value)),
            MapleDslResultHandler.identify(LocalDate.class, Boolean.class, (value, ctx) -> value.atTime(0,0).atZone(ctx.globalZoneId()).toInstant().toEpochMilli() == 0),
            MapleDslResultHandler.identify(LocalTime.class, Boolean.class, (value, ctx) -> (char) value.atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toInstant().toEpochMilli() == 0),
            MapleDslResultHandler.identify(OffsetTime.class, Boolean.class, (value, ctx) -> (char) value.atDate(LocalDate.now()).toInstant().toEpochMilli() == 0),
            MapleDslResultHandler.identify(OffsetDateTime.class, Boolean.class, (value, ctx) -> (char) value.toInstant().toEpochMilli() == 0),
            MapleDslResultHandler.identify(LocalDateTime.class, Boolean.class, (value, ctx) -> (char) value.atZone(ctx.globalZoneId()).toInstant().toEpochMilli() == 0),
            MapleDslResultHandler.identify(ZonedDateTime.class, Boolean.class, (value, ctx) -> (char) value.toInstant().toEpochMilli() == 0)
    ),
    CHAR(
            MapleDslResultHandler.identify(Byte.class, Character.class, (value, ctx) -> (char) value.intValue()),
            MapleDslResultHandler.identify(Integer.class, Character.class, (value, ctx) -> (char) value.intValue()),
            MapleDslResultHandler.identify(Short.class, Character.class, (value, ctx) -> (char) value.intValue()),
            MapleDslResultHandler.identify(Long.class, Character.class, (value, ctx) -> (char) value.intValue()),
            MapleDslResultHandler.identify(Float.class, Character.class, (value, ctx) -> (char) value.intValue()),
            MapleDslResultHandler.identify(Double.class, Character.class, (value, ctx) -> (char) value.intValue()),
            MapleDslResultHandler.identify(Number.class, Character.class, (value, ctx) -> (char) value.intValue()),
            MapleDslResultHandler.identify(Boolean.class, Character.class, (value, ctx) -> value ? '0' : '1'),
            MapleDslResultHandler.identify(String.class, Character.class, (value, ctx) -> value.charAt(0)),
            MapleDslResultHandler.identify(LocalDate.class, Character.class, (value, ctx) -> (char) value.atTime(0,0).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalTime.class, Character.class, (value, ctx) -> (char) value.atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetTime.class, Character.class, (value, ctx) -> (char) value.atDate(LocalDate.now()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetDateTime.class, Character.class, (value, ctx) -> (char) value.toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalDateTime.class, Character.class, (value, ctx) -> (char) value.atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(ZonedDateTime.class, Character.class, (value, ctx) -> (char) value.toInstant().toEpochMilli())
    ),
    BYTE(
            MapleDslResultHandler.identify(Integer.class, Byte.class, (value, ctx) -> (byte) value.intValue()),
            MapleDslResultHandler.identify(Short.class, Byte.class, (value, ctx) -> (byte) value.intValue()),
            MapleDslResultHandler.identify(Long.class, Byte.class, (value, ctx) -> (byte) value.intValue()),
            MapleDslResultHandler.identify(Float.class, Byte.class, (value, ctx) -> (byte) value.intValue()),
            MapleDslResultHandler.identify(Double.class, Byte.class, (value, ctx) -> (byte) value.intValue()),
            MapleDslResultHandler.identify(Character.class, Byte.class, (value, ctx) -> (byte) value.charValue()),
            MapleDslResultHandler.identify(Number.class, Byte.class, (value, ctx) -> (byte) value.intValue()),
            MapleDslResultHandler.identify(Boolean.class, Byte.class, (value, ctx) -> value ? (byte) 0 : ((byte) 1)),
            MapleDslResultHandler.identify(String.class, Byte.class, (value, ctx) -> Byte.parseByte(value)),
            MapleDslResultHandler.identify(LocalDate.class, Byte.class, (value, ctx) -> (byte) value.atTime(0,0).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalTime.class, Byte.class, (value, ctx) -> (byte) value.atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetTime.class, Byte.class, (value, ctx) -> (byte) value.atDate(LocalDate.now()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetDateTime.class, Byte.class, (value, ctx) -> (byte) value.toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalDateTime.class, Byte.class, (value, ctx) -> (byte) value.atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(ZonedDateTime.class, Byte.class, (value, ctx) -> (byte) value.toInstant().toEpochMilli())
    ),
    SHORT(
            MapleDslResultHandler.identify(Byte.class, Short.class, (value, ctx) -> (short) value.intValue()),
            MapleDslResultHandler.identify(Integer.class, Short.class, (value, ctx) -> (short) value.intValue()),
            MapleDslResultHandler.identify(Long.class, Short.class, (value, ctx) -> (short) value.intValue()),
            MapleDslResultHandler.identify(Float.class, Short.class, (value, ctx) -> (short) value.intValue()),
            MapleDslResultHandler.identify(Double.class, Short.class, (value, ctx) -> (short) value.intValue()),
            MapleDslResultHandler.identify(Character.class, Short.class, (value, ctx) -> (short) value.charValue()),
            MapleDslResultHandler.identify(Number.class, Short.class, (value, ctx) -> (short) value.intValue()),
            MapleDslResultHandler.identify(Boolean.class, Short.class, (value, ctx) -> value ? (short) 0 : ((short) 1)),
            MapleDslResultHandler.identify(String.class, Short.class, (value, ctx) -> Short.parseShort(value)),
            MapleDslResultHandler.identify(LocalDate.class, Short.class, (value, ctx) -> (short) value.atTime(0,0).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalTime.class, Short.class, (value, ctx) -> (short) value.atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetTime.class, Short.class, (value, ctx) -> (short) value.atDate(LocalDate.now()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetDateTime.class, Short.class, (value, ctx) -> (short) value.toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalDateTime.class, Short.class, (value, ctx) -> (short) value.atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(ZonedDateTime.class, Short.class, (value, ctx) -> (short) value.toInstant().toEpochMilli())
    ),
    INTEGER(
            MapleDslResultHandler.identify(Byte.class, Integer.class, (value, ctx) -> value.intValue()),
            MapleDslResultHandler.identify(Long.class, Integer.class, (value, ctx) -> value.intValue()),
            MapleDslResultHandler.identify(Float.class, Integer.class, (value, ctx) -> value.intValue()),
            MapleDslResultHandler.identify(Double.class, Integer.class, (value, ctx) -> value.intValue()),
            MapleDslResultHandler.identify(Character.class, Integer.class, (value, ctx) -> (int) value),
            MapleDslResultHandler.identify(Number.class, Integer.class, (value, ctx) -> value.intValue()),
            MapleDslResultHandler.identify(Boolean.class, Integer.class, (value, ctx) -> value ? 0 : 1),
            MapleDslResultHandler.identify(String.class, Integer.class, (value, ctx) -> Integer.parseInt(value)),
            MapleDslResultHandler.identify(LocalDate.class, Integer.class, (value, ctx) -> (int) value.atTime(0,0).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalTime.class, Integer.class, (value, ctx) -> (int) value.atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetTime.class, Integer.class, (value, ctx) -> (int) value.atDate(LocalDate.now()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetDateTime.class, Integer.class, (value, ctx) -> (int) value.toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalDateTime.class, Integer.class, (value, ctx) -> (int) value.atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(ZonedDateTime.class, Integer.class, (value, ctx) -> (int) value.toInstant().toEpochMilli())
    ),
    LONG(
            MapleDslResultHandler.identify(Byte.class, Long.class, (value, ctx) -> value.longValue()),
            MapleDslResultHandler.identify(Float.class, Long.class, (value, ctx) -> value.longValue()),
            MapleDslResultHandler.identify(Double.class, Long.class, (value, ctx) -> value.longValue()),
            MapleDslResultHandler.identify(Character.class, Long.class, (value, ctx) -> (long) value),
            MapleDslResultHandler.identify(Number.class, Long.class, (value, ctx) -> value.longValue()),
            MapleDslResultHandler.identify(Boolean.class, Long.class, (value, ctx) -> value ? 0L : 1L),
            MapleDslResultHandler.identify(String.class, Long.class, (value, ctx) -> Long.parseLong(value)),
            MapleDslResultHandler.identify(LocalDate.class, Long.class, (value, ctx) -> value.atTime(0,0).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalTime.class, Long.class, (value, ctx) -> value.atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetTime.class, Long.class, (value, ctx) -> value.atDate(LocalDate.now()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetDateTime.class, Long.class, (value, ctx) -> value.toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalDateTime.class, Long.class, (value, ctx) -> value.atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(ZonedDateTime.class, Long.class, (value, ctx) -> value.toInstant().toEpochMilli())
    ),
    FLOAT(
            MapleDslResultHandler.identify(Byte.class, Float.class, (value, ctx) -> value.floatValue()),
            MapleDslResultHandler.identify(Long.class, Float.class, (value, ctx) -> value.floatValue()),
            MapleDslResultHandler.identify(Double.class, Float.class, (value, ctx) -> value.floatValue()),
            MapleDslResultHandler.identify(Character.class, Float.class, (value, ctx) -> (float) value),
            MapleDslResultHandler.identify(Number.class, Float.class, (value, ctx) -> value.floatValue()),
            MapleDslResultHandler.identify(Boolean.class, Float.class, (value, ctx) -> value ? 0f : 1f),
            MapleDslResultHandler.identify(String.class, Float.class, (value, ctx) -> Float.parseFloat(value)),
            MapleDslResultHandler.identify(LocalDate.class, Float.class, (value, ctx) -> (float) value.atTime(0,0).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalTime.class, Float.class, (value, ctx) -> (float) value.atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetTime.class, Float.class, (value, ctx) -> (float) value.atDate(LocalDate.now()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetDateTime.class, Float.class, (value, ctx) -> (float) value.toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalDateTime.class, Float.class, (value, ctx) -> (float) value.atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(ZonedDateTime.class, Float.class, (value, ctx) -> (float) value.toInstant().toEpochMilli())
    ),
    DOUBLE(
            MapleDslResultHandler.identify(Byte.class, Double.class, (value, ctx) -> value.doubleValue()),
            MapleDslResultHandler.identify(Long.class, Double.class, (value, ctx) -> value.doubleValue()),
            MapleDslResultHandler.identify(Float.class, Double.class, (value, ctx) -> value.doubleValue()),
            MapleDslResultHandler.identify(Character.class, Double.class, (value, ctx) -> (double) value),
            MapleDslResultHandler.identify(Number.class, Double.class, (value, ctx) -> value.doubleValue()),
            MapleDslResultHandler.identify(Boolean.class, Double.class, (value, ctx) -> value ? 0d : 1d),
            MapleDslResultHandler.identify(String.class, Double.class, (value, ctx) -> Double.parseDouble(value)),
            MapleDslResultHandler.identify(LocalDate.class, Double.class, (value, ctx) -> (double) value.atTime(0,0).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalTime.class, Double.class, (value, ctx) -> (double) value.atDate(LocalDate.now()).atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetTime.class, Double.class, (value, ctx) -> (double) value.atDate(LocalDate.now()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(OffsetDateTime.class, Double.class, (value, ctx) -> (double) value.toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(LocalDateTime.class, Double.class, (value, ctx) -> (double) value.atZone(ctx.globalZoneId()).toInstant().toEpochMilli()),
            MapleDslResultHandler.identify(ZonedDateTime.class, Double.class, (value, ctx) -> (double) value.toInstant().toEpochMilli())
    ),
    STRING(
            MapleDslResultHandler.identify(Byte.class, String.class, (value, ctx) -> String.valueOf(value)),
            MapleDslResultHandler.identify(Long.class, String.class, (value, ctx) -> String.valueOf(value)),
            MapleDslResultHandler.identify(Float.class, String.class, (value, ctx) -> String.valueOf(value)),
            MapleDslResultHandler.identify(Double.class, String.class, (value, ctx) -> String.valueOf(value)),
            MapleDslResultHandler.identify(Character.class, String.class, (value, ctx) -> String.valueOf(value)),
            MapleDslResultHandler.identify(Number.class, String.class, (value, ctx) -> String.valueOf(value)),
            MapleDslResultHandler.identify(Boolean.class, String.class, (value, ctx) -> String.valueOf(value)),
            MapleDslResultHandler.identify(LocalDate.class, String.class, (value, ctx) -> ctx.globalDateFormatter().format(value)),
            MapleDslResultHandler.identify(LocalTime.class, String.class, (value, ctx) -> ctx.globalTimeFormatter().format(value)),
            MapleDslResultHandler.identify(OffsetTime.class, String.class, (value, ctx) -> ctx.globalTimeFormatter().format(value)),
            MapleDslResultHandler.identify(OffsetDateTime.class, String.class, (value, ctx) -> ctx.globalDateTimeFormatter().format(value)),
            MapleDslResultHandler.identify(LocalDateTime.class, String.class, (value, ctx) -> ctx.globalDateTimeFormatter().format(value)),
            MapleDslResultHandler.identify(ZonedDateTime.class, String.class, (value, ctx) -> ctx.globalDateTimeFormatter().format(value))
    ),
    DATE(
            MapleDslResultHandler.identify(Byte.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value.longValue())),
            MapleDslResultHandler.identify(Integer.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value.longValue())),
            MapleDslResultHandler.identify(Short.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value.longValue())),
            MapleDslResultHandler.identify(Long.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value)),
            MapleDslResultHandler.identify(Float.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value.longValue())),
            MapleDslResultHandler.identify(Double.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value.longValue())),
            MapleDslResultHandler.identify(Number.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value.longValue())),
            MapleDslResultHandler.identify(String.class, java.util.Date.class, (value, ctx) -> new java.util.Date(LocalDateTime.parse(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(LocalDate.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(LocalTime.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(OffsetTime.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value
                    .atDate(LocalDate.now())
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(OffsetDateTime.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(LocalDateTime.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(ZonedDateTime.class, java.util.Date.class, (value, ctx) -> new java.util.Date(value
                    .toInstant()
                    .toEpochMilli()
            ))
    ),
    SQL_DATE(
            MapleDslResultHandler.identify(Byte.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value.longValue())),
            MapleDslResultHandler.identify(Integer.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value.longValue())),
            MapleDslResultHandler.identify(Short.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value.longValue())),
            MapleDslResultHandler.identify(Long.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value)),
            MapleDslResultHandler.identify(Float.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value.longValue())),
            MapleDslResultHandler.identify(Double.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value.longValue())),
            MapleDslResultHandler.identify(Number.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value.longValue())),
            MapleDslResultHandler.identify(String.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(LocalDateTime.parse(value)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(LocalDate.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value
                    .atTime(0, 0)
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(LocalTime.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value
                    .atDate(LocalDate.now())
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(OffsetTime.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value
                    .atDate(LocalDate.now())
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(OffsetDateTime.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(LocalDateTime.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value
                    .atZone(ctx.globalZoneId())
                    .toInstant()
                    .toEpochMilli()
            )),
            MapleDslResultHandler.identify(ZonedDateTime.class, java.sql.Date.class, (value, ctx) -> new java.sql.Date(value
                    .toInstant()
                    .toEpochMilli()
            ))
    ),
    LOCAL_DATE(
            MapleDslResultHandler.identify(Byte.class, LocalDate.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalDate()),
            MapleDslResultHandler.identify(Integer.class, LocalDate.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalDate()),
            MapleDslResultHandler.identify(Short.class, LocalDate.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalDate()),
            MapleDslResultHandler.identify(Long.class, LocalDate.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ctx.globalZoneId()).toLocalDate()),
            MapleDslResultHandler.identify(Float.class, LocalDate.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalDate()),
            MapleDslResultHandler.identify(Double.class, LocalDate.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalDate()),
            MapleDslResultHandler.identify(Number.class, LocalDate.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalDate()),
            MapleDslResultHandler.identify(String.class, LocalDate.class, (value, ctx) -> LocalDateTime.parse(value.intern(), ctx.globalDateTimeFormatter()).toLocalDate()),
            MapleDslResultHandler.identify(OffsetDateTime.class, LocalDate.class, (value, ctx) -> value.toLocalDate()),
            MapleDslResultHandler.identify(LocalDateTime.class, LocalDate.class, (value, ctx) -> value.toLocalDate()),
            MapleDslResultHandler.identify(ZonedDateTime.class, LocalDate.class, (value, ctx) -> value.toLocalDate())
    ),
    LOCAL_TIME(
            MapleDslResultHandler.identify(Byte.class, LocalTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalTime()),
            MapleDslResultHandler.identify(Integer.class, LocalTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalTime()),
            MapleDslResultHandler.identify(Short.class, LocalTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalTime()),
            MapleDslResultHandler.identify(Long.class, LocalTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ctx.globalZoneId()).toLocalTime()),
            MapleDslResultHandler.identify(Float.class, LocalTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalTime()),
            MapleDslResultHandler.identify(Double.class, LocalTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalTime()),
            MapleDslResultHandler.identify(Number.class, LocalTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId()).toLocalTime()),
            MapleDslResultHandler.identify(String.class, LocalTime.class, (value, ctx) -> LocalDateTime.parse(value.intern(), ctx.globalDateTimeFormatter()).toLocalTime()),
            MapleDslResultHandler.identify(OffsetTime.class, LocalTime.class, (value, ctx) -> value.toLocalTime()),
            MapleDslResultHandler.identify(OffsetDateTime.class, LocalTime.class, (value, ctx) -> value.toLocalTime()),
            MapleDslResultHandler.identify(LocalDateTime.class, LocalTime.class, (value, ctx) -> value.toLocalTime()),
            MapleDslResultHandler.identify(ZonedDateTime.class, LocalTime.class, (value, ctx) -> value.toLocalTime())
    ),
    LOCAL_DATE_TIME(
            MapleDslResultHandler.identify(Byte.class, LocalDateTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Integer.class, LocalDateTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Short.class, LocalDateTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Long.class, LocalDateTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Float.class, LocalDateTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Double.class, LocalDateTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Number.class, LocalDateTime.class, (value, ctx) -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(String.class, LocalDateTime.class, (value, ctx) -> LocalDateTime.parse(value.intern(), ctx.globalDateTimeFormatter())),
            MapleDslResultHandler.identify(LocalDate.class, LocalDateTime.class, (value, ctx) -> value.atTime(0,0)),
            MapleDslResultHandler.identify(LocalTime.class, LocalDateTime.class, (value, ctx) -> value.atDate(LocalDate.now())),
            MapleDslResultHandler.identify(OffsetTime.class, LocalDateTime.class, (value, ctx) -> value.atDate(LocalDate.now()).toLocalDateTime()),
            MapleDslResultHandler.identify(OffsetDateTime.class, LocalDateTime.class, (value, ctx) -> value.toLocalDateTime()),
            MapleDslResultHandler.identify(ZonedDateTime.class, LocalDateTime.class, (value, ctx) -> value.toLocalDateTime())
    ),
    OFFSET_TIME(
            MapleDslResultHandler.identify(Byte.class, OffsetTime.class, (value, ctx) -> OffsetTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Integer.class, OffsetTime.class, (value, ctx) -> OffsetTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Short.class, OffsetTime.class, (value, ctx) -> OffsetTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Long.class, OffsetTime.class, (value, ctx) -> OffsetTime.ofInstant(Instant.ofEpochMilli(value), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Float.class, OffsetTime.class, (value, ctx) -> OffsetTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Double.class, OffsetTime.class, (value, ctx) -> OffsetTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Number.class, OffsetTime.class, (value, ctx) -> OffsetTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(String.class, OffsetTime.class, (value, ctx) -> OffsetTime.parse(value.intern(), ctx.globalDateTimeFormatter())),
            MapleDslResultHandler.identify(LocalDateTime.class, OffsetTime.class, (value, ctx) -> value.toLocalTime().atOffset(ZoneOffset.of(ctx.globalTimeZone().getID()))),
            MapleDslResultHandler.identify(LocalTime.class, OffsetTime.class, (value, ctx) -> value.atOffset(ZoneOffset.of(ctx.globalTimeZone().getID()))),
            MapleDslResultHandler.identify(OffsetDateTime.class, OffsetTime.class, (value, ctx) -> value.toOffsetTime()),
            MapleDslResultHandler.identify(ZonedDateTime.class, OffsetTime.class, (value, ctx) -> value.toOffsetDateTime().toOffsetTime())
    ),
    OFFSET_DATE_TIME(
            MapleDslResultHandler.identify(Byte.class, OffsetDateTime.class, (value, ctx) -> OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Integer.class, OffsetDateTime.class, (value, ctx) -> OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Short.class, OffsetDateTime.class, (value, ctx) -> OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Long.class, OffsetDateTime.class, (value, ctx) -> OffsetDateTime.ofInstant(Instant.ofEpochMilli(value), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Float.class, OffsetDateTime.class, (value, ctx) -> OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Double.class, OffsetDateTime.class, (value, ctx) -> OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Number.class, OffsetDateTime.class, (value, ctx) -> OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(String.class, OffsetDateTime.class, (value, ctx) -> OffsetDateTime.parse(value.intern(), ctx.globalDateTimeFormatter())),
            MapleDslResultHandler.identify(LocalDate.class, OffsetDateTime.class, (value, ctx) -> value.atTime(0,0).atOffset(ZoneOffset.of(ctx.globalTimeZone().getID()))),
            MapleDslResultHandler.identify(LocalTime.class, OffsetDateTime.class, (value, ctx) -> value.atDate(LocalDate.now()).atOffset(ZoneOffset.of(ctx.globalTimeZone().getID()))),
            MapleDslResultHandler.identify(LocalDateTime.class, OffsetDateTime.class, (value, ctx) -> value.atOffset(ZoneOffset.of(ctx.globalTimeZone().getID()))),
            MapleDslResultHandler.identify(OffsetTime.class, OffsetDateTime.class, (value, ctx) -> value.atDate(LocalDate.now())),
            MapleDslResultHandler.identify(ZonedDateTime.class, OffsetDateTime.class, (value, ctx) -> value.toOffsetDateTime())
    ),
    ZONED_DATE_TIME(
            MapleDslResultHandler.identify(Byte.class, ZonedDateTime.class, (value, ctx) -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Integer.class, ZonedDateTime.class, (value, ctx) -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Short.class, ZonedDateTime.class, (value, ctx) -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Long.class, ZonedDateTime.class, (value, ctx) -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Float.class, ZonedDateTime.class, (value, ctx) -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Double.class, ZonedDateTime.class, (value, ctx) -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(Number.class, ZonedDateTime.class, (value, ctx) -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.longValue()), ctx.globalZoneId())),
            MapleDslResultHandler.identify(String.class, ZonedDateTime.class, (value, ctx) -> ZonedDateTime.parse(value.intern(), ctx.globalDateTimeFormatter())),
            MapleDslResultHandler.identify(LocalDate.class, ZonedDateTime.class, (value, ctx) -> value.atTime(0,0).atZone(ZoneOffset.of(ctx.globalTimeZone().getID()))),
            MapleDslResultHandler.identify(LocalTime.class, ZonedDateTime.class, (value, ctx) -> value.atDate(LocalDate.now()).atZone(ZoneOffset.of(ctx.globalTimeZone().getID()))),
            MapleDslResultHandler.identify(LocalDateTime.class, ZonedDateTime.class, (value, ctx) -> value.atZone(ZoneOffset.of(ctx.globalTimeZone().getID()))),
            MapleDslResultHandler.identify(OffsetTime.class, ZonedDateTime.class, (value, ctx) -> value.atDate(LocalDate.now()).toZonedDateTime()),
            MapleDslResultHandler.identify(OffsetDateTime.class, ZonedDateTime.class, (value, ctx) -> value.toZonedDateTime())
    ),
    ANY(MapleDslResultHandler.identify(Object.class, Object.class, (value, ctx) -> value));

    final MapleDslResultHandler<?, ?>[] handlers;

    DefaultMapleDslResultHandlers(MapleDslResultHandler<?, ?>... handlers) {
        this.handlers = handlers;
    }
}
