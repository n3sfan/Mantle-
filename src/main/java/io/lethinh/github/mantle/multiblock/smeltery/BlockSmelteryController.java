package io.lethinh.github.mantle.multiblock.smeltery;

import org.bukkit.block.Block;

import io.lethinh.github.mantle.multiblock.MultiBlockController;

/**
 * Created by Le Thinh
 */
public class BlockSmelteryController extends MultiBlockController<SmelteryMultiBlockTracker> {

	public BlockSmelteryController(Block block, String... players) {
		super(block, 81, "Smeltery", players);
		tracker = new SmelteryMultiBlockTracker(this);
	}

	@Override
	public void mimicWork() {
		// TODO Do cooking stuffs
	}

}
