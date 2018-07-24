package io.lethinh.github.mantle.command;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.MantleItemStacks;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Thanks to Banbeucmas for creating this.
 */
public class ListCommand extends AbstractCommand {

	public ListCommand(CommandSender sender) {
		super(null, null, sender);
	}

	@Override
	public ExecutionResult now() {
		getSender().sendMessage(Utils.getColoredString("&5Available machines: "));

		for (ItemStack stack : MantleItemStacks.STACKS) {
			String name = stack.getItemMeta().getLocalizedName().replace(Mantle.PLUGIN_ID + "_", "");
			getSender().sendMessage("&d" + name);
		}

		return ExecutionResult.DONT_CARE;
	}

}
