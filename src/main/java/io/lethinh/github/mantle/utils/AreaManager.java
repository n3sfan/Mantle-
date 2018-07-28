package io.lethinh.github.mantle.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Created by Le Thinh
 */
public class AreaManager {

	private final Block center;
	private final List<Block> scannedBlocks;
	private final int xRange, yRange, zRange;
	private final boolean scanY;
	private final Predicate<Block> predicate;

	public AreaManager(Block center, int xRange, int yRange, int zRange, boolean scanY) {
		this(center, xRange, yRange, zRange, scanY, block -> !block.isEmpty() && !block.isLiquid());
	}

	public AreaManager(Block center, int xRange, int yRange, int zRange, boolean scanY, Predicate<Block> predicate) {
		this.center = center;
		this.scannedBlocks = new ArrayList<>();
		this.xRange = xRange;
		this.yRange = yRange;
		this.zRange = zRange;
		this.scanY = scanY;
		this.predicate = predicate;
	}

	public void scanBlocks() {
		if (xRange == 0 && zRange == 0 && !scanY && yRange == 0) {
			Location location = center.getLocation();
			String loc = "World: " + location.getWorld().getName() + ", X:" + location.getX() + ", Y:" + location.getY()
					+ ", Z:" + location.getZ();
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "At: " + loc + " . Invalid area, cannot be scanned!");
			return;
		}

		World world = center.getWorld();

		for (int x = center.getX() - xRange; x <= center.getX() + xRange; ++x) {
			for (int z = center.getZ() - zRange; z <= center.getZ() + zRange; ++z) {
				if (scanY) {
					for (int y = center.getY(); y < yRange * 2 + 1; ++y) {
						Block neighborBlock = world.getBlockAt(x, y, z);

						if (predicate == null || predicate.test(neighborBlock)) {
							scannedBlocks.add(neighborBlock);
						}
					}
				} else {
					Block neighborBlock = world.getBlockAt(x, center.getY(), z);

					if (predicate == null || predicate.test(neighborBlock)) {
						scannedBlocks.add(neighborBlock);
					}
				}
			}
		}
	}

	public boolean noBlocks() {
		return scannedBlocks.isEmpty();
	}

	/* Getters */
	public List<Block> getScannedBlocks() {
		return scannedBlocks;
	}

}
