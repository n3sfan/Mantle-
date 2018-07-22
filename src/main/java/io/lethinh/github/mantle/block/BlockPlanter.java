package io.lethinh.github.mantle.block;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Created by Le Thinh
 */
public class BlockPlanter extends BlockMachine {

	public BlockPlanter(Block block) {
		super(block, 9, "Planter");
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		(subThread = new BukkitRunnable() {
			@Override
			public void run() {
				Collection<Block> surroundings = Utils.getSurroundingBlocks(block, 1, 0, 1, false,
						block -> block.isEmpty());

				for (Block surround : surroundings) {
					for (int i = 0; i < inventory.getSize(); ++i) {
						ItemStack content = inventory.getItem(i);

						if (content == null || content.getAmount() == 0) {
							continue;
						}

						Material material = content.getType();

						if (material == Material.CARROT || material == Material.SEEDS
								|| material == Material.BEETROOT_SEEDS || material == Material.MELON_SEEDS
								|| material == Material.MELON_SEEDS) {
							surround.setType(material);
							inventory.remove(content);
						}

						content.setAmount(content.getAmount() - 1);
						inventory.setItem(i, content);
					}
				}
			}
		}).runTaskTimer(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
	}

}
