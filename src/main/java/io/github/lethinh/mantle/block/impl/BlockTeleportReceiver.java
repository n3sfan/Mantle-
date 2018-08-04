package io.github.lethinh.mantle.block.impl;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.block.GenericMachine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Le Thinh
 */
public class BlockTeleportReceiver extends BlockMachine {

	public static final ConcurrentMap<ItemStack, Location> WARPS = new ConcurrentHashMap<>();
	private ItemStack lastStack = null;

	public BlockTeleportReceiver(Block block, String... players) {
		super(GenericMachine.TELEPORT_RECEIVER, block, 9, "Teleport Receiver", players);

		for (int i = 1; i < inventory.getSize(); ++i) {
			inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
		}
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
	}

	@Override
	public void work() {
		ItemStack stack = inventory.getItem(0);

		if (stack == null || stack.getAmount() == 0) {
			return;
		}

		if (lastStack != null && !lastStack.isSimilar(stack)) {
			WARPS.remove(lastStack);
		}

		if (!WARPS.containsKey(stack)) {
			WARPS.put(stack, block.getLocation());
			lastStack = stack;
		}
	}

	/* Callbacks */
	@Override
	public void onMachineBroken(Player player) {
		for (Entry<ItemStack, Location> entry : WARPS.entrySet()) {
			if (entry.getValue().equals(block.getLocation())) {
				WARPS.remove(entry.getKey());
			}
		}
	}

	@Override
	public boolean onInventoryInteract(ClickType clickType, InventoryAction action, SlotType slotType,
                                       ItemStack clicked, ItemStack cursor, int slot, InventoryView view, HumanEntity player) {
		return slot >= 1 && slot < 9;
	}

	@Override
	public int getRealSlots() {
		return 1;
	}

}
