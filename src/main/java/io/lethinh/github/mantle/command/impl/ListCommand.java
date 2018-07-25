package io.lethinh.github.mantle.command.impl;

import io.lethinh.github.mantle.MantleItemStacks;
import io.lethinh.github.mantle.command.AbstractCommand;
import io.lethinh.github.mantle.command.ExecutionResult;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Thanks to Banbeucmas for creating this.
 */
public class ListCommand extends AbstractCommand {

	public ListCommand() {
		super("list", "Show a list of machines");
	}

	@Override
	public ExecutionResult now() {
		getSender().sendMessage(Utils.getColoredString("&5Available machines: "));
		MantleItemStacks.STACKS.forEach(stack -> getSender().sendMessage(stack.getItemMeta().getDisplayName()));
		return ExecutionResult.DONT_CARE;
	}

}
