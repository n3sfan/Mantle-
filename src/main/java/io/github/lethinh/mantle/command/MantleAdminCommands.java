package io.github.lethinh.mantle.command;

import io.github.lethinh.mantle.command.impl.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        commands.add(new ReloadCommand());
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
		}

		return true;
	}

}
