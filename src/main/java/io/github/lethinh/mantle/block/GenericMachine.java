/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.block;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.MantleItemStacks;
import io.github.lethinh.mantle.block.impl.*;
import io.github.lethinh.mantle.multiblock.smeltery.BlockSmelteryController;
import io.github.lethinh.mantle.multiblock.smeltery.BlockSmelteryPart;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public enum GenericMachine {

    UNKNOWN(""),
    BLOCK_BREAKER("block_breaker") {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockBlockBreaker(block, players);
        }
    },
    BLOCK_PLACER("block_placer") {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockBlockPlacer(block, players);
        }
    },
    TREE_CUTTER("tree_cutter") {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockTreeCutter(block, players);
        }
    },
    TELEPORT_RECEIVER("teleport_receiver") {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockTeleportReceiver(block, players);
        }
    },
    TELEPORT_TRANSMITTER("teleport_transmitter") {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockTeleportTransmitter(block, players);
        }
    },
    RECIPE_ATTACHER("recipe_attacher") {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockRecipeAttacher(block, players);
        }
    },
    AUTO_CRAFTER("auto_crafter") {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockAutoCrafter(block, players);
        }
    },
    SMELTERY_CONTROLLER("smeltery_controller") {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockSmelteryController(block, players);
        }
    },
    SMELTERY_BLOCK("smeltery_block") {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockSmelteryPart(block);
        }
    },
    ENDER_HOPPER("ender_hopper") {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockEnderHopper(block, players);
        }
    };

    private final String name;

    GenericMachine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ItemStack getStackForBlock() {
        for (ItemStack stack : MantleItemStacks.STACKS) {
            if (!stack.getType().isBlock()) {
                continue;
            }

            String locName = stack.getItemMeta().getLocalizedName().replace(Mantle.PLUGIN_ID + "_", "");

            if (locName.equalsIgnoreCase(name)) {
                return stack;
            }
        }

        return null;
    }

    public BlockMachine createBlockMachine(Block block, String... players) {
        throw new UnsupportedOperationException();
    }

}
