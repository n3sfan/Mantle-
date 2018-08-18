/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.energy;

import io.github.lethinh.mantle.io.direct.CustomDataManager;
import io.github.lethinh.mantle.io.direct.CustomDataSerializable;

public class EnergyCapacitor implements EnergyPulse, CustomDataSerializable {

    private final int maxEnergy, maxInsert, maxExtract;
    private int currentEnergy;

    public EnergyCapacitor(int maxEnergy, int maxInsert, int maxExtract) {
        this(maxEnergy, maxInsert, maxExtract, 0);
    }

    public EnergyCapacitor(int maxEnergy, int maxInsert, int maxExtract, int currentEnergy) {
        this.maxEnergy = maxEnergy;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
        this.currentEnergy = currentEnergy;
    }

    /* EnergyPulse */
    @Override
    public int getMaxEnergy() {
        return maxEnergy;
    }

    @Override
    public int getMaxInsert() {
        return maxInsert;
    }

    @Override
    public int getMaxExtract() {
        return maxExtract;
    }

    @Override
    public int getCurrentEnergy() {
        return currentEnergy;
    }

    @Override
    public void setCurrentEnergy(int currentEnergy) {
        if (currentEnergy < 0) {
            return;
        }

        if (currentEnergy >= maxEnergy) {
            this.currentEnergy = maxEnergy;
        } else {
            this.currentEnergy = currentEnergy;
        }
    }

    @Override
    public int insertEnergy(int energy) {
        if (currentEnergy == maxEnergy) {
            return 0;
        }

        int insert = currentEnergy + energy;

        if (insert > maxEnergy) {
            insert = maxInsert;
        }

        setCurrentEnergy(insert);
        return insert;
    }

    @Override
    public int extractEnergy(int energy) {
        if (currentEnergy < 0) {
            return 0;
        }

        if (energy > currentEnergy) {
            energy = currentEnergy;
        }

        int extract = currentEnergy - energy;

        if (extract < 0) {
            extract = 0;
        }

        setCurrentEnergy(extract);
        return extract;
    }

    /* CustomDataSerializable */
    @Override
    public void readCustomData(CustomDataManager manager) {
        setCurrentEnergy(manager.getAsNumber("currentEnergy").intValue());
    }

    @Override
    public CustomDataManager writeCustomData() {
        CustomDataManager manager = new CustomDataManager();
        manager.put("currentEnergy", currentEnergy);
        return manager;
    }

}
