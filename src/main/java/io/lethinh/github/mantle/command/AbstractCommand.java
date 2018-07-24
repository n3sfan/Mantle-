package io.lethinh.github.mantle.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import io.lethinh.github.mantle.utils.Utils;

/**
 * Thanks to Banbeucmas for creating this.
 */
public abstract class AbstractCommand {

	private final String permission;
	private final String[] args;
	private final CommandSender sender;

	public AbstractCommand(CommandSender sender) {
		this(null, new String[0], sender);
	}

	public AbstractCommand(String permission, String[] args, CommandSender sender) {
		this.permission = permission;
		this.args = args;
		this.sender = sender;
	}

	/**
	 * @return an {@link ExecutionResult}, should never be null
	 */
	public abstract ExecutionResult now();

	public void execute() {
		switch (now()) {
		case DONT_CARE:
			break;
		case MISSING_ARGS:
			// TODO Do stuff
			sender.sendMessage(ChatColor.DARK_RED + "Not enough command arguments!");
			break;
		case NO_PERMISSION:
			sender.sendMessage(Utils.getColoredString("&4Missing Permission: &c" + permission));
			break;
		case NO_PLAYER:
			sender.sendMessage(Utils.getColoredString("&4Player is not exist or isn't online"));
			break;
		case CONSOLE_NOT_PERMITTED:
			sender.sendMessage(Utils.getColoredString("&4This command is not available to console"));
			break;
		default:
			break;
		}
	}

	/* Getters */
	public String getPermission() {
		return permission;
	}

	public String[] getArgs() {
		return args;
	}

	public CommandSender getSender() {
		return sender;
	}

}
