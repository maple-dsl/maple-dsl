package com.mapledsl.core.condition.common;

/**
 * @author bofa1ex
 * @since 2023/8/14
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
