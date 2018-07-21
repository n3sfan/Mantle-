package io.lethinh.github.mantle;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.lethinh.github.mantle.block.BlockMachine;
import io.lethinh.github.mantle.loader.CommandLoader;
import io.lethinh.github.mantle.loader.EventLoader;
import io.lethinh.github.mantle.loader.ILoader;

/**
 * A plugin which is mainly created for agriculture and tech
 *
 * Created by Le Thinh
 */
public class Mantle extends JavaPlugin {

	public static final String PLUGIN_ID = "mantle_"; // Just for enforcements, no conflicts with other plugins
	public static Mantle instance;

	private EventLoader eventLoader;
	private CommandLoader commandLoader;

	@Override
	public void onEnable() {
		// Welcome message
		Logger logger = getLogger();
		logger.info("Mantle - an agricultural with tech involvements is starting...");

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

		// Machine
		logger.info("Loading machines' data...");

		try {
			BlockMachine.loadMachinesData();
			logger.info("Loaded machines' data!");
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning(
					"Machines' data file wasn't found! This may be due to running this plugin for the first time");
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				CopyOnWriteArrayList<BlockMachine> machines = BlockMachine.MACHINES;

				if (machines.isEmpty()) {
					return;
				}

				machines.forEach(machine -> machine.handleUpdate(Mantle.this));
			}
		}.runTaskTimerAsynchronously(this, 20L, 20L);

		// Set instance
		instance = this;
	}

	@Override
	public void onDisable() {
		try {
			BlockMachine.saveMachinesData();
		} catch (IOException e) {
			getLogger().warning("An error encountered while saving machines' data, contact the developers of Mantle!");
			e.printStackTrace();
		}
	}

	/* Getters */
	public EventLoader getEventLoader() {
		return eventLoader;
	}

	public CommandLoader getCommandLoader() {
		return commandLoader;
	}

}
