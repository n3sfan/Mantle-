/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.block;

import io.github.lethinh.mantle.energy.EnergyCapacitor;
import io.github.lethinh.mantle.energy.EnergyPulse;
import io.github.lethinh.mantle.io.direct.CustomDataManager;
import io.github.lethinh.mantle.utils.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;

public class BlockMachineEnergized extends BlockMachine implements EnergyPulse {

    private EnergyCapacitor energyCapacitor;

    public BlockMachineEnergized(MachineDefinition machineType, Block block, int invSlots, String invName, String... players) {
        super(machineType, block, invSlots, invName, players);
    }

    public BlockMachineEnergized(MachineDefinition machineType, Block block, Inventory inventory, String... players) {
        super(machineType, block, inventory, players);
    }

    public void transferEnergyToNearbyMachines(int energy) {
        for (BlockFace face : Utils.getMainFaces()) {
            Location location = Utils.offsetLocation(block.getLocation(), face);

            for (int i = 0; i < MACHINES.size(); ++i) {
                BlockMachine test = MACHINES.get(i);

                if (!(test instanceof BlockMachineEnergized) || !test.block.getLocation().equals(location)) {
                    continue;
                }

                BlockMachineEnergized adjacent = (BlockMachineEnergized) test;
                adjacent.insertEnergy(extractEnergy(energy));
                MACHINES.set(i, adjacent);
            }
        }
    }

    /* CustomDataSerializable */
    public void readCustomData(CustomDataManager manager) {
        energyCapacitor.readCustomData(manager);
    }

    public CustomDataManager writeCustomData() {
        return energyCapacitor.writeCustomData();
    }

    /* Getter & Setter */
    public EnergyCapacitor getEnergyCapacitor() {
        return energyCapacitor;
    }

    public void setEnergyCapacitor(EnergyCapacitor energyCapacitor) {
        this.energyCapacitor = energyCapacitor;
    }

    /* EnergyPulse */
    @Override
    public int getMaxEnergy() {
        return energyCapacitor.getMaxEnergy();
    }

    @Override
    public int getMaxInsert() {
        return energyCapacitor.getMaxInsert();
    }

    @Override
    public int getMaxExtract() {
        return energyCapacitor.getMaxExtract();
    }

    @Override
    public int getCurrentEnergy() {
        return energyCapacitor.getCurrentEnergy();
    }

    @Override
    public void setCurrentEnergy(int energy) {
        energyCapacitor.setCurrentEnergy(energy);
    }

    @Override
    public int insertEnergy(int energy) {
        return energyCapacitor.insertEnergy(energy);
    }

    @Override
    public int extractEnergy(int energy) {
        return energyCapacitor.extractEnergy(energy);
    }

}
