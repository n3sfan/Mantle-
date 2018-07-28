package io.lethinh.github.mantle.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.lethinh.github.mantle.command.impl.AllowCommand;
import io.lethinh.github.mantle.command.impl.DeallowCommand;
import io.lethinh.github.mantle.command.impl.GiveCommand;
import io.lethinh.github.mantle.command.impl.ListCommand;

/**
 * Thanks to Banbeucmas for creating this.
 */
public class MantleAdminCommands implements CommandExecutor {

	public final List<AbstractCommand> commands = new ArrayList<>();

	public MantleAdminCommands() {
		commands.add(new GiveCommand());
		commands.add(new ListCommand());
		commands.add(new AllowCommand());
		commands.add(new DeallowCommand());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			// Nesfan edit starts
			sender.sendMessage(ChatColor.DARK_RED + "Invalid command arguments! It should be one of the followings: ");

			for (AbstractCommand c : commands) {
				sender.sendMessage("/mantle " + c.getName() + " " + c.getUsage() + ": " + c.getDescription());
			}
		} else {
			String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
			boolean hasCommand = false;

			for (AbstractCommand c : commands) {
				if (!c.getName().equalsIgnoreCase(args[0])) {
					continue;
				}

				c.setArgs(subArgs);
				c.setSender(sender);
				c.execute();
				hasCommand = true;
			}

			if (!hasCommand) {
				sender.sendMessage(ChatColor.DARK_RED + "No command arguments like that was found!");
			}

			// End

//			switch (args[0].toLowerCase()) {
//			case "give":
//				new GiveCommand(subArgs, sender).execute();
//				break;
//			case "list":
//				new ListCommand(sender).execute();
//				break;
//			case "allow": // Nesfan edit starts
//				new AllowCommand(subArgs, sender).execute();
//				break;
//			case "deallow":
//				new DeallowCommand(subArgs, sender).execute();
//				break;// End
//			default:
//				sender.sendMessage(Utils.getColoredString("&cInvalid argument"));
//				break;
//			}
		}

		return true;
	}

}
