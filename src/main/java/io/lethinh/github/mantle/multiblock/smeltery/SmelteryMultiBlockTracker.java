package io.lethinh.github.mantle.multiblock.smeltery;

import org.bukkit.Location;
import org.bukkit.block.Block;

import io.lethinh.github.mantle.MantleItemStacks;
import io.lethinh.github.mantle.multiblock.MultiBlockController;
import io.lethinh.github.mantle.multiblock.MultiBlockTracker;
import io.lethinh.github.mantle.utils.Utils;

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
		return block != null && (Utils.isBlockEqualStack(block, MantleItemStacks.SMELTERY_BLOCK)
				|| Utils.isBlockEqualStack(block, MantleItemStacks.SMELTERY_CONTROLLER));
	}

}
