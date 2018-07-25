package io.lethinh.github.mantle.block.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.block.BlockMachine;
import io.lethinh.github.mantle.nbt.NBTTagCompound;
import io.lethinh.github.mantle.utils.ItemStackFactory;

/**
 * Created by Le Thinh
 */
public class BlockBlockPlacer extends BlockMachine implements Listener {

	// Placing helper thingy
	private BlockFace face = BlockFace.NORTH;

	public BlockBlockPlacer(Block block, String... players) {
		super(block, 45, "Block Placer", players);

		// Inventory
		for (int i = 27; i < 36; ++i) {
			inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1));
		}

		inventory.setItem(36, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 2))
				.setLocalizedName("North").build());
		inventory.setItem(37, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3))
				.setLocalizedName("South").build());
		inventory.setItem(38, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4))
				.setLocalizedName("East").build());
		inventory.setItem(39, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5))
				.setLocalizedName("West").build());
		inventory.setItem(40, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 6))
				.setLocalizedName("Up").build());
		inventory.setItem(41, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7))
				.setLocalizedName("Down").build());
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		runnable.runTaskTimer(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
	}

	@Override
	public void work() {
		Block surround = block.getRelative(face);

		for (int i = 0; i < getRealSlots(); ++i) {
			ItemStack content = inventory.getItem(i);

			if (!surround.isEmpty() || content == null || content.getAmount() == 0
					|| !content.getType().isBlock()) {
				continue;
			}

			surround.setType(content.getType());
			content.setAmount(content.getAmount() - 1);
			inventory.setItem(i, content);
		}
	}

	/* NBT */
	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = super.writeToNBT();
		nbt.setInteger("FaceIndex", face.ordinal());
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		face = BlockFace.values()[nbt.getInteger("FaceIndex")];
	}

	/* Event */
	private Location interactPos;

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockOpened(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Block block = event.getClickedBlock();

		BlockMachine.MACHINES.stream().filter(machine -> block.getLocation().equals(machine.block.getLocation()))
				.forEach(machine -> interactPos = block.getLocation());
	}

	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();

		if (!this.inventory.getName().equals(inventory.getName())) {
			return;
		}

		if (!block.getLocation().equals(interactPos)) {
			return;
		}

		if (event.getSlot() < 27) {
			return;
		}

		ItemStack curStack = event.getCurrentItem();

		if (curStack == null || curStack.getAmount() == 0 || Material.STAINED_GLASS_PANE != curStack.getType()) {
			return;
		}

		switch (curStack.getDurability()) {
		case 1:
			event.setCancelled(true);
			break;
		case 2:
			face = BlockFace.NORTH;
			event.setCancelled(true);
			break;
		case 3:
			face = BlockFace.SOUTH;
			event.setCancelled(true);
			break;
		case 4:
			face = BlockFace.EAST;
			event.setCancelled(true);
			break;
		case 5:
			face = BlockFace.WEST;
			event.setCancelled(true);
			break;
		case 6:
			face = BlockFace.UP;
			event.setCancelled(true);
			break;
		case 7:
			face = BlockFace.DOWN;
			event.setCancelled(true);
			break;
		default:
			break;
		}
	}

}
