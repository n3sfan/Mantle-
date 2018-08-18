/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.energy;

public interface EnergyPulse {

    int getMaxEnergy();

    int getMaxInsert();

    int getMaxExtract();

    int getCurrentEnergy();

    void setCurrentEnergy(int energy);

    int insertEnergy(int energy);

    int extractEnergy(int energy);

}
