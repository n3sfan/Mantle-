/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.gson;

import io.github.lethinh.mantle.gson.direct.CustomDataManager;

class MachineData {

    private int invSize;
    private String invTitle;
    private StackData[] stacks;
    private boolean stoppedTick;
    private String[] allowedPlayers;
    private CustomDataManager customDataManager;

    /* Getters & Setters */
    int getInvSize() {
        return invSize;
    }

    void setInvSize(int invSize) {
        this.invSize = invSize;
    }

    String getInvTitle() {
        return invTitle;
    }

    void setInvTitle(String invTitle) {
        this.invTitle = invTitle;
    }

    StackData[] getStacks() {
        return stacks;
    }

    void setStacks(StackData[] stacks) {
        this.stacks = stacks;
    }

    boolean isStoppedTick() {
        return stoppedTick;
    }

    void setStoppedTick(boolean stoppedTick) {
        this.stoppedTick = stoppedTick;
    }

    String[] getAllowedPlayers() {
        return allowedPlayers;
    }

    void setAllowedPlayers(String[] allowedPlayers) {
        this.allowedPlayers = allowedPlayers;
    }

    CustomDataManager getCustomDataManager() {
        return customDataManager;
    }

    void setCustomDataManager(CustomDataManager customDataManager) {
        this.customDataManager = customDataManager;
    }

}
