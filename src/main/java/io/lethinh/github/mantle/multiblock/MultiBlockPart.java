package io.lethinh.github.mantle.multiblock;

import org.bukkit.block.Block;
import org.bukkit.event.Listener;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.block.BlockMachine;

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

	}

	@Override
	public void work() {
		if (!hasController) {
			return;
		}

		accessiblePlayers = controller.accessiblePlayers;
		inventory = controller.inventory;
	}

	/* NBT */
//	@Override
//	public NBTTagCompound writeToNBT() {
//		NBTTagCompound nbt = super.writeToNBT();
//		nbt.setBoolean("HasController", hasController);
//		nbt.setString("ControllerLoc", Utils.serializeLocation(controller.block.getLocation()));
//		return nbt;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public void readFromNBT(NBTTagCompound nbt) {
//		hasController = nbt.getBoolean("HasController");
//		Location location = Utils.deserializeLocation(nbt.getString("ControllerLoc"));
//
//		for (BlockMachine machine : BlockMachine.MACHINES) {
//			if (!(machine instanceof MultiBlockMachine) || !machine.block.getLocation().equals(location)) {
//				continue;
//			}
//
//			controller = (MultiBlockMachine<T>) machine;
//		}
//
//		super.readFromNBT(nbt);
//	}

}
