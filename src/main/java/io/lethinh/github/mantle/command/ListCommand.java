package io.lethinh.github.mantle.command;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.MantleItemStacks;
import io.lethinh.github.mantle.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

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
        return null;
    }
}
