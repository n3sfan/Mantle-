package io.lethinh.github.mantle.multiblock;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import io.lethinh.github.mantle.block.BlockMachine;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Created by Le Thinh
 */
public abstract class MultiBlockTracker {

	@SuppressWarnings("rawtypes")
	private final MultiBlockController controller;
	private final int size;

	public MultiBlockTracker(@SuppressWarnings("rawtypes") MultiBlockController controller, int size) {
		this.controller = controller;
		this.size = size;
	}

	@SuppressWarnings("unchecked")
	public MultiBlockStructure detectMultiblock(Location center) {
		List<Location> parts = new ArrayList<>();

		// Get the minimum y
		center = offset2Outer(center, BlockFace.DOWN, size);

		// Calculate distances to each wall in 4 horizontal directions
		BlockFace[] horizontals = new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST };
		int[] dists2Walls = new int[4];

		for (int i = 0; i < horizontals.length; ++i) {
			BlockFace face = horizontals[i];
			Location wallLoc = offset2Outer(center, face, size);

			// No outer wall, shit
			if (wallLoc.getBlock().isEmpty() || wallLoc.getBlock().isLiquid()) {
				return null;
			}

			// Calculate distance (Pythagorean theorem)
			int dx = wallLoc.getBlockX() - center.getBlockX();
			int dz = wallLoc.getBlockZ() - center.getBlockZ();
			dists2Walls[i] = (int) Math.sqrt(dx * dx + dz * dz);
		}

		// Larger than the multi-block's size?
		if (dists2Walls[1] - dists2Walls[0] + 1 > size || dists2Walls[3] - dists2Walls[2] + 1 > size) {
			return null;
		}

		// Check and add floor
		if (!checkHorizontal(center, dists2Walls, parts)) {
			System.out.println("Floor failed");
			return null;
		}

		// Check and add ceil
		if (!checkHorizontal(offset2Outer(center, BlockFace.UP, size + 1), dists2Walls, parts)) {
			System.out.println("Ceil failed");
			return null;
		}

		// Check and add walls
		if (!checkWalls(center, dists2Walls, size, parts)) {
			System.out.println("Walls failed");
			return null;
		}

		// Validate parts
		for (BlockMachine machine : BlockMachine.MACHINES) {
			if (!(machine instanceof MultiBlockPart)) {
				continue;
			}

			for (Location part : parts) {
				if (!machine.block.getLocation().equals(part)) {
					continue;
				}

				MultiBlockPart<?> multiBlockPart = (MultiBlockPart<?>) machine;

				if (multiBlockPart.hasController) {
					return null;
				}

				multiBlockPart.hasController = true;
				multiBlockPart.controller = controller;
			}
		}

		return new MultiBlockStructure(parts);
	}

	/**
	 * Calculate center location of the multi-block by the block inside it
	 *
	 * @param inside
	 * @return the center location of the multiblock
	 */
	public Location getCenter(Location inside) {
		// left = negative, right = positive (the same on the axis)

		int xLeft = 1, xRight = 1;
		int zLeft = 1, zRight = 1;

		for (int i = 1; i < size; ++i) // Even if I set to 0, it will return the same result
		{
			// Expand x left
			if (inside.add(-xLeft, 0, 0).getBlock().isEmpty()) {
				++xLeft;
			}

			// Expand x right
			if (inside.add(xRight, 0, 0).getBlock().isEmpty()) {
				++xRight;
			}

			// Move the position on the left of x axis
			if (xLeft - xRight > 1) {
				--xLeft;
				inside = inside.add(-1, 0, 0);
				++xRight;
			}

			// Or on the right
			if (xRight - xLeft > 1) {
				--xRight;
				inside = inside.add(1, 0, 0);
				++xLeft;
			}

			// Expand z left
			if (inside.add(0, 0, -zLeft).getBlock().isEmpty()) {
				++zLeft;
			}

			// Expand z right
			if (inside.add(0, 0, zRight).getBlock().isEmpty()) {
				++zRight;
			}

			// Move the position on the left of z axis
			if (zLeft - zRight > 1) {
				--zLeft;
				inside = inside.add(0, 0, -1);
				++zRight;
			}

			// Or on the right
			if (zRight - zLeft > 1) {
				--zRight;
				inside = inside.add(0, 0, 1);
				++zLeft;
			}
		}

		return inside;
	}

	protected boolean checkHorizontal(Location center, int[] dists2Walls, List<Location> parts) {
		// Calculate the outer start and end locations
		Location start = center.add(-dists2Walls[2], 0, -dists2Walls[0]);
		Location end = center.add(dists2Walls[3], 0, dists2Walls[1]);

		// Check if all blocks' material match with the multi-block part's
		for (Location z = start; z.getBlockZ() <= end.getBlockZ(); z = z.add(0, 0, 1)) {
			for (Location x = z; x.getBlockX() <= end.getBlockX(); x = x.add(1, 0, 0)) {
				if (!isBlockValid(x)) {
					return false;
				} else {
					parts.add(x);
				}
			}
		}

		return true;
	}

	protected boolean checkWalls(Location center, int[] dists2Walls, int height, List<Location> parts) {
		// Calculate the outer start and end locations
		Location start = center.add(-dists2Walls[2], 0, -dists2Walls[0]);
		Location end = center.add(dists2Walls[3], 0, dists2Walls[1]);

		for (int i = 1; i <= height; ++i) {
			start = start.add(0, i, 0);
			end = end.add(0, i, 0);

			for (Location z = start; z.getBlockZ() <= end.getBlockZ(); z = z.add(0, 0, 1)) {
				for (Location x = z; x.getBlockX() <= end.getBlockX(); x = x.add(1, 0, 0)) {
					// Skip because it is inner block
					if (x.getBlock().isEmpty()) {
						continue;
					}

					if (!isBlockValid(x)) {
						return false;
					} else {
						parts.add(x);
					}
				}
			}
		}

		return true;
	}

	protected Location offset2Outer(Location location, BlockFace face, int n) {
		for (int i = 0; i < n && location.getBlock().isEmpty(); ++i) {
			location = Utils.offsetLocation(location, face);
			System.out.println(location.toString());
		}

		return location;
	}

	protected abstract boolean isBlockValid(Location location);

	/* Getter */
	public int getSize() {
		return size;
	}

}
