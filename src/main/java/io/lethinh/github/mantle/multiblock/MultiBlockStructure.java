package io.lethinh.github.mantle.multiblock;

import java.util.List;

import org.bukkit.Location;

import io.lethinh.github.mantle.utils.Cuboid;

/**
 * Created by Le Thinh
 */
public class MultiBlockStructure {

	private final List<Location> parts;
	private final Cuboid cuboid;

	public MultiBlockStructure(List<Location> parts) {
		this.parts = parts;

		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;

		for (Location part : parts) {
			if (part.getBlockX() < minX) {
				minX = part.getBlockX();
			}

			if (part.getBlockY() < minY) {
				minY = part.getBlockY();
			}

			if (part.getBlockZ() < minZ) {
				minZ = part.getBlockZ();
			}

			if (part.getBlockX() > maxX) {
				maxX = part.getBlockX();
			}

			if (part.getBlockY() > maxY) {
				maxY = part.getBlockY();
			}

			if (part.getBlockZ() > maxZ) {
				maxZ = part.getBlockZ();
			}
		}

		this.cuboid = new Cuboid(minX, minY, minZ, maxX, maxY, maxZ);
	}

	/* Getters */
	public List<Location> getParts() {
		return parts;
	}

	public Cuboid getCuboid() {
		return cuboid;
	}

}
