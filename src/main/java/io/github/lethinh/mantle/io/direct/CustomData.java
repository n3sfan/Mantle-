/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.io.direct;

import com.google.gson.internal.LazilyParsedNumber;

/**
 * Allow reading and writing machines data directly to JSON. Multiple custom data are represented under array, you
 * should use {@link CustomDataManager}, it does all the things. Implement {@link CustomDataSerializable} to use
 */
public class CustomData {

    private String key;
    private Object value;

    public CustomData() {

    }

    public CustomData(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value == null ? "" : value;
    }

    public boolean isBooleanValue() {
        return value instanceof Boolean;
    }

    public boolean isNumberValue() {
        return value instanceof Number;
    }

    public boolean isStringValue() {
        return value instanceof String;
    }

    public Boolean getValueAsBoolean() {
        return isStringValue() ? Boolean.parseBoolean((String) value) : value instanceof Boolean ? (Boolean) value : false;
    }

    public Number getValueAsNumber() {
        try {
            return isStringValue() ? new LazilyParsedNumber((String) value) : (Number) value;
        } catch (Throwable e) {
            return -1;
        }
    }

    public Character getValueAsChar() {
        return getValueAsString().charAt(0);
    }

    public String getValueAsString() {
        return isStringValue() ? (String) value : String.valueOf(value);
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
