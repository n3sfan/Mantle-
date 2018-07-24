package io.lethinh.github.mantle.command;

import io.lethinh.github.mantle.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.MantleItemStacks;

/**
 * Created by Le Thinh
 */
public class MantleAdminCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length <= 0){
			//TODO Help commands
		}
		else{
			switch (args[0].toLowerCase()){
				case "give":
					new GiveCommand(args, sender).execute();
					break;
				case "list":
					new ListCommand(sender).execute();
					break;
				default:
					sender.sendMessage(Utils.getColoredString("&cInvalid Argument"));
					break;
			}
		}
		return true;
	}

}
