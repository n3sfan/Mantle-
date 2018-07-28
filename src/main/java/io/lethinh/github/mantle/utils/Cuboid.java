package io.lethinh.github.mantle.utils;

import org.bukkit.Location;

/**
 * Created by Le Thinh
 */
public class Cuboid {

	private final int minX, minY, minZ, maxX, maxY, maxZ;

	public Cuboid(Location start, Location end) {
		this(start.getBlockX(), start.getBlockY(), start.getBlockZ(), end.getBlockX(), end.getBlockY(),
				end.getBlockZ());
	}

	public Cuboid(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	/* Getters */
	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}

}
