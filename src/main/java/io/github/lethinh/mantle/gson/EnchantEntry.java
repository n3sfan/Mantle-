/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.gson;

class EnchantEntry {

    private String name;
    private int level;

    /* Getters & Setters */
    public String getName() {
        return name;
    }

    public EnchantEntry setName(String name) {
        this.name = name;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public EnchantEntry setLevel(int level) {
        this.level = level;
        return this;
    }

}
