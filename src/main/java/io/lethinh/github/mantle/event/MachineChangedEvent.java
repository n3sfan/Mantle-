package io.lethinh.github.mantle.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.block.BlockMachine;
import io.lethinh.github.mantle.block.BlockPlanter;
import io.lethinh.github.mantle.block.BlockTreeCutter;
import io.lethinh.github.mantle.loader.ItemStackLoader;

/**
 * Created by Le Thinh
 */
public class MachineChangedEvent implements Listener {

	@EventHandler
	public void onBlockPlaced(BlockPlaceEvent event) {
		ItemStack heldItem = event.getItemInHand();
		Block block = event.getBlockPlaced();
		Player player = event.getPlayer();

		if (heldItem.isSimilar(ItemStackLoader.TREE_CUTTER)) {
			BlockMachine.MACHINES.add(new BlockTreeCutter(block, player, heldItem));
		} else if (heldItem.isSimilar(ItemStackLoader.PLANTER)) {
			BlockMachine.MACHINES.add(new BlockPlanter(block, player, heldItem));
		}
	}

	@EventHandler
	public void onBlockBroken(BlockBreakEvent event) {
		Block block = event.getBlock();
		Location blockPos = block.getLocation();
		BlockMachine.MACHINES.removeIf(machine -> machine.block.getLocation().equals(blockPos));
	}

}
