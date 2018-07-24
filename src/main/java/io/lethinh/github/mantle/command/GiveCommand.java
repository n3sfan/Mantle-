package io.lethinh.github.mantle.command;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.MantleItemStacks;
import io.lethinh.github.mantle.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand extends AbstractCommand {
    public GiveCommand(String[] args, CommandSender sender) {
        super( Mantle.PLUGIN_ID + ".give", args, sender);
    }

    @Override
    public ExecutionResult now() {
        if(!getSender().hasPermission(getPermission())){
            return ExecutionResult.NOPERMISSION;
        }
        if(getArgs().length < 2){
            return ExecutionResult.MISSINGARGS;
        }
        else if(getArgs().length == 3){
            if(!(getSender() instanceof Player)){
                return ExecutionResult.CONSOLENOTPERMITTED;
            }
            Player target = (Player) getSender();
            if(!giveItem(getArgs()[0], target)){
                getSender().sendMessage(Utils.getColoredString("&cItem &4" + getArgs()[0] + "not found"));
            }
        }

        Player target = Bukkit.getPlayer(getArgs()[0]);
        if(target == null || !target.isOnline()){
            return ExecutionResult.NOPLAYER;
        }

        if(!giveItem(getArgs()[1], target)){
            getSender().sendMessage(Utils.getColoredString("&cItem &4" + getArgs()[1] + "not found"));
        }

        return null;
    }

    private boolean giveItem(String item, Player target){
        for (ItemStack stack : MantleItemStacks.STACKS) {
            String name = stack.getItemMeta().getLocalizedName().replace(Mantle.PLUGIN_ID + "_", "");

            if (name.equalsIgnoreCase(item)) {
                target.getInventory().addItem(stack);
                target.sendMessage("Gave " + target.getName() + " " + stack.getItemMeta().getDisplayName());
                return true;
            }
        }
        return false;

    }
}
