package io.lethinh.github.mantle.block;

import java.util.Collection;

import org.bukkit.Location;
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
				Location blockPos = block.getLocation();
				Collection<Block> surroundings = Utils.getSurroundingBlocks(block, 3, true);

				for (Block surround : surroundings) {
					if (!surround.isEmpty() || surround.getLocation().equals(blockPos)) {
						continue;
					}

					ItemStack content = inventory.getItem(0);

					if (content == null || content.getAmount() == 0) {
						continue;
					}

					Material material = content.getType();

					if (material.equals(Material.CARROT) || material.equals(Material.SEEDS)
							|| material.equals(Material.BEETROOT_SEEDS) || material.equals(Material.MELON_SEEDS)
							|| material.equals(Material.MELON_SEEDS)) {
						surround.setType(material);
						inventory.remove(content);
					}

					content.setAmount(content.getAmount() - 1);
					inventory.setItem(0, content);
				}
			}
		}).runTaskTimer(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
	}

}
