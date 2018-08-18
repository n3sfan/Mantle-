package io.github.lethinh.mantle.block.impl;

import io.github.lethinh.mantle.block.GenericMachine;
import org.bukkit.block.Block;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachine;

/**
 * Created by Le Thinh
 */
public class BlockPlanter extends BlockMachine {

	public BlockPlanter(Block block, String... players) {
		super(GenericMachine.UNKNOWN, block, 9, "Planter", players);
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		// runnable.runTaskTimer(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
	}

	@Override
	public void work() {
//		Collection<Block> surroundings = Utils.getSurroundingBlocks(block, 1, 0, 1, false,
//				block -> block.isEmpty());
//
//		for (Block surround : surroundings) {
//			for (int i = 0; i < inventory.getSize(); ++i) {
//				ItemStack content = inventory.getItem(i);
//
//				if (content == null || content.getAmount() == 0) {
//					continue;
//				}
//
//				Material material = content.getType();
//
//				if (material == Material.CARROT_ITEM || material == Material.POTATO_ITEM
//						|| material == Material.SEEDS || material == Material.BEETROOT_SEEDS
//						|| material == Material.MELON_SEEDS) {
//					surround.setType(material);
//					inventory.remove(content);
//				}
//
//				content.setAmount(content.getAmount() - 1);
//				inventory.setItem(i, content);
//			}
//		}
	}

}
