package io.lethinh.github.mantle.block;

import java.util.Collection;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.nbt.NBTTagCompound;
import io.lethinh.github.mantle.utils.ItemStackFactory;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Created by Le Thinh
 */
public class BlockTreeCutter extends BlockMachine implements Listener {

	private int xExpand = 7, yExpand = 30, zExpand = 7;
	private boolean fancyRender = true;

	public BlockTreeCutter(Block block) {
		super(block, 45, "Tree Cutter");

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
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		runnable.runTaskTimer(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
	}

	@Override
	public void work() {
		Collection<Block> surroundings = Utils.getSurroundingBlocks(block, xExpand, yExpand, zExpand, true,
				b -> !b.getLocation().equals(block.getLocation()));

		if (surroundings.isEmpty()) {
			return;
		}

		for (Block surround : surroundings) {
			Material material = surround.getType();

			if (material.equals(Material.LOG) || material.equals(Material.LOG_2)
					|| material.equals(Material.LEAVES) || material.equals(Material.LEAVES_2)) {
				if (fancyRender) {
					@SuppressWarnings("deprecation")
					int id = material.getId();
					block.getWorld().playEffect(surround.getLocation(), Effect.STEP_SOUND, id);
				}

				surround.getDrops().forEach(inventory::addItem);
				surround.setType(Material.AIR);
			}
		}
	}

	@Override
	public boolean canWork() {
		return super.canWork() && !Utils.isFull(inventory);
	}

	/* NBT */
	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = super.writeToNBT();
		nbt.setInteger("XExpand", xExpand);
		nbt.setInteger("YExpand", yExpand);
		nbt.setInteger("ZExpand", zExpand);
		nbt.setBoolean("FancyRender", fancyRender);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		xExpand = nbt.getInteger("XExpand");
		yExpand = nbt.getInteger("YExpand");
		zExpand = nbt.getInteger("ZExpand");
		fancyRender = nbt.getBoolean("FancyRender");
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
		int slot = event.getSlot();

		if (!this.inventory.getName().equals(inventory.getName())) {
			return;
		}

		if (!block.getLocation().equals(interactPos)) {
			return;
		}

		if (event.getSlot() < 27) {
			return;
		}

		if (slot == 39) {
			fancyRender = !fancyRender;
			event.setCancelled(true);
			inventory.setItem(slot,
					new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("Fancy Render: " + fancyRender)
							.build());
		}

		ItemStack curStack = event.getCurrentItem();

		if (curStack == null || curStack.getAmount() == 0 || Material.STAINED_GLASS_PANE != curStack.getType()) {
			return;
		}

		ClickType clickType = event.getClick();

		switch (curStack.getDurability()) {
		case 1:
			event.setCancelled(true);
			break;
		case 2:
			xExpand = parse0(xExpand, clickType);
			inventory.setItem(slot,
					new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("X Expand: " + xExpand)
							.build());
			event.setCancelled(true);
			break;
		case 3:
			yExpand = parse0(yExpand, clickType);
			inventory.setItem(slot,
					new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("Y Expand: " + yExpand)
							.build());
			event.setCancelled(true);
			break;
		case 4:
			zExpand = parse0(zExpand, clickType);
			inventory.setItem(slot,
					new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("Z Expand: " + zExpand)
							.build());
			event.setCancelled(true);
			break;
		default:
			break;
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

		return Math.max(0, num);
	}

}
