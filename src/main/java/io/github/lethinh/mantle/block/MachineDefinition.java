/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.block;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Define a machine. Uses for creating {@link BlockMachine}
 */
public interface MachineDefinition {

    String getName();

    ItemStack getStackForBlock();

    BlockMachine createBlockMachine(Block block, String... players);

}
