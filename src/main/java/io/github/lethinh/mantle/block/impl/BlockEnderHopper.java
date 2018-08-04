/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.block.impl;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.block.GenericMachine;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class BlockEnderHopper extends BlockMachine {

    public BlockEnderHopper(Block block, String... players) {
        super(GenericMachine.ENDER_HOPPER, block, 27, "Ender Hopper", players);
    }

    @Override
    public void handleUpdate(Mantle plugin) {
        runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
    }

    @Override
    public void work() {
        World world = block.getWorld();
        Location location = block.getLocation();

        world.getNearbyEntities(location, 5D, 5D, 5D).stream().filter(entity -> entity instanceof Item && entity.isValid()).map(entity -> (Item) entity).forEach(item -> {
            item.teleport(location);
            ItemStack stack = item.getItemStack();
            inventory.addItem(stack);
            item.remove();
        });
    }


}
