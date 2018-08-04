/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.io.direct;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Look-up {@link CustomData} and get value from it easily
 */
public class CustomDataManager implements Map<String, Object> {

    private List<CustomData> dataList;
    private final Map<String, Object> dataMap;

    public CustomDataManager() {
        this(new ArrayList<>());
    }

    public CustomDataManager(List<CustomData> dataList) {
        this.dataList = dataList;
        this.dataMap = new HashMap<>();

        if (!dataList.isEmpty()) {
            dataList.forEach(this::put);
        }
    }

    /* Lookup Helpers */
    public Object put(CustomData data) {
        return put(data.getKey(), data.getValue());
    }

    public CustomData getData(String key) {
        return new CustomData(key, get(key));
    }

    public Boolean getAsBoolean(String key) {
        return getData(key).getValueAsBoolean();
    }

    public Number getAsNumber(String key) {
        return getData(key).getValueAsNumber();
    }

    public Character getAsChar(String key) {
        return getData(key).getValueAsChar();
    }

    public String getAsString(String key) {
        return getData(key).getValueAsString();
    }

    /* Getters */
    public List<CustomData> getDataList() {
        return dataList;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    /* Map */
    @Override
    public int size() {
        return dataMap.size();
    }

    @Override
    public boolean isEmpty() {
        return dataList.isEmpty() || dataMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return dataMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return dataMap.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return dataMap.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        dataList.add(new CustomData(key, value));
        return dataMap.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return dataMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        dataMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return dataMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return dataMap.values();
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return dataMap.entrySet();
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        dataMap.forEach(action);
    }

}
