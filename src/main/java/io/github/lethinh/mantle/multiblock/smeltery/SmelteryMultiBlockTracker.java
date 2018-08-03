package io.github.lethinh.mantle.multiblock.smeltery;

import org.bukkit.Location;
import org.bukkit.block.Block;

import io.github.lethinh.mantle.MantleItemStacks;
import io.github.lethinh.mantle.multiblock.MultiBlockController;
import io.github.lethinh.mantle.multiblock.MultiBlockTracker;
import io.github.lethinh.mantle.utils.Utils;

/**
 * Created by Le Thinh
 */
public class SmelteryMultiBlockTracker extends MultiBlockTracker {

    public SmelteryMultiBlockTracker(@SuppressWarnings("rawtypes") MultiBlockController machine) {
        super(machine, 3);
    }

    @Override
    protected boolean isBlockValid(Location location) {
        Block block = location.getBlock();
        return Utils.isBlockEqualStack(block, MantleItemStacks.SMELTERY_BLOCK) || Utils.isBlockEqualStack(block, MantleItemStacks.SMELTERY_CONTROLLER);
    }

}
