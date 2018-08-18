package io.github.lethinh.mantle.multiblock.smeltery;

import io.github.lethinh.mantle.block.GenericMachine;
import org.bukkit.block.Block;

import io.github.lethinh.mantle.multiblock.MultiBlockController;

/**
 * Created by Le Thinh
 */
public class BlockSmelteryController extends MultiBlockController<SmelteryMultiBlockTracker> {

    public BlockSmelteryController(Block block, String... players) {
        super(GenericMachine.SMELTERY_CONTROLLER, block, 81, "Smeltery", players);
        tracker = new SmelteryMultiBlockTracker(this);
    }

    @Override
    public void mimicWork() {
        // TODO Do cooking stuffs
    }

}
