package io.lethinh.github.mantle;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.lethinh.github.mantle.block.BlockMachine;
import io.lethinh.github.mantle.loader.CommandLoader;
import io.lethinh.github.mantle.loader.EventLoader;
import io.lethinh.github.mantle.loader.ILoader;

/**
 * Created by Le Thinh
 */
public class Mantle extends JavaPlugin {

    public static final String PLUGIN_ID = "mantle"; // Just for enforcements, no conflicts with other plugins

    private EventLoader eventLoader;
    private CommandLoader commandLoader;

    @Override
    public void onEnable() {
        // Welcome message
        Logger logger = getLogger();
        logger.info(ChatColor.GOLD + "Mantle -" + ChatColor.LIGHT_PURPLE + " an agricultural with tech involvements"
                + ChatColor.WHITE + " is starting...");

        // Load
        eventLoader = new EventLoader();
        commandLoader = new CommandLoader();

        ILoader.LOADERS.forEach(loader -> {
            try {
                loader.load(this);
            } catch (Throwable e) {
                e.printStackTrace();
                logger.warning("Failed to load " + loader.getClass().getSimpleName() + "! Plugin will not work well.");
            }
        });

        // Machine tick
        new BukkitRunnable() {
            @Override
            public void run() {
                CopyOnWriteArrayList<BlockMachine> machines = BlockMachine.MACHINES;

                if (machines.isEmpty()) {
                    return;
                }

                machines.forEach(machine -> machine.tick(Mantle.this));
            }
        }.runTaskTimer(this, 20L, 20L);
    }

    public EventLoader getEventLoader() {
        return eventLoader;
    }

    public CommandLoader getCommandLoader() {
        return commandLoader;
    }

}