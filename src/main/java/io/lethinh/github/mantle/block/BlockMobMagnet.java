package io.lethinh.github.mantle.block;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.nbt.NBTTagCompound;
import io.lethinh.github.mantle.utils.ItemStackFactory;

/**
 * Created by Le Thinh
 */
public class BlockMobMagnet extends BlockMachine {

	private int xExpand = 7, yExpand = 30, zExpand = 7;

	public BlockMobMagnet(Block block) {
		super(block, 9, "Mob Magnet");

		// Inventory
		inventory.setItem(0, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1))
				.setLocalizedName("X Expand: " + xExpand).setLore("Left click to increase, right click to decrease")
				.build());
		inventory.setItem(1, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 2))
				.setLocalizedName("Y Expand: " + yExpand).setLore("Left click to increase, right click to decrease")
				.build());
		inventory.setItem(2, new ItemStackFactory(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3))
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
		Location blockPos = block.getLocation();
		Collection<Entity> entities = world.getNearbyEntities(blockPos, xExpand, yExpand, zExpand);

		if (entities.isEmpty()) {
			return;
		}

		for (Entity entity : entities) {
			if (!(entity instanceof Monster) || entity.isDead() || entity.getLocation().equals(blockPos)) {
				continue;
			}

			entity.setVelocity(blockPos.subtract(entity.getLocation()).toVector());
		}
	}

	@Override
	public int getRealSlots() {
		return 0;
	}

	/* NBT */
	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = super.writeToNBT();
		nbt.setInteger("XExpand", xExpand);
		nbt.setInteger("YExpand", yExpand);
		nbt.setInteger("ZExpand", zExpand);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		xExpand = nbt.getInteger("XExpand");
		yExpand = nbt.getInteger("YExpand");
		zExpand = nbt.getInteger("ZExpand");
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

		ItemStack curStack = event.getCurrentItem();

		if (curStack == null || curStack.getAmount() == 0 || Material.STAINED_GLASS_PANE != curStack.getType()) {
			return;
		}

		ClickType clickType = event.getClick();

		switch (curStack.getDurability()) {
		case 1:
			xExpand = parse0(xExpand, clickType);
			inventory.setItem(slot,
					new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("X Expand: " + xExpand)
							.build());
			event.setCancelled(true);
			break;
		case 2:
			yExpand = parse0(yExpand, clickType);
			inventory.setItem(slot,
					new ItemStackFactory(inventory.getItem(slot)).setLocalizedName("Y Expand: " + yExpand)
							.build());
			event.setCancelled(true);
			break;
		case 3:
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
