package io.github.lethinh.mantle.block.impl;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachineEnergized;
import io.github.lethinh.mantle.block.GenericMachine;
import io.github.lethinh.mantle.energy.EnergyCapacitor;
import io.github.lethinh.mantle.io.direct.CustomDataManager;
import io.github.lethinh.mantle.utils.ItemStackFactory;
import io.github.lethinh.mantle.utils.Utils;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class BlockFirstAidKit extends BlockMachineEnergized {
    private int rad = 3;
    private boolean eff = true;
    private double amount = 0;

    public BlockFirstAidKit(Block block, String[] players) {
        super(GenericMachine.FIRST_AID_KIT, block, 18, "First Aid Kit", players);

        inventory.setItem(0, new ItemStackFactory(new ItemStack(Material.LAVA_BUCKET, 1))
                .setLocalizedName("Amount: " + amount)
                .build());
        inventory.setItem(1, new ItemStackFactory(new ItemStack(Material.WATCH, 1))
                .setLocalizedName("Radius: " + rad).setLore("Left click to increase, right click to decrease")
                .build());
        inventory.setItem(2, new ItemStackFactory(new ItemStack(Material.FEATHER))
                .setLocalizedName("Effects: " + eff)
                .setLore("Display effects while healing players")
                .build());
        for(int i = 3; i < 9; i++){
            inventory.setItem(i, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8))
                    .build());
        }

        for(int i = 9; i < 18; i++){
            inventory.setItem(i, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5))
                    .setLocalizedName("Put your food here to contribute")
                    .build());
        }

        setEnergyCapacitor(new EnergyCapacitor(DEFAULT_ENERGY_CAPACITY, 500, 0));
    }

    @Override
    public void handleUpdate(Mantle plugin) {
        runnable.runTaskTimerAsynchronously(plugin, 0L, DEFAULT_PERIOD);
    }

    @Override
    public void work() {
        List<Player> players = block.getWorld().getNearbyEntities(block.getLocation(), rad, rad, rad).stream().filter(entity -> entity instanceof Player).map(entity -> (Player) entity).collect(Collectors.toList());
        for(Player p : players){
            double ch = p.getHealth();
            double mh = p.getMaxHealth();
            if(ch == mh){
                continue;
            }
            double nh = mh-ch;
            if(amount >= nh){
                amount -= nh;
                p.setHealth(mh);
            } else {
                amount = 0;
                p.setHealth(amount);
            }
            if(eff){
                p.getWorld().playEffect(p.getLocation(), Effect.HAPPY_VILLAGER, 0, 5);
            }
        }
        inventory.setItem(0,
                new ItemStackFactory(inventory.getItem(0)).setLocalizedName("Amount: " + amount)
                        .build());
    }

    @Override
    public boolean canWork() {
        return super.canWork() && !Utils.isFull(inventory, 18);
    }

    @Override
    public CustomDataManager writeCustomData() {
        CustomDataManager manager = new CustomDataManager();
        manager.put("Radius", rad);
        manager.put("Effects", eff);
        manager.put("Amount", amount);
        return manager;
    }

    @Override
    public void readCustomData(CustomDataManager manager) {
        rad = manager.getAsNumber("Radius").intValue();
        amount = manager.getAsNumber("Amount").doubleValue();
        eff = manager.getAsBoolean("Effects");
    }

    /* Callbacks */
    @Override
    public boolean onInventoryInteract(ClickType clickType, InventoryAction action, InventoryType.SlotType slotType,
                                       ItemStack clicked, ItemStack cursor, int slot, InventoryView view, HumanEntity player) {
        if (slot == 0 || (slot >= 3 && slot < 9)) {
            return true;
        }
        if (slot == 1) {
            rad = parse0(rad, clickType);
            inventory.setItem(slot,
                    new ItemStackFactory(inventory.getItem(slot))
                            .setLocalizedName("Radius: " + rad)
                            .build());
            return true;
        }
        if (slot == 2) {
            eff = !eff;
            inventory.setItem(slot,
                    new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("Effects: " + eff)
                            .build());
            return true;
        }
        if (9 <= slot && slot < 18) {
            if(cursor != null && cursor.getType() != Material.AIR){
                switch(cursor.getType()){
                    case ROTTEN_FLESH:
                        amount += 0.5d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case APPLE:
                    case CHORUS_FRUIT:
                        amount += 4d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case RABBIT_STEW:
                        amount += 8d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case BAKED_POTATO:
                    case BREAD:
                    case COOKED_FISH:
                    case COOKED_RABBIT:
                    case SPIDER_EYE:
                        amount += 5d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case BEETROOT:
                    case RAW_FISH:
                    case MELON_STEM:
                    case POTATO_ITEM:
                        amount += 1d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case BEETROOT_SOUP:
                    case COOKED_CHICKEN:
                    case COOKED_MUTTON:
                    case MUSHROOM_SOUP:
                        amount += 6d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case CAKE:
                        amount += 12d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case GOLDEN_APPLE:
                        amount += 10d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case GOLDEN_CARROT:
                        amount += 10d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case CARROT:
                    case RAW_BEEF:
                    case PORK:
                        amount += 3d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case COOKIE:
                    case RAW_CHICKEN:
                    case MUTTON:
                        amount += 2d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                    case GRILLED_PORK:
                    case PUMPKIN_PIE:
                    case COOKED_BEEF:
                        amount += 8d / 5d * cursor.getAmount();
                        player.setItemOnCursor(null);
                        break;
                }
                inventory.setItem(0,
                        new ItemStackFactory(inventory.getItem(0)).setLocalizedName("Amount: " + amount)
                                .build());
            }
            return true;
        }
        return false;
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
