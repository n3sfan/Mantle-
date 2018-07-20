package io.lethinh.github.mantle.block;

import java.util.Collection;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import io.lethinh.github.mantle.Mantle;

/**
 * Created by Le Thinh
 */
public class BlockTreeCutter extends BlockMachine {

	public BlockTreeCutter(Block block, Player player, ItemStack heldItem) {
		super(block, player, heldItem);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void handleUpdate(Mantle plugin) {
		Location blockPos = block.getLocation();
		BlockIterator iterator = new BlockIterator(blockPos, 0, 7);

		iterator.forEachRemaining(neighborBlock -> {
			if (neighborBlock.isEmpty() || neighborBlock.isLiquid()
					|| neighborBlock.getLocation().equals(blockPos)) {
				return;
			}

			Material material = neighborBlock.getType();

			if (material.isBurnable()) {
				block.getWorld().playEffect(blockPos, Effect.STEP_SOUND, material.getId());
				neighborBlock.setType(Material.AIR);

				Dispenser dispenser = (Dispenser) block.getState();
				Collection<ItemStack> unsignedDrops = neighborBlock.getDrops();
				ItemStack[] drops = new ItemStack[unsignedDrops.size()];
				unsignedDrops.toArray(drops);

				dispenser.getInventory().addItem(drops);
			}
		});
	}

}
