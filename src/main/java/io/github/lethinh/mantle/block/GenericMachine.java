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

public enum GenericMachine implements MachineDefinition {

    UNKNOWN, // FIXME Necessary?
    BLOCK_BREAKER {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockBlockBreaker(block, players);
        }
    },
    BLOCK_PLACER {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockBlockPlacer(block, players);
        }
    },
    TREE_CUTTER {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockTreeCutter(block, players);
        }
    },
    TELEPORT_RECEIVER {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockTeleportReceiver(block, players);
        }
    },
    TELEPORT_TRANSMITTER {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockTeleportTransmitter(block, players);
        }
    },
    RECIPE_ATTACHER {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockRecipeAttacher(block, players);
        }
    },
    AUTO_CRAFTER {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockAutoCrafter(block, players);
        }
    },
    SMELTERY_CONTROLLER {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockSmelteryController(block, players);
        }
    },
    SMELTERY_BLOCK {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockSmelteryPart(block);
        }
    },
    ENDER_HOPPER {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockEnderHopper(block, players);
        }
    },
    ENERGY_PIPE {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockEnergyPipe(block, players);
        }
    },
    FIRST_AID_KIT {
        @Override
        public BlockMachine createBlockMachine(Block block, String... players) {
            return new BlockFirstAidKit(block, players);
        }
    };

    @Override
    public String getName() {
        return name().toLowerCase();
    }

    @Override
    public ItemStack getStackForBlock() {
        for (ItemStack stack : MantleItemStacks.STACKS) {
            if (!stack.getType().isBlock()) {
                continue;
            }

            String locName = stack.getItemMeta().getLocalizedName().replace(Mantle.PLUGIN_ID + "_", "");

            if (locName.equalsIgnoreCase(getName())) {
                return stack;
            }
        }

        return null;
    }

    @Override
    public BlockMachine createBlockMachine(Block block, String... players) {
        throw new AbstractMethodError();
    }

}
