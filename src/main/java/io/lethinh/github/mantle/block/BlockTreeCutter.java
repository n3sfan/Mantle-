package io.lethinh.github.mantle.block;

import java.util.Collection;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Created by Le Thinh
 */
public class BlockTreeCutter extends BlockMachine {

	public BlockTreeCutter(Block block) {
		super(block, 45, "Tree Cutter");
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		(subThread = new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				Collection<Block> surroundings = Utils.getSurroundingBlocks(block, 7, 30, 7, true, null);

				for (Block surround : surroundings) {
					Material material = surround.getType();

					if (material.equals(Material.LOG) || material.equals(Material.LOG_2)
							|| material.equals(Material.LEAVES) || material.equals(Material.LEAVES_2)) {
						block.getWorld().playEffect(surround.getLocation(), Effect.STEP_SOUND, material.getId());
						surround.getDrops().forEach(inventory::addItem);
						surround.setType(Material.AIR);
					}
				}
			}
		}).runTaskTimer(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
	}

}
