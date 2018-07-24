package io.lethinh.github.mantle.command;


import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractCommand {
    private String permission;
    private String[] args;
    private CommandSender sender;
    private Mantle plugin = JavaPlugin.getPlugin(Mantle.class);

    public AbstractCommand(String permission, String[] args, CommandSender sender) {
        this.permission = permission;
        this.args = args;
        this.sender = sender;
    }

    public abstract ExecutionResult now();

    public void execute(){
        switch (now()){
            case MISSINGARGS:
                //TODO Do stuff
                return;
            case NOPERMISSION:
                sender.sendMessage(Utils.getColoredString("&4Missing Permission: &c" + permission));
                return;
            case NOPLAYER:
                sender.sendMessage(Utils.getColoredString("&4Player is not excist or isn't online"));
                return;
            case CONSOLENOTPERMITTED:
                sender.sendMessage(Utils.getColoredString("&4This command is not available to console"));
                return;
        }
    }

    public String getPermission() {
        return permission;
    }

    public String[] getArgs() {
        return args;
    }

    public String getFormat(){
        return null;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Mantle getPlugin() {
        return plugin;
    }
}
