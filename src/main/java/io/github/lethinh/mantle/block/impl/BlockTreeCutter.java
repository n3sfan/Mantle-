package io.github.lethinh.mantle.block.impl;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachineEnergized;
import io.github.lethinh.mantle.block.GenericMachine;
import io.github.lethinh.mantle.energy.EnergyCapacitor;
import io.github.lethinh.mantle.io.direct.CustomDataManager;
import io.github.lethinh.mantle.utils.AreaManager;
import io.github.lethinh.mantle.utils.ItemStackFactory;
import io.github.lethinh.mantle.utils.Utils;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Created by Le Thinh
 */
public class BlockTreeCutter extends BlockMachineEnergized {

    private int xExpand = 7, yExpand = 30, zExpand = 7;
    private boolean fancyRender = true;

    public BlockTreeCutter(Block block, String... players) {
        super(GenericMachine.TREE_CUTTER, block, 45, "Tree Cutter", players);

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
        inventory.setItem(39, new ItemStackFactory(new ItemStack(Material.FEATHER))
                .setLocalizedName("Fancy Render: " + fancyRender)
                .setLore("Display effects when block is broken, may be laggy")
                .build());

        setEnergyCapacitor(new EnergyCapacitor(DEFAULT_ENERGY_CAPACITY, 2000, 0));
    }

    @Override
    public void handleUpdate(Mantle plugin) {
        runnable.runTaskTimer(plugin, 60L, DEFAULT_PERIOD);
    }

    @Override
    public void work() {
        AreaManager manager = new AreaManager(block, xExpand, yExpand, zExpand, true,
                b -> !b.isEmpty() && !b.isLiquid() && !b.getLocation().equals(block.getLocation()));
        manager.scanBlocks();

        if (manager.noBlocks()) {
            return;
        }

        manager.getScannedBlocks().stream().filter(surround -> Arrays.asList(Material.LOG, Material.LOG_2, Material.LEAVES, Material.LEAVES_2).contains(surround.getType())).forEach(surround -> {
            if (fancyRender) {
                @SuppressWarnings("deprecation")
                int id = surround.getType().getId();
                block.getWorld().playEffect(surround.getLocation(), Effect.STEP_SOUND, id);
            }

            surround.getDrops().forEach(inventory::addItem);
            surround.setType(Material.AIR, Mantle.instance.getConfig().getBoolean("performance.apply_physics"));
        });
    }

    @Override
    public boolean canWork() {
        return super.canWork() && !Utils.isFull(inventory, 27);
    }

    @Override
    public CustomDataManager writeCustomData() {
        CustomDataManager manager = super.writeCustomData();
        manager.put("XExpand", xExpand);
        manager.put("YExpand", yExpand);
        manager.put("ZExpand", zExpand);
        manager.put("FancyRender", fancyRender);
        return manager;
    }

    @Override
    public void readCustomData(CustomDataManager manager) {
        super.readCustomData(manager);
        xExpand = manager.getAsNumber("XExpand").intValue();
        yExpand = manager.getAsNumber("YExpand").intValue();
        zExpand = manager.getAsNumber("ZExpand").intValue();
        fancyRender = manager.getAsBoolean("FancyRender");
    }

    /* Callbacks */
    @Override
    public boolean onInventoryInteract(ClickType clickType, InventoryAction action, SlotType slotType, ItemStack clicked, ItemStack cursor, int slot, InventoryView view, HumanEntity player) {
        if (slot < 27) {
            return false;
        }

        if (slot == 39) {
            fancyRender = !fancyRender;
            inventory.setItem(slot,
                    new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("Fancy Render: " + fancyRender)
                            .build());
            return true;
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

        return num > 100 ? 100 : Math.max(0, num);
    }

}
