package io.github.lethinh.mantle.block.impl;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachineEnergized;
import io.github.lethinh.mantle.block.GenericMachine;
import io.github.lethinh.mantle.energy.EnergyCapacitor;
import io.github.lethinh.mantle.utils.ItemStackFactory;
import io.github.lethinh.mantle.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Le Thinh
 */
public class BlockRecipeAttacher extends BlockMachineEnergized {

    private static final short INPUT_SLOT = 0;
    private static final short TO_ATTACH_SLOT = 18;

    public BlockRecipeAttacher(Block block, String... players) {
        super(GenericMachine.RECIPE_ATTACHER, block, 27, "Recipe Attacher", players);

        // Inventory
        for (int i = INPUT_SLOT + 1; i < TO_ATTACH_SLOT; ++i) {
            inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
        }

        for (int i = TO_ATTACH_SLOT + 1; i < TO_ATTACH_SLOT + 9; ++i) {
            inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
        }

        setEnergyCapacitor(new EnergyCapacitor(DEFAULT_ENERGY_CAPACITY, 200, 0));
    }

    @Override
    public void handleUpdate(Mantle plugin) {
        runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
    }

    @Override
    public void work() {
        ItemStack input = inventory.getItem(INPUT_SLOT);
        ItemStack toAttach = inventory.getItem(TO_ATTACH_SLOT);

        if (input == null || toAttach == null || toAttach.getType() != Material.PAPER) {
            return;
        }

        List<Recipe> recipes = Bukkit.getRecipesFor(input).stream().filter(recipe -> recipe instanceof Keyed)
                .collect(Collectors.toList());

        if (recipes.isEmpty()) {
            return;
        }

        ItemStack plan = new ItemStackFactory(toAttach)
                .setDisplayName(Utils.encryptBase64(((Keyed) recipes.get(0)).getKey().toString())).build();
        inventory.setItem(TO_ATTACH_SLOT, plan);
    }

    @Override
    public void dropItems(ItemStack stack) {
        World world = block.getWorld();
        Location location = block.getLocation();
        world.dropItemNaturally(location, stack);

        ItemStack input = inventory.getItem(INPUT_SLOT);
        ItemStack plan = inventory.getItem(TO_ATTACH_SLOT);

        if (input != null) {
            world.dropItemNaturally(location, input);
        }

        if (plan != null) {
            world.dropItemNaturally(location, plan);
        }
    }

    /* Callbacks */
    @Override
    public boolean onInventoryInteract(ClickType clickType, InventoryAction action, SlotType slotType, ItemStack clicked, ItemStack cursor, int slot, InventoryView view, HumanEntity player) {
        if (clicked == null || clicked.getAmount() == 0 || Material.STAINED_GLASS_PANE != clicked.getType()) {
            return false;
        }

        return slot > INPUT_SLOT && slot < TO_ATTACH_SLOT || slot > TO_ATTACH_SLOT && slot < TO_ATTACH_SLOT + 9;
    }

//	private int parse0(int num, ClickType clickType) {
//		if (clickType == ClickType.LEFT) {
//			++num;
//		} else if (clickType == ClickType.SHIFT_LEFT) {
//			num += 10;
//		} else if (clickType == ClickType.RIGHT) {
//			--num;
//		} else if (clickType == ClickType.SHIFT_RIGHT) {
//			num -= 10;
//		}
//
//		return num > maxChoice ? maxChoice : Math.max(0, num);
//	}

}
