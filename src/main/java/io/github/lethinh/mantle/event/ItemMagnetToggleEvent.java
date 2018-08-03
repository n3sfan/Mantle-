package io.github.lethinh.mantle.event;

import io.github.lethinh.mantle.utils.ItemStackFactory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import io.github.lethinh.mantle.MantleItemStacks;

/**
 * Created by Le Thinh
 */
public class ItemMagnetToggleEvent implements Listener {

	@EventHandler
	public void onToggle(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (!player.isSneaking() || event.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}

		PlayerInventory inventory = player.getInventory();

		boolean mainHand = inventory.getItemInMainHand() != null
				&& inventory.getItemInMainHand().getItemMeta() != null
				&& MantleItemStacks.ITEM_MAGNET.getItemMeta().getLocalizedName()
						.equals(inventory.getItemInMainHand().getItemMeta().getLocalizedName());
		boolean offHand = inventory.getItemInOffHand() != null && inventory.getItemInOffHand().getItemMeta() != null
				&& MantleItemStacks.ITEM_MAGNET.getItemMeta().getLocalizedName()
						.equals(inventory.getItemInOffHand().getItemMeta().getLocalizedName());

		if (mainHand && offHand) {
			player.sendMessage(
					ChatColor.RED + "You cannot toggle 2 magnets at the same time, please only hold one!");
			return;
		}

		if (mainHand) {
			ItemStack stack = inventory.getItemInMainHand();
			boolean disabled = stack.getItemMeta().getDisplayName().contains("Disabled");

			if (disabled) {
				inventory.setItemInMainHand(new ItemStackFactory(stack)
						.setDisplayName(ChatColor.BLUE + "Item Magnet " + ChatColor.DARK_GREEN + "(Enabled)")
						.build());
			} else {
				inventory.setItemInMainHand(new ItemStackFactory(stack)
						.setDisplayName(ChatColor.BLUE + "Item Magnet " + ChatColor.DARK_RED + "(Disabled)")
						.build());
			}
		}

		if (offHand) {
			ItemStack stack = inventory.getItemInOffHand();
			boolean disabled = stack.getItemMeta().getDisplayName().contains("Disabled");

			if (disabled) {
				inventory.setItemInOffHand(new ItemStackFactory(stack)
						.setDisplayName(ChatColor.BLUE + "Item Magnet " + ChatColor.DARK_GREEN + "(Enabled)")
						.build());
			} else {
				inventory.setItemInOffHand(new ItemStackFactory(stack)
						.setDisplayName(ChatColor.BLUE + "Item Magnet " + ChatColor.DARK_RED + "(Disabled)")
						.build());
			}
		}
	}

}
