package io.lethinh.github.mantle.block;

import org.bukkit.Effect;
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
import io.lethinh.github.mantle.nbt.NBTTagCompound;
import io.lethinh.github.mantle.utils.ItemStackFactory;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Created by Le Thinh
 */
public class BlockBlockBreaker extends BlockMachine implements Listener {

	// Breaking helper thingy
	private BlockFace face = BlockFace.NORTH;
	private boolean fancyRender = true;

	public BlockBlockBreaker(Block block, String... players) {
		super(block, 45, "Block Breaker", players);

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
		inventory.setItem(42, new ItemStackFactory(new ItemStack(Material.FEATHER))
				.setLocalizedName("Fancy Render: " + fancyRender).setLore("Display effects when block is broken")
				.build());
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		runnable.runTaskTimer(plugin, DEFAULT_DELAY * 2L, DEFAULT_PERIOD);
	}

	@Override
	public void work() {
		Block surround = block.getRelative(face);

		if (surround.isEmpty() || surround.isLiquid()) {
			return;
		}

		if (fancyRender) {
			@SuppressWarnings("deprecation")
			int typeId = surround.getTypeId();
			block.getWorld().playEffect(surround.getLocation(), Effect.STEP_SOUND, typeId);
		}

		surround.getDrops().forEach(inventory::addItem);
		surround.setType(Material.AIR);
	}

	@Override
	public boolean canWork() {
		return super.canWork() && !Utils.isFull(inventory);
	}

	/* NBT */
	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = super.writeToNBT();
		nbt.setInteger("FaceIndex", face.ordinal());
		nbt.setBoolean("FancyRender", fancyRender);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		face = BlockFace.values()[nbt.getInteger("FaceIndex")];
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

		if (slot == 42) {
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
