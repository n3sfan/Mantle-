package io.lethinh.github.mantle.block;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;

/**
 * Created by Le Thinh
 */
public abstract class BlockMachine {

	public static final CopyOnWriteArrayList<BlockMachine> MACHINES = new CopyOnWriteArrayList<>();

	public final Block block;
	public final Player player;
	public final ItemStack heldItem;

	public BlockMachine(Block block, Player player, ItemStack heldItem) {
		this.block = block;
		this.player = player;
		this.heldItem = heldItem;
	}

	private boolean stoppedTicking = true;

	public void tick(Mantle plugin) {
		if (stoppedTicking) {
			return;
		}

		handleUpdate(plugin);
	}

	public abstract void handleUpdate(Mantle plugin);

	public boolean isStoppedTicking() {
		return stoppedTicking;
	}

	public void setStoppedTicking(boolean stoppedTicking) {
		this.stoppedTicking = stoppedTicking;
	}

	/* Object */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BlockMachine) {
			BlockMachine machine = (BlockMachine) obj;
			return block.getLocation().equals(machine.block.getLocation()) && heldItem.isSimilar(machine.heldItem);
		} else if (obj instanceof Block) {
			return block.getLocation().equals(((Block) obj).getLocation());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(block.getLocation(), heldItem);
	}

}
