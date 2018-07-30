package io.lethinh.github.mantle.block.impl;

import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.block.BlockMachine;

/**
 * Created by Le Thinh
 */
public class BlockTeleportTransmitter extends BlockMachine {

	public BlockTeleportTransmitter(Block block, String... players) {
		super(block, 9, "Teleport Transmitter", players);

		for (int i = 1; i < inventory.getSize(); ++i) {
			inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
		}
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY + 10L, DEFAULT_PERIOD);
	}

	@Override
	public void work() {
		ItemStack stack = inventory.getItem(0);

		if (stack == null || stack.getAmount() == 0 || BlockTeleportReceiver.WARPS.isEmpty()) {
			return;
		}

		Queue<Player> queue = new ArrayBlockingQueue<>(4); // Max is 4 players per teleport and who stand on the
															// transmitter first will be teleported first
		Location dst = null;

		for (Entry<ItemStack, Location> entry : BlockTeleportReceiver.WARPS.entrySet()) {
			ItemStack toStack = entry.getKey();

			if (toStack == null) {
				continue;
			}

			Location toLocation = entry.getValue();

			if (!toStack.equals(stack)) {
				continue;
			}

			dst = toLocation;
			block.getWorld().getNearbyEntities(block.getLocation(), 1D, 1D, 1D).stream()
					.filter(e -> !e.isDead() && e instanceof Player)
					.forEach(e -> queue.offer((Player) e));
		}

		while (dst != null && !queue.isEmpty()) {
			Player player = queue.poll();
			player.teleport(dst, TeleportCause.PLUGIN);
			player.setFallDistance(0F);
		}
	}

	/* Callbacks */
	@Override
	public boolean onInventoryInteract(ClickType clickType, InventoryAction action, SlotType slotType,
			ItemStack clicked, ItemStack cursor, int slot, InventoryView view) {
		return slot >= 1 && slot < 9;
	}

	@Override
	public int getRealSlots() {
		return 1;
	}

}
