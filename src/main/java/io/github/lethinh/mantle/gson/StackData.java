/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.gson;

import org.bukkit.inventory.ItemFlag;

class StackData {

    private int slot;
    private String type;
    private int amount;
    private byte data;
    private short damage;
    private String displayName;
    private String localizedName;
    private String[] lore;
    private EnchantEntry[] enchants;
    private ItemFlag[] flags;

    /* Getters & Setters */
    int getSlot() {
        return slot;
    }

    void setSlot(int slot) {
        this.slot = slot;
    }

    String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    int getAmount() {
        return amount;
    }

    void setAmount(int amount) {
        this.amount = amount;
    }

    byte getData() {
        return data;
    }

    void setData(byte data) {
        this.data = data;
    }

    short getDamage() {
        return damage;
    }

    void setDamage(short damage) {
        this.damage = damage;
    }

    String getDisplayName() {
        return displayName;
    }

    void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    String getLocalizedName() {
        return localizedName;
    }

    void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    String[] getLore() {
        return lore;
    }

    void setLore(String[] lore) {
        this.lore = lore;
    }

    EnchantEntry[] getEnchants() {
        return enchants;
    }

    void setEnchants(EnchantEntry[] enchants) {
        this.enchants = enchants;
    }

    ItemFlag[] getFlags() {
        return flags;
    }

    void setFlags(ItemFlag[] flags) {
        this.flags = flags;
    }

}
