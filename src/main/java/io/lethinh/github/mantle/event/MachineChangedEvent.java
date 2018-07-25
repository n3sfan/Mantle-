package io.lethinh.github.mantle.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.MantleItemStacks;
import io.lethinh.github.mantle.block.BlockMachine;
import io.lethinh.github.mantle.block.impl.BlockBlockBreaker;
import io.lethinh.github.mantle.block.impl.BlockBlockPlacer;
import io.lethinh.github.mantle.block.impl.BlockMobMagnet;
import io.lethinh.github.mantle.block.impl.BlockTreeCutter;

/**
 * Created by Le Thinh
 */
public class MachineChangedEvent implements Listener {

	@EventHandler
	public void onBlockPlaced(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		ItemStack heldItem = event.getItemInHand();
		// ItemStack copy = heldItem.clone();
		Block block = event.getBlockPlaced();
		BlockMachine machine = null;

		if (heldItem.isSimilar(MantleItemStacks.TREE_CUTTER)) {
			BlockMachine.MACHINES.add(machine = new BlockTreeCutter(block, name));
		} /*
			 * else if (heldItem.isSimilar(MantleItemStacks.PLANTER)) {
			 * BlockMachine.MACHINES.add(machine = new BlockPlanter(block)); }
			 */
		else if (heldItem.isSimilar(MantleItemStacks.BLOCK_BREAKER)) {
			BlockMachine.MACHINES.add(machine = new BlockBlockBreaker(block, name));
		} else if (heldItem.isSimilar(MantleItemStacks.BLOCK_PLACER)) {
			BlockMachine.MACHINES.add(machine = new BlockBlockPlacer(block, name));
		} else if (heldItem.isSimilar(MantleItemStacks.MOB_MAGNET)) {
			BlockMachine.MACHINES.add(machine = new BlockMobMagnet(block, name));
		}

		if (machine != null) {
			if (machine.canPlace(player)) {
				machine.setStoppedTick(false);

				if (machine instanceof Listener) {
					Bukkit.getServer().getPluginManager().registerEvents((Listener) machine, Mantle.instance);
				}
			} /*
				 * else { BlockMachine.MACHINES.remove(machine);
				 *
				 * new BukkitRunnable() {
				 *
				 * @Override public void run() { block.setType(Material.AIR);
				 * player.getInventory().addItem(copy); player.sendMessage(ChatColor.RED +
				 * "Oops, look like you cannot use this block! Please get higher level or permission."
				 * ); } }.runTask(Mantle.instance); }
				 */
		}
	}

	@EventHandler
	public void onBlockBroken(BlockBreakEvent event) {
		Block block = event.getBlock();

		BlockMachine.MACHINES.removeIf(machine -> {
			if (machine.block.getLocation().equals(block.getLocation())) {
				machine.setStoppedTick(true);

				if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
					event.setDropItems(false);

					for (ItemStack stack : MantleItemStacks.STACKS) {
						if (stack.getItemMeta().getLocalizedName().replace(Mantle.PLUGIN_ID + "_", "")
								.equalsIgnoreCase(machine.getName())) {
							machine.dropItems(stack);
						}
					}
				}

				return true;
			}

			return false;
		});
	}

	@EventHandler
	public void onBlockOpened(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		Block block = event.getClickedBlock();
		Player player = event.getPlayer();

		BlockMachine.MACHINES.stream().filter(machine -> block.getLocation().equals(machine.block.getLocation()))
				.forEach(machine -> {
					event.setCancelled(true);

					if (machine.canOpen(player)) {
						player.openInventory(machine.inventory);
					} else {
						player.sendMessage(ChatColor.RED + "You cannot open this machine because it is locked!");
					}
				});
	}

}
