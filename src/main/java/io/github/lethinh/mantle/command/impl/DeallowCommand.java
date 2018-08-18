package io.github.lethinh.mantle.command.impl;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.command.AbstractCommand;
import io.github.lethinh.mantle.command.ExecutionResult;

/**
 * Created by Le Thinh
 */
public class DeallowCommand extends AbstractCommand {

	public DeallowCommand() {
		super("deallow", "<player>", "Deallow specific player to use your machines", null);
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
		Player check = Bukkit.getPlayerExact(target);

		if (check == null || !check.isOnline()) {
			return ExecutionResult.NO_PLAYER;
		}

		String removed = null;

		for (BlockMachine machine : BlockMachine.MACHINES) {
			// Check if sender of this command is not owner of the machine then will do
			// nothing
			if (!machine.accessiblePlayers.contains(sender.getName())) {
				continue;
			}

			if (!machine.accessiblePlayers.contains(target) || target.equals(sender.getName())) {
				continue;
			}

			machine.accessiblePlayers.remove(target);
			removed = target;
		}

		if (StringUtils.isBlank(removed)) {
			sender.sendMessage(ChatColor.DARK_RED + "You don't own any machines or the player isn't in your own list!");
		} else {
			sender.sendMessage("Deallowed " + ChatColor.RED + removed + ChatColor.WHITE + " to open your machines!");
		}

		return ExecutionResult.DONT_CARE;
	}

}
