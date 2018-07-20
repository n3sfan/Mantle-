package io.lethinh.github.mantle.loader;

import org.bukkit.plugin.PluginManager;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.event.BedrockBreakEvent;
import io.lethinh.github.mantle.event.MachineChangedEvent;
import io.lethinh.github.mantle.event.FastLeafDecayEvent;
import io.lethinh.github.mantle.event.WateringCanEvent;

/**
 * Created by Le Thinh
 */
public class EventLoader implements ILoader {

	public EventLoader() {
		LOADERS.add(this);
	}

	@Override
	public void load(Mantle plugin) throws Throwable {
		PluginManager pluginManager = plugin.getServer().getPluginManager();

		// Register event listeners
		pluginManager.registerEvents(new FastLeafDecayEvent(), plugin);
		pluginManager.registerEvents(new WateringCanEvent(), plugin);
		pluginManager.registerEvents(new MachineChangedEvent(), plugin);
		pluginManager.registerEvents(new BedrockBreakEvent(), plugin);
	}

}
