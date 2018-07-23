package io.lethinh.github.mantle.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.MantleItemStacks;

/**
 * Created by Le Thinh
 */
public class CommandMantleGive implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}

		if (!"mantlegive".equals(label) || !"mantlegive".equals(command.getLabel())) {
			return false;
		}

		if (sender.hasPermission(Mantle.PLUGIN_ID + ".give") || sender.isOp() || sender.getName().equals("Nesfan")) {
			if (args == null || args.length < 1) {
				sender.sendMessage(ChatColor.RED + "Not enough command arguments!");
				return false;
			}

			Player player = (Player) sender;
			int itemIdx = 0;

			if (StringUtils.isNotBlank(args[0])) {
				player = Bukkit.getServer().getPlayer(args[0]);
				itemIdx = 1;
			}

			if (player == null) {
				player = (Player) sender;
				itemIdx = 0;
			}

			String item = args[itemIdx];

			for (ItemStack stack : MantleItemStacks.STACKS) {
				String name = stack.getItemMeta().getLocalizedName().replace(Mantle.PLUGIN_ID + "_", "");

				if (name.equalsIgnoreCase(item)) {
					player.getInventory().addItem(stack);
					player.sendMessage("Gave " + player.getName() + " " + stack.getItemMeta().getDisplayName());
					return true;
				}
			}

			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You cannot use this command!");
			return false;
		}
	}

}
