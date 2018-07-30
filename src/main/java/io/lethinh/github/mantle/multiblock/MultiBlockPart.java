package io.lethinh.github.mantle.multiblock;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.block.BlockMachine;
import io.lethinh.github.mantle.nbt.NBTTagCompound;

/**
 * Created by Le Thinh
 */
public class MultiBlockPart<T extends MultiBlockTracker> extends BlockMachine implements Listener {

	public MultiBlockController<T> controller;
	public boolean hasController = false;

	public MultiBlockPart(Block block) {
		super(block, 0, "");
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
	}

	@Override
	public void work() {
		if (!hasController) {
			return;
		}

		accessiblePlayers = controller.accessiblePlayers;
		inventory = controller.inventory;
	}

	@Override
	public boolean canOpen(Player player) {
		return !hasController || controller == null || controller.canOpen(player);
	}

	@Override
	public boolean canBreak(Player player) {
		return !hasController || controller == null || controller.canBreak(player);
	}

	/* NBT */
	@Override
	public NBTTagCompound writeToNBT() {
		return new NBTTagCompound();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	}

}
