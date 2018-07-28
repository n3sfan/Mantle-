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

		// Get the start and end corners' locations
		Location start = center, end = center;

		if ((size & 1) == 0) // If size is even
		{
			for (int i = 0; i < size / 2; ++i) {
				offsetStartEndLocations(start, end);
			}
		} else // If size is odd
		{
			for (int i = 0; i < (size - 1) / 2; ++i) {
				offsetStartEndLocations(start, end);
			}
		}

		// Check and add floor
		if (!checkHorizontal(start, end, parts)) {
			return null;
		}

		// Check and add ceil
		if (!checkHorizontal(offset2Outer(start, BlockFace.UP, size + 1), offset2Outer(start, BlockFace.UP, size + 1),
				parts)) {
			return null;
		}

		// Check and add walls
		if (!checkWalls(start, end, size, parts)) {
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
	 * Get center location of the multi-block by the block behind the controller
	 *
	 * @param inside
	 * @return the center location of the multiblock
	 */
	public Location getCenter(Location inside) {
		int x1 = 1, x2 = 1;
		int z1 = 1, z2 = 1;

		// Not 0 because that will check all multi-block
		for (int i = 1; i < size; ++i) {
			// If block is inner on x direction
			if (inside.add(x1, 0, 0).getBlock().isEmpty()) {
				++x1;
			}

			if (inside.add(-x2, 0, 0).getBlock().isEmpty()) {
				++x2;
			}

			// If x1 hit the wall on z and x2 didn't
			if (x1 - x2 > 1) {
				--x1;
				inside = inside.add(-1, 0, 0);
				++x2;
			}

			// Or x2 hit the wall and x1 didn't
			if (x2 - x1 > 1) {
				++x1;
				inside = inside.add(1, 0, 0);
				--x2;
			}

			// If block is inner on z direction
			if (inside.add(0, 0, z1).getBlock().isEmpty()) {
				++z1;
			}

			if (inside.add(0, 0, -z2).getBlock().isEmpty()) {
				++z2;
			}

			// If z1 hit the wall on z and z2 didn't
			if (z1 - z2 > 1) {
				--z1;
				inside = inside.add(0, 0, -1);
				++z2;
			}

			// Or z2 hit the wall and z1 didn't
			if (z2 - z1 > 1) {
				++z1;
				inside = inside.add(0, 0, 1);
				--z2;
			}
		}

		return inside;
	}

	protected boolean checkHorizontal(Location start, Location end, List<Location> parts) {
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

	protected boolean checkWalls(Location start, Location end, int height, List<Location> parts) {
		// Get the outer start and end locations
		start = start.add(-1, 0, -1);
		end = end.add(1, 0, 1);

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

	protected void offsetStartEndLocations(Location start, Location end) {
		start = start.add(-1, 0, -1);
		end = start.add(1, 0, 1);
	}

	protected Location offset2Outer(Location location, BlockFace face, int n) {
		for (int i = 0; i < n && location.getBlock().isEmpty(); ++i) {
			location = Utils.offsetLocation(location, face);
		}

		return location;
	}

	protected abstract boolean isBlockValid(Location location);

	/* Getter */
	public int getSize() {
		return size;
	}

}
