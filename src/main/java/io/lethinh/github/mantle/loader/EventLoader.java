package io.lethinh.github.mantle.loader;

import java.util.logging.Logger;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.event.BedrockBreakEvent;
import io.lethinh.github.mantle.event.FastLeafDecayEvent;
import io.lethinh.github.mantle.event.MachineChangedEvent;
import io.lethinh.github.mantle.event.WateringCanEvent;

/**
 * Created by Le Thinh
 */
public class EventLoader implements ILoader {

	public EventLoader() {
		preLoad();
	}

	@Override
	public void load(Mantle plugin) throws Exception {
		Logger logger = plugin.getLogger();
		logger.info("Registering events...");

		registerEvents(new FastLeafDecayEvent(), plugin);
		registerEvents(new WateringCanEvent(), plugin);
		registerEvents(new MachineChangedEvent(), plugin);
		registerEvents(new BedrockBreakEvent(), plugin);
		// registerEvents(new FastCropsHarvestEvent(), plugin);

		logger.info("Registered events!");
	}

	private static void registerEvents(Listener listener, Mantle plugin) {
		PluginManager pluginManager = plugin.getServer().getPluginManager();
		pluginManager.registerEvents(listener, plugin);
	}

}
