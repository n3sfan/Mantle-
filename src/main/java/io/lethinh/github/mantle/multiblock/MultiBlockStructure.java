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

//	/**
//	 * @param center Center of multiblock, same y with the controller
//	 * @param size   Size of the multi-block, e.g: if it is 3x3 then the size is 3
//	 * @return
//	 */
//	public static MultiBlockStructure detectMultiblock(Location center, int size, ItemStack partMaterial) {
//		List<Location> parts = new ArrayList<>();
//
//		int centerY = center.getBlockY();
//		center = offsetLocation(center, BlockFace.DOWN, size);
//
//		// Check walls
//		BlockFace[] horizontalFaces = new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST,
//				BlockFace.EAST };
//
//		// Distances to each wall
//		int[] dists2Walls = new int[4];
//
//		for (int i = 0; i < horizontalFaces.length; ++i) {
//			BlockFace face = horizontalFaces[i];
//			Location cornerLoc = offsetLocation(center, face, size);
//			// Diagonal blocks range calculation, not for line
//			dists2Walls[i] = cornerLoc.getBlockX() + center.getBlockX() - (cornerLoc.getBlockZ() + center.getBlockZ());
//		}
//
//		int xRange = dists2Walls[2] + dists2Walls[3]; // Blocks length of multi-block on X axis
//		int zRange = dists2Walls[0] + dists2Walls[1]; // Blocks length of the multi-block on Z axis
//
//		// Larger then expected size of the multi-block so skip it
//		if (xRange > size || zRange > size) {
//			return null;
//		}
//
//		// Check floor
//		if (!checkHorizontal(center, dists2Walls, partMaterial, parts)) {
//			return null;
//		}
//
//		// Check ceil
//		if (!checkHorizontal(offsetLocation(center, BlockFace.UP, size), dists2Walls, partMaterial, parts)) {
//			return null;
//		}
//
//		Cuboid cuboid = new Cuboid(center.getBlockX() + dists2Walls[0], center.getBlockY(),
//				center.getBlockZ() + dists2Walls[2], center.getBlockX() + dists2Walls[1], centerY,
//				center.getBlockZ() + dists2Walls[3]);
//	}
//
//	@SuppressWarnings("deprecation")
//	private static boolean checkHorizontal(Location center, int[] cornersDists, ItemStack partMaterial,
//			List<Location> parts) {
//		// Negative face to positive face
//		Location from = center.add(cornersDists[2], 0, cornersDists[0]);
//		Location to = center.add(cornersDists[3], 0, cornersDists[1]);
//		List<Location> blocks = new ArrayList<>();
//
//		// Add all locations of X direction
//		for (int x = 0; x < to.getBlockX() - from.getBlockX(); ++x) {
//			blocks.add(center.add(x, 0, 0));
//			blocks.add(center.add(-x, 0, 0));
//		}
//
//		// Same thing of Z direction
//		for (int z = 0; z <= to.getBlockZ() - from.getBlockZ(); ++z) {
//			blocks.add(center.add(0, 0, z));
//			blocks.add(center.add(0, 0, -z));
//		}
//
//		return blocks.stream().map(location -> location.getBlock()).allMatch(block -> !block.isEmpty()
//				&& block.getType() == partMaterial.getType() && block.getData() == partMaterial.getData().getData())
//				&& parts.addAll(blocks);
//	}
//
//	private static boolean checkLayers(Location center, int height, int[] cornersDists, ItemStack partMaterial,
//			List<Location> parts) {
//		// Negative face to positive face
//		Location from = center.add(cornersDists[2], 0, cornersDists[0]);
//		Location to = center.add(cornersDists[3], 0, cornersDists[1]);
//		List<Location> blocks = new ArrayList<>();
//
//		for (int i = 0; i < height; ++i) {
//
//		}
//	}
//
//	/**
//	 * Offset {@code location} until it meets the outer wall of the multi-block
//	 *
//	 * @param location
//	 * @param face
//	 * @param n
//	 * @return
//	 */
//	private static Location offsetLocation(Location location, BlockFace face, int n) {
//		for (int i = 0; i < n && location.getBlock().isEmpty(); ++i) {
//			location = Utils.offsetLocation(location, face);
//		}
//
//		return location;
//	}

	/* Getters */
	public List<Location> getParts() {
		return parts;
	}

	public Cuboid getCuboid() {
		return cuboid;
	}

}
