package io.lethinh.github.mantle.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.MantleItemStacks;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Thanks to Banbeucmas for creating this.
 */
public class GiveCommand extends AbstractCommand {

	public GiveCommand(String[] args, CommandSender sender) {
		super(Mantle.PLUGIN_ID + ".give", args, sender);
	}

	@Override
	public ExecutionResult now() {
		CommandSender sender = getSender();

		if (!sender.hasPermission(getPermission())) {
			return ExecutionResult.NO_PERMISSION;
		}

		String[] args = getArgs();
        if (args.length < 1) {
            return ExecutionResult.MISSING_ARGS;
        }

		String name = args[0];

		if (args.length == 2) {
			if (!(sender instanceof Player)) {
				return ExecutionResult.CONSOLE_NOT_PERMITTED;
			}

			Player target = (Player) sender;
			if (!giveItem(name, target)) {
				sender.sendMessage(Utils.getColoredString("&cItem &4" + name + "wasn't found"));
			}

			return ExecutionResult.DONT_CARE;
		}

		Player target = Bukkit.getPlayer(name);
		if (target == null || !target.isOnline()) {
			return ExecutionResult.NO_PLAYER;
		}

		if (!giveItem(args[1], target)) {
			sender.sendMessage(Utils.getColoredString("&cItem &4" + args[1] + "wasn't found"));
		}

		return ExecutionResult.DONT_CARE;
	}

	private static boolean giveItem(String item, Player target) {
		for (ItemStack stack : MantleItemStacks.STACKS) {
			String name = stack.getItemMeta().getLocalizedName().replace(Mantle.PLUGIN_ID + "_", "");

			if (name.equalsIgnoreCase(item)) {
				target.getInventory().addItem(stack);
				target.sendMessage("Gave " + target.getName() + " " + stack.getItemMeta().getDisplayName());
				return true;
			}
		}

		return false;
	}

}
