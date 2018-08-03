package io.github.lethinh.mantle.command.impl;

import io.github.lethinh.mantle.MantleItemStacks;
import io.github.lethinh.mantle.command.AbstractCommand;
import io.github.lethinh.mantle.command.ExecutionResult;
import io.github.lethinh.mantle.utils.Utils;

/**
 * Thanks to Banbeucmas for creating this.
 */
public class ListCommand extends AbstractCommand {

	public ListCommand() {
		super("list", "Show a list of machines");
	}

	@Override
	public ExecutionResult now() {
		getSender().sendMessage(Utils.getColoredString("&5Available machines and items: "));
		MantleItemStacks.STACKS.forEach(stack -> getSender().sendMessage(stack.getItemMeta().getDisplayName()));
		return ExecutionResult.DONT_CARE;
	}

}
