package io.github.lethinh.mantle.loader;

import java.util.logging.Logger;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.event.BedrockBreakEvent;
import io.github.lethinh.mantle.event.FastLeafDecayEvent;
import io.github.lethinh.mantle.event.ItemMagnetToggleEvent;
import io.github.lethinh.mantle.event.MachineChangedEvent;
import io.github.lethinh.mantle.event.WateringCanEvent;

/**
 * Created by Le Thinh
 */
public class EventLoader implements ILoader {

	public EventLoader() {
		preLoad();
	}

	@Override
	public void load(Mantle plugin) {
		Logger logger = plugin.getLogger();
		logger.info("Registering events...");

		registerEvents(new FastLeafDecayEvent(), plugin);
		registerEvents(new WateringCanEvent(), plugin);
		registerEvents(new MachineChangedEvent(), plugin);
		registerEvents(new BedrockBreakEvent(), plugin);
		registerEvents(new ItemMagnetToggleEvent(), plugin);
		// registerEvents(new FastCropsHarvestEvent(), plugin);

		logger.info("Registered events!");
	}

	private static void registerEvents(Listener listener, Mantle plugin) {
		PluginManager pluginManager = plugin.getServer().getPluginManager();
		pluginManager.registerEvents(listener, plugin);
	}

}
