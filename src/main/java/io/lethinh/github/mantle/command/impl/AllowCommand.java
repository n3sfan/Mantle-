package io.lethinh.github.mantle.command.impl;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.lethinh.github.mantle.block.BlockMachine;
import io.lethinh.github.mantle.command.AbstractCommand;
import io.lethinh.github.mantle.command.ExecutionResult;

/**
 * Created by Le Thinh
 */
public class AllowCommand extends AbstractCommand {

	public AllowCommand() {
		super("allow", "<player>", "Allow specific player to use your machines", null);
	}

	@Override
	public ExecutionResult now() {
		String[] args = getArgs();
		CommandSender sender = getSender();

		if (!(sender instanceof Player)) {
			return ExecutionResult.CONSOLE_NOT_PERMITTED;
		}

		if (args.length == 0) {
			return ExecutionResult.MISSING_ARGS;
		}

		String target = args[0];
		Player check = Bukkit.getPlayer(target);

		if (check == null || !check.isOnline()) {
			return ExecutionResult.NO_PLAYER;
		}

		String added = null;

		for (BlockMachine machine : BlockMachine.MACHINES) {
			// Check if sender of this command is not owner of the machine
			if (!machine.accessiblePlayers.contains(sender.getName())) {
				continue;
			}

			if (machine.accessiblePlayers.contains(target)) {
				continue;
			}

			machine.accessiblePlayers.add(target);
			added = target;
		}

		if (StringUtils.isBlank(added)) {
			sender.sendMessage(
					ChatColor.DARK_RED + "You don't own any machines or the player is already in your own list!");
		} else {
			sender.sendMessage("Allowed " + ChatColor.GOLD + added + ChatColor.WHITE + " to open your machines!");
		}

		return ExecutionResult.DONT_CARE;
	}

}
