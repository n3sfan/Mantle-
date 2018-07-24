package io.lethinh.github.mantle.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.lethinh.github.mantle.block.BlockMachine;

/**
 * Created by Le Thinh
 */
public class AllowCommand extends AbstractCommand {

	public AllowCommand(String[] args, CommandSender sender) {
		super(null, args, sender);
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

		for (BlockMachine machine : BlockMachine.MACHINES) {
			if (!machine.accessiblePlayers.contains(sender.getName())) {
				continue;
			}

			machine.accessiblePlayers.add(target);
		}

		return ExecutionResult.DONT_CARE;
	}

}
