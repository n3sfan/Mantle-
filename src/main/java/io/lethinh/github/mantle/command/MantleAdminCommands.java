package io.lethinh.github.mantle.command;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.lethinh.github.mantle.utils.Utils;

/**
 * Thanks to Banbeucmas for creating this.
 */
public class MantleAdminCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.DARK_RED + "Invalid command arguments!");
		} else {
			String[] subArgs = Arrays.copyOfRange(args, 1, args.length); // The part I (Nesfan) edited

			switch (args[0].toLowerCase()) {
			case "give":
				new GiveCommand(subArgs, sender).execute();
				break;
			case "list":
				new ListCommand(sender).execute();
				break;
			case "allow": // Nesfan edit starts
				new AllowCommand(subArgs, sender).execute();
				break; // End
			default:
				sender.sendMessage(Utils.getColoredString("&cInvalid argument"));
				break;
			}
		}

		return true;
	}

}
