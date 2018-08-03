package io.github.lethinh.mantle.loader;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.command.MantleAdminCommands;

import java.util.logging.Logger;

/**
 * Created by Le Thinh
 */
public class CommandLoader implements ILoader {

    public CommandLoader() {
        preLoad();
    }

    @Override
    public void load(Mantle plugin) {
        Logger logger = plugin.getLogger();
        logger.info("Registering commands...");

        plugin.getCommand("mantle").setExecutor(new MantleAdminCommands());

        logger.info("Registered commands!");
    }

}
