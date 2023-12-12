package com.mapledsl.core.condition.common;

/**
 * Enum representing different types of operations that can be used in conditions.
 */
public enum OP {
    AND, OR, XOR,

    ASSIGN,

    EQ,
    NE,
    LT,
    LE,
    GT,
    GE,

    IN,
    NOT_IN,

    ISNULL,
    NOT_NULL,

    CONTAINS,

    STARTS_WITH,
    NOT_STARTS_WITH,

    ENDS_WITH,
    NOT_ENDS_WITH
}
