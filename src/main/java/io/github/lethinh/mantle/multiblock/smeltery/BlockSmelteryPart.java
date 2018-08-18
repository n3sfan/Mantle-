package io.github.lethinh.mantle.multiblock.smeltery;

import io.github.lethinh.mantle.block.GenericMachine;
import org.bukkit.block.Block;

import io.github.lethinh.mantle.multiblock.MultiBlockPart;

/**
 * Created by Le Thinh
 */
public class BlockSmelteryPart extends MultiBlockPart<SmelteryMultiBlockTracker> {

    public BlockSmelteryPart(Block block) {
        super(GenericMachine.SMELTERY_BLOCK, block);
    }

}
