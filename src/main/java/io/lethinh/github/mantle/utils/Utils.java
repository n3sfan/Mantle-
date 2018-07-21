package io.lethinh.github.mantle.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Le Thinh
 */
public final class Utils {

	private Utils() {

	}

	public static boolean isGrowable(Material material) {
		return material.equals(Material.SOIL) || material.equals(Material.CROPS)
				|| material.equals(Material.SEEDS)
				|| material.equals(Material.BEETROOT_SEEDS) || material.equals(Material.MELON_SEEDS)
				|| material.equals(Material.PUMPKIN_SEEDS);
	}

	public static boolean areStacksEqualIgnoreDurability(ItemStack stackA, ItemStack stackB) {
		return stackA != null && stackB != null && stackA.getType().equals(stackB.getType()) && stackA.hasItemMeta()
				&& stackB.hasItemMeta() && Bukkit.getItemFactory().equals(stackA.getItemMeta(), stackB.getItemMeta());
	}

	public static BlockFace[] getMainFaces() {
		return new BlockFace[] { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH,
				BlockFace.WEST, BlockFace.EAST };
	}

	public static Collection<Block> getSurroundingBlocks(Block center, int dist, boolean yLayer) {
		return getSurroundingBlocks(center, dist, dist, dist, yLayer, null);
	}

	public static Collection<Block> getSurroundingBlocks(Block center, int xDist, int yDist, int zDist, boolean yLayer,
			Predicate<Block> predicate) {
		List<Block> ret = new ArrayList<>();
		Predicate<Block> test = block -> !(block.isEmpty() && block.isLiquid());

		if (predicate != null) {
			test = test.and(predicate);
		}

		for (int x = -xDist; x <= xDist; ++x) {
			for (int z = -zDist; z <= zDist; ++z) {
				if (yLayer) {
					for (int y = 0; y <= yDist; ++y) {
						Block neighborBlock = center.getRelative(x, y, z);

						if (test.test(center)) {
							ret.add(neighborBlock);
						}
					}
				} else {
					Block neighborBlock = center.getRelative(x, 0, z);

					if (test.test(center)) {
						ret.add(neighborBlock);
					}
				}
			}
		}

		return Collections.unmodifiableCollection(ret);
	}

}
