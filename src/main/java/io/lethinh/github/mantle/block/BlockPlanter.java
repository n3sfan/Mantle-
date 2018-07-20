package io.lethinh.github.mantle.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import io.lethinh.github.mantle.Mantle;

/**
 * Created by Le Thinh
 */
public class BlockPlanter extends BlockMachine {

	public BlockPlanter(Block block, Player player, ItemStack heldItem) {
		super(block, player, heldItem);
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		Location blockPos = block.getLocation();
		BlockIterator iterator = new BlockIterator(blockPos, 0, 7);

		iterator.forEachRemaining(neighborBlock -> {
			if (!neighborBlock.isEmpty() || neighborBlock.getLocation().equals(blockPos)) {
				return;
			}

			Dispenser dispenser = (Dispenser) block.getState();
			Inventory inventory = dispenser.getInventory();

			for (ItemStack content : inventory.getContents()) {
				Material material = content.getType();

				if (material.equals(Material.CARROT) || material.equals(Material.SEEDS)
						|| material.equals(Material.BEETROOT_SEEDS) || material.equals(Material.MELON_SEEDS)
						|| material.equals(Material.MELON_SEEDS)) {
					inventory.remove(content);
					neighborBlock.setType(material);
				}
			}
		});
	}

}
