package io.lethinh.github.mantle;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.PlayerInventory;
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

	public static final String PLUGIN_ID = "mantle"; // Just for enforcements, no conflicts with other plugins
	public static final String VERSION = "1.0.1";
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

		// Item Magnet
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : getServer().getOnlinePlayers()) {
					PlayerInventory inventory = player.getInventory();

					if (inventory.getItemInMainHand() != null && inventory.getItemInMainHand().getItemMeta() != null
							&& MantleItemStacks.ITEM_MAGNET.getItemMeta().getLocalizedName()
									.equals(inventory.getItemInMainHand().getItemMeta().getLocalizedName())) {
						if (inventory.getItemInMainHand().getItemMeta().getDisplayName().contains("Disabled")) {
							continue;
						}

						for (Entity entity : player.getNearbyEntities(5D, 5D, 5D)) {
							if (entity.isDead() || !(entity instanceof Item)) {
								continue;
							}

							Item item = (Item) entity;
							item.setPickupDelay(0);
							item.teleport(player, TeleportCause.PLUGIN);
						}
					} else if (inventory.getItemInOffHand() != null
							&& inventory.getItemInOffHand().getItemMeta() != null
							&& MantleItemStacks.ITEM_MAGNET.getItemMeta().getLocalizedName()
									.equals(inventory.getItemInOffHand().getItemMeta().getLocalizedName())) {
						if (inventory.getItemInOffHand().getItemMeta().getDisplayName().contains("Disabled")) {
							continue;
						}

						for (Entity entity : player.getNearbyEntities(5D, 5D, 5D)) {
							if (entity.isDead() || !(entity instanceof Item)) {
								continue;
							}

							Item item = (Item) entity;
							item.setPickupDelay(0);
							item.teleport(player, TeleportCause.PLUGIN);
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(this, 20L, 10L);

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
