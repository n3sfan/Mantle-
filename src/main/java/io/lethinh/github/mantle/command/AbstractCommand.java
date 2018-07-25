package io.lethinh.github.mantle.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import io.lethinh.github.mantle.utils.Utils;

/**
 * Thanks to Banbeucmas for creating this. I (Nesfan) removed final from
 * {@code args} and {@code sender}, and added {@code description} in order for
 * the ease of iteration, see {@link MantleAdminCommands}
 */
public abstract class AbstractCommand {

	private final String name, usage, description, permission;
	private String[] args;
	private CommandSender sender;

	public AbstractCommand(String name, String description) {
		this(name, "", description, null);
	}

	public AbstractCommand(String name, String usage, String description, String permission) {
		this.name = name;
		this.usage = usage;
		this.description = description;
		this.permission = permission;
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
			sender.sendMessage(Utils.getColoredString("&4Player isn't exist or isn't online"));
			break;
		case CONSOLE_NOT_PERMITTED:
			sender.sendMessage(Utils.getColoredString("&4This command is not available to the console"));
			break;
		default:
			break;
		}
	}

	/* Getters */
	public String getName() {
		return name;
	}

	public String getUsage() {
		return usage;
	}

	public String getDescription() {
		return description;
	}

	public String getPermission() {
		return permission;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public CommandSender getSender() {
		return sender;
	}

	public void setSender(CommandSender sender) {
		this.sender = sender;
	}

}
