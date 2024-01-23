package com.mapledsl.core.model;

public interface ID {
    String fragment();

    default boolean quoted() {
        return false;
    }
}
