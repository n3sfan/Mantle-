/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.block.impl;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.block.GenericMachine;
import io.github.lethinh.mantle.io.direct.CustomDataManager;
import io.github.lethinh.mantle.utils.ItemStackFactory;
import io.github.lethinh.mantle.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class BlockEnderHopper extends BlockMachine {

    private int xExpand = 5, yExpand = 5, zExpand = 5;

    public BlockEnderHopper(Block block, String... players) {
        super(GenericMachine.ENDER_HOPPER, block, 45, "Ender Hopper", players);

        // Inventory
        for (int i = 27; i < 36; ++i) {
            inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1));
        }

        inventory.setItem(36, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 2))
                .setLocalizedName("X Expand: " + xExpand).setLore("Left click to increase, right click to decrease")
                .build());
        inventory.setItem(37, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3))
                .setLocalizedName("Y Expand: " + yExpand).setLore("Left click to increase, right click to decrease")
                .build());
        inventory.setItem(38, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4))
                .setLocalizedName("Z Expand: " + zExpand).setLore("Left click to increase, right click to decrease")
                .build());
    }

    @Override
    public void handleUpdate(Mantle plugin) {
        runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
    }

    @Override
    public void work() {
        World world = block.getWorld();
        Location location = block.getLocation();

        world.getNearbyEntities(location, xExpand, yExpand, zExpand).stream().filter(entity -> entity instanceof Item && entity.isValid()).map(entity -> (Item) entity).forEach(item -> {
            item.teleport(location);
            ItemStack stack = item.getItemStack();
            inventory.addItem(stack);
            item.remove();
        });
    }

    @Override
    public boolean canWork() {
        return super.canWork() && !Utils.isFull(inventory, 27);
    }

    @Override
    public CustomDataManager writeCustomData() {
        CustomDataManager manager = new CustomDataManager();
        manager.put("XExpand", xExpand);
        manager.put("YExpand", yExpand);
        manager.put("ZExpand", zExpand);
        return manager;
    }

    @Override
    public void readCustomData(CustomDataManager manager) {
        xExpand = manager.getAsNumber("XExpand").intValue();
        yExpand = manager.getAsNumber("YExpand").intValue();
        zExpand = manager.getAsNumber("ZExpand").intValue();
    }

    /* Callbacks */
    @Override
    public boolean onInventoryInteract(ClickType clickType, InventoryAction action, InventoryType.SlotType slotType, ItemStack clicked, ItemStack cursor, int slot, InventoryView view, HumanEntity player) {
        if (slot < 27) {
            return false;
        }

        if (clicked == null || clicked.getAmount() == 0 || Material.STAINED_GLASS_PANE != clicked.getType()) {
            return false;
        }

        switch (clicked.getDurability()) {
            case 1:
                return true;
            case 2:
                xExpand = parse0(xExpand, clickType);
                inventory.setItem(slot,
                        new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("X Expand: " + xExpand)
                                .build());
                return true;
            case 3:
                yExpand = parse0(yExpand, clickType);
                inventory.setItem(slot,
                        new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("Y Expand: " + yExpand)
                                .build());
                return true;
            case 4:
                zExpand = parse0(zExpand, clickType);
                inventory.setItem(slot,
                        new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("Z Expand: " + zExpand)
                                .build());
                return true;
            default:
                return false;
        }
    }

    private static int parse0(int num, ClickType clickType) {
        if (clickType == ClickType.LEFT) {
            ++num;
        } else if (clickType == ClickType.SHIFT_LEFT) {
            num += 10;
        } else if (clickType == ClickType.RIGHT) {
            --num;
        } else if (clickType == ClickType.SHIFT_RIGHT) {
            num -= 10;
        }

        return num > 20 ? 20 : Math.max(0, num);
    }

}
