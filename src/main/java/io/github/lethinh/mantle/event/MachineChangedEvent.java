package io.github.lethinh.mantle.event;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.MantleItemStacks;
import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.block.impl.*;
import io.github.lethinh.mantle.multiblock.smeltery.BlockSmelteryController;
import io.github.lethinh.mantle.multiblock.smeltery.BlockSmelteryPart;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Le Thinh
 */
public class MachineChangedEvent implements Listener {

	@EventHandler
	public void onBlockPlaced(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		ItemStack heldItem = event.getItemInHand();
		// ItemStack copy = heldItem.clone();
		Block block = event.getBlockPlaced();
		BlockMachine machine = null;

		if (heldItem.isSimilar(MantleItemStacks.TREE_CUTTER)) {
			BlockMachine.MACHINES.add(machine = new BlockTreeCutter(block, name));
		} /*
			 * else if (heldItem.isSimilar(MantleItemStacks.PLANTER)) {
			 * BlockMachine.MACHINES.add(machine = new BlockPlanter(block)); }
			 */
		else if (heldItem.isSimilar(MantleItemStacks.BLOCK_BREAKER)) {
			BlockMachine.MACHINES.add(machine = new BlockBlockBreaker(block, name));
		} else if (heldItem.isSimilar(MantleItemStacks.BLOCK_PLACER)) {
			BlockMachine.MACHINES.add(machine = new BlockBlockPlacer(block, name));
		} else if (heldItem.isSimilar(MantleItemStacks.MOB_MAGNET)) {
			BlockMachine.MACHINES.add(machine = new BlockMobMagnet(block, name));
		} else if (heldItem.isSimilar(MantleItemStacks.SMELTERY_BLOCK)) {
			BlockMachine.MACHINES.add(machine = new BlockSmelteryPart(block));
		} else if (heldItem.isSimilar(MantleItemStacks.SMELTERY_CONTROLLER)) {
			BlockMachine.MACHINES.add(machine = new BlockSmelteryController(block, name));
		} else if (heldItem.isSimilar(MantleItemStacks.TELEPORT_RECEIVER)) {
			BlockMachine.MACHINES.add(machine = new BlockTeleportReceiver(block, name));
		} else if (heldItem.isSimilar(MantleItemStacks.TELEPORT_TRANSMITTER)) {
			BlockMachine.MACHINES.add(machine = new BlockTeleportTransmitter(block, name));
		} else if (heldItem.isSimilar(MantleItemStacks.RECIPE_ATTACHER)) {
			BlockMachine.MACHINES.add(machine = new BlockRecipeAttacher(block, name));
		} else if (heldItem.isSimilar(MantleItemStacks.AUTO_CRAFTER)) {
			BlockMachine.MACHINES.add(machine = new BlockAutoCrafter(block, name));
		}

		if (machine != null) {
			machine.onMachinePlaced(player, heldItem);
		}
	}

	@EventHandler
	public void onBlockBroken(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();

		for (BlockMachine machine : BlockMachine.MACHINES) {
			if (!machine.block.getLocation().equals(block.getLocation())) {
				continue;
			}

			if (!machine.canBreak(player)) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You cannot break this machine because you it is locked!");
				continue;
			}

			machine.setTickStopped(true);

			if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				event.setDropItems(false);

				for (ItemStack stack : MantleItemStacks.STACKS) {
					if (stack.getItemMeta().getLocalizedName().replace(Mantle.PLUGIN_ID + "_", "")
							.equalsIgnoreCase(machine.machineType.getName())) {
						machine.dropItems(stack);
					}
				}
			}

			machine.onMachineBroken(player);
			BlockMachine.MACHINES.remove(machine);
		}
	}

	private final AtomicReference<Location> interactPos = new AtomicReference<>(); // Concurrent

	@EventHandler
	public void onBlockOpened(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (player.isSneaking() || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		Block block = event.getClickedBlock();

		BlockMachine.MACHINES.stream().filter(machine -> block.getLocation().equals(machine.block.getLocation()))
				.forEach(machine -> {
					if (machine.canOpen(player)) {
						if (machine.inventory.getSize() > 0) {
							event.setCancelled(true);
							player.openInventory(machine.inventory);
							interactPos.set(machine.block.getLocation());
						}
					} else {
						player.sendMessage(ChatColor.RED + "You cannot open this machine because it is locked!");
					}
				});
	}

	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		ClickType clickType = event.getClick();
		InventoryAction action = event.getAction();
		SlotType slotType = event.getSlotType();
		ItemStack clicked = event.getCurrentItem();
		ItemStack cursor = event.getCursor();
		int slot = event.getSlot();
		InventoryView view = event.getView();

		for (BlockMachine machine : BlockMachine.MACHINES) {
			if (!machine.inventory.getName().equals(inventory.getName())
					|| !machine.block.getLocation().equals(interactPos.get())) {
				continue;
			}

			boolean cancel = machine.onInventoryInteract(clickType, action, slotType, clicked, cursor, slot, view);

			if (cancel) {
				event.setCancelled(true);
			}
		}
	}

}
