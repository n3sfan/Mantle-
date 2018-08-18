package io.github.lethinh.mantle.command.impl;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.command.AbstractCommand;
import io.github.lethinh.mantle.command.ExecutionResult;
import io.github.lethinh.mantle.io.AutoSaveHelper;
import io.github.lethinh.mantle.utils.Utils;
import org.bukkit.plugin.Plugin;

public class ReloadCommand extends AbstractCommand {
    public ReloadCommand() {
        super("reload", "", "Reload the configuration", Mantle.PLUGIN_ID + ".reload");
    }

    @Override
    public ExecutionResult now() {
        if (!getSender().hasPermission(getPermission())) {
            return ExecutionResult.NO_PERMISSION;
        }
        Plugin pl = Mantle.instance;
        pl.reloadConfig();
        new AutoSaveHelper(pl, pl.getConfig().getBoolean("auto_save.enable"), pl.getConfig().getLong("auto_save.interval"));
        getSender().sendMessage(Utils.getColoredString("&aReloaded the configuration!"));
        return ExecutionResult.DONT_CARE;
    }
}
