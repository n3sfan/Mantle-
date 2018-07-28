package io.lethinh.github.mantle.multiblock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.block.BlockMachine;
import io.lethinh.github.mantle.nbt.NBTTagCompound;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Created by Le Thinh
 */
public abstract class MultiBlockController<T extends MultiBlockTracker> extends BlockMachine {

	public T tracker;
	public MultiBlockStructure structure;
	public boolean firstTime = true;

	public MultiBlockController(Block block, int invSlots, String invName, String... players) {
		super(block, invSlots, invName, players);
	}

	public MultiBlockController(Block block, Inventory inventory, String... players) {
		super(block, inventory, players);
	}

	@Override
	public void handleUpdate(Mantle plugin) {
		runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
	}

	@Override
	public void work() {
		checkMultiblock();

		if (structure == null) {
			return;
		}

		if (firstTime) {
			accessiblePlayers.stream().map(Bukkit::getPlayerExact).filter(p -> p != null)
					.forEach(p -> p.sendMessage(ChatColor.GREEN + "Multiblock at " + block.getLocation().toString()
							+ " was successfully formed!"));
			firstTime = false;
		}

		mimicWork();
	}

	public abstract void mimicWork();

	/* Multi-block */
	public void checkMultiblock() {
		Location inside = Utils.offsetLocation(block.getLocation(), getDirection().getOppositeFace());
		MultiBlockStructure structure = tracker.detectMultiblock(tracker.getCenter(inside));
		this.structure = structure;
	}

	public abstract BlockFace getDirection();

	/* NBT */
	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = super.writeToNBT();
		nbt.setBoolean("FirstTime", firstTime);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		firstTime = nbt.getBoolean("FirstTime");
		super.readFromNBT(nbt);
	}

}
