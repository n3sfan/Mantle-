package io.lethinh.github.mantle;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

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

	public static final String PLUGIN_ID = "mantle"; // Just for enforcements, no conflicts with other plugins
	public static Mantle instance;

	private EventLoader eventLoader;
	private CommandLoader commandLoader;

	@Override
	public void onEnable() {
		// Set instance
		instance = this;

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
		try {
			logger.info("Loading machines data...");
			BlockMachine.loadMachinesData();
			logger.info("Loaded machines data!");
			logger.info("Loading machines inventories data...");
			BlockMachine.loadMachinesInventoriesData();
			logger.info("Loaded machines inventories data!");
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning("An error occured while loading machines or their inventories data!");
		}

//		new BukkitRunnable() {
//			@Override
//			public void run() {
//				if (BlockMachine.MACHINES.isEmpty()) {
//					return;
//				}
//
//				BlockMachine.MACHINES.forEach(machine -> machine.tick(Mantle.this));
//			}
//		}.runTaskTimerAsynchronously(this, 20L, 20L);

		logger.info("Mantle is loaded!");
	}

	@Override
	public void onDisable() {
		Logger logger = getLogger();

		try {
			logger.info("Saving machines data...");
			BlockMachine.saveMachinesData();
			logger.info("Saved machines data!");
			logger.info("Saving machines inventories data...");
			BlockMachine.saveMachinesInventoriesData();
			logger.info("Saved machines inventories data!");
		} catch (IOException e) {
			logger.warning(
					"An error occured while saving machines or their inventories data, contact the developer of Mantle!");
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
