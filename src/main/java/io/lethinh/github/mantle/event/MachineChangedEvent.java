package io.lethinh.github.mantle.event;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.MantleItemStacks;
import io.lethinh.github.mantle.block.BlockBlockBreaker;
import io.lethinh.github.mantle.block.BlockBlockPlacer;
import io.lethinh.github.mantle.block.BlockMachine;
import io.lethinh.github.mantle.block.BlockPlanter;
import io.lethinh.github.mantle.block.BlockTreeCutter;

/**
 * Created by Le Thinh
 */
public class MachineChangedEvent implements Listener {

	@EventHandler
	public void onBlockPlaced(BlockPlaceEvent event) {
		ItemStack heldItem = event.getItemInHand();
		Block block = event.getBlockPlaced();
		BlockMachine machine = null;

		if (heldItem.isSimilar(MantleItemStacks.TREE_CUTTER)) {
			BlockMachine.MACHINES.add(machine = new BlockTreeCutter(block));
		} else if (heldItem.isSimilar(MantleItemStacks.PLANTER)) {
			BlockMachine.MACHINES.add(machine = new BlockPlanter(block));
		} else if (heldItem.isSimilar(MantleItemStacks.BLOCK_BREAKER)) {
			BlockMachine.MACHINES.add(machine = new BlockBlockBreaker(block));
		} else if (heldItem.isSimilar(MantleItemStacks.BLOCK_PLACER)) {
			BlockMachine.MACHINES.add(machine = new BlockBlockPlacer(block));
		}

		if (machine != null) {
			machine.stoppedTick = false;

			if (machine instanceof Listener) {
				Bukkit.getServer().getPluginManager().registerEvents((Listener) machine, Mantle.instance);
			}
		}
	}

	@EventHandler
	public void onBlockBroken(BlockBreakEvent event) {
		Block block = event.getBlock();
		BlockMachine.MACHINES.removeIf(machine -> {
			if (machine.block.getLocation().equals(block.getLocation())) {
				machine.stoppedTick = true;

				if (machine.subThread != null) {
					machine.subThread.cancel();
				}

				return true;
			}

			return false;
		});
	}

	@EventHandler
	public void onBlockOpened(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		Block block = event.getClickedBlock();
		Player player = event.getPlayer();

		BlockMachine.MACHINES.stream().filter(machine -> block.getLocation().equals(machine.block.getLocation()))
				.forEach(machine -> {
					event.setCancelled(true);
					player.openInventory(machine.inventory);
				});
	}

}
