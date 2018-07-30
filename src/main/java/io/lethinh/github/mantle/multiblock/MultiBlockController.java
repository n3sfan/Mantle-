package io.lethinh.github.mantle.multiblock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
	public BlockFace direction;

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

	/* Callbacks */
	@Override
	public void onMachinePlaced(Player player, ItemStack heldItem) {
		direction = Utils.getBlockFaceFromPlayer(block.getLocation(), player);
		System.out.println("Placed dir: " + direction.name());
		super.onMachinePlaced(player, heldItem);
	}

	@Override
	public void onMachineBroken(Player player) {
		if (structure == null) {
			return;
		}

		for (BlockMachine machine : BlockMachine.MACHINES) {
			if (!(machine instanceof MultiBlockPart)) {
				continue;
			}

			for (Location part : structure.getParts()) {
				if (part.getBlock().isEmpty() || part.getBlock().isLiquid()
						|| !machine.block.getLocation().equals(part)) {
					continue;
				}

				MultiBlockPart<?> multiBlockPart = (MultiBlockPart<?>) machine;
				multiBlockPart.hasController = false;
				multiBlockPart.controller = null;
			}
		}
	}

	/* Multi-block */
	public void checkMultiblock() {
		Location inside = Utils.offsetLocation(block.getLocation(), direction.getOppositeFace());
		Location center = tracker.getCenter(inside);
		MultiBlockStructure structure = tracker.detectMultiblock(center);
		this.structure = structure;
	}

	/* NBT */
	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = super.writeToNBT();
		nbt.setBoolean("FirstTime", firstTime);
		nbt.setInteger("Direction", direction.ordinal());
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		firstTime = nbt.hasKey("FirstTime") ? nbt.getBoolean("FirstTime") : false;
		direction = nbt.hasKey("Direction") ? BlockFace.values()[nbt.getInteger("Direction")] : BlockFace.SELF;
		super.readFromNBT(nbt);
	}

}
