package com.mapledsl.core.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class ID extends Number implements CharSequence, Serializable, Comparable<ID> {
    private final String textValue;
    private final Number numberValue;
    private final Object value;

    public ID(String textValue) {
        this.value = textValue;
        this.textValue = textValue;
        this.numberValue = null;
    }

    public ID(Number numberValue) {
        this.value = numberValue;
        this.numberValue = numberValue;
        this.textValue = null;
    }

    public Number getNumberValue() {
        return numberValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public int length() {
        return textValue != null ? textValue.length() : 0;
    }

    @Override
    public char charAt(int index) {
        return textValue != null ? textValue.charAt(index) : 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return textValue != null ? textValue.subSequence(start, end) : "";
    }

    @Override
    public int intValue() {
        return numberValue != null ? numberValue.intValue() : 0;
    }

    @Override
    public long longValue() {
        return numberValue != null ? numberValue.longValue() : 0;
    }

    @Override
    public float floatValue() {
        return numberValue != null ? numberValue.floatValue() : 0;
    }

    @Override
    public double doubleValue() {
        return numberValue != null ? numberValue.doubleValue() : 0;
    }

    @Override
    public String toString() {
        if (textValue != null) return textValue;
        if (numberValue != null) return numberValue.toString();
        return super.toString();
    }

    @Override
    public int compareTo(@NotNull ID o) {
        return o.toString().compareTo(this.toString());
    }

    public boolean equalsIgnoreCase(String anotherString) {
        return Objects.equals(textValue, anotherString)
                || (anotherString != null)
                && (anotherString.length() == textValue.length())
                && regionMatches(true, 0, anotherString, 0, textValue.length());
    }

    public boolean regionMatches(boolean ignoreCase, int toffset,
                                 String other, int ooffset, int len) {
        if (textValue == null) return false;

        char[] ta = textValue.toCharArray();
        int to = toffset;
        char[] pa = other.toCharArray();
        int po = ooffset;
        // Note: toffset, ooffset, or len might be near -1>>>1.
        if ((ooffset < 0) || (toffset < 0)
                || (toffset > (long)textValue.length() - len)
                || (ooffset > (long)other.length() - len)) {
            return false;
        }
        while (len-- > 0) {
            char c1 = ta[to++];
            char c2 = pa[po++];
            if (c1 == c2) {
                continue;
            }
            if (ignoreCase) {
                // If characters don't match but case may be ignored,
                // try converting both characters to uppercase.
                // If the results match, then the comparison scan should
                // continue.
                char u1 = Character.toUpperCase(c1);
                char u2 = Character.toUpperCase(c2);
                if (u1 == u2) {
                    continue;
                }
                // Unfortunately, conversion to uppercase does not work properly
                // for the Georgian alphabet, which has strange rules about case
                // conversion.  So we need to make one last check before
                // exiting.
                if (Character.toLowerCase(u1) == Character.toLowerCase(u2)) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean equals(Number number) {
        if (number == null) return false;
        return numberValue != null && numberValue.equals(number);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean equals(CharSequence charSequence) {
        if (charSequence == null) return false;
        return textValue != null && textValue.contentEquals(charSequence);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof ID) {
            ID id = (ID) o;
            return Objects.equals(textValue, id.textValue) && Objects.equals(numberValue, id.numberValue);
        }
        if (o instanceof Number) return o == numberValue;
        if (o instanceof CharSequence) return o == textValue;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(textValue, numberValue);
    }
}
