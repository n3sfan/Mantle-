package io.github.lethinh.mantle.event;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

/**
 * Created by Le Thinh
 */
public class FastLeafDecayEvent implements Listener {

	@EventHandler
	public void onLeafDecay(LeavesDecayEvent event) {
		Block block = event.getBlock();
		World world = block.getWorld();
		block.setType(Material.AIR);
		block.getDrops().forEach(stack -> world.dropItemNaturally(block.getLocation(), stack));
	}

}
