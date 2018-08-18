/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.block.impl;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachineEnergized;
import io.github.lethinh.mantle.block.GenericMachine;
import io.github.lethinh.mantle.energy.EnergyCapacitor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockEnergyPipe extends BlockMachineEnergized {

    public BlockEnergyPipe(Block block, String... players) {
        super(GenericMachine.ENERGY_PIPE, block, null, players);
        setEnergyCapacitor(new EnergyCapacitor(10000, 10000, 10000));
    }

    @Override
    public void handleUpdate(Mantle plugin) {
        runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
    }

    @Override
    public void work() {
        transferEnergyToNearbyMachines(getMaxExtract());
    }

    @Override
    public boolean canOpen(Player player) {
        return false;
    }

    @Override
    public int getRealSlots() {
        return 0;
    }

}
