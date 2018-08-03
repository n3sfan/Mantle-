package io.github.lethinh.mantle.event;

import io.github.lethinh.mantle.utils.Utils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Le Thinh
 */
public class FastCropsHarvestEvent implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onHarvest(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		if (event.getItem() != null) {
			return;
		}

		Block block = event.getClickedBlock();
		World world = block.getWorld();

		if (Utils.isGrowable(block.getType()) && block.getType() != Material.SOIL && block.getData() == 7) {
			world.dropItemNaturally(block.getLocation(), new ItemStack(block.getType(), (int) (Math.random() + 1)));
			block.getDrops().forEach(drop -> world.dropItemNaturally(block.getLocation(), drop));
			block.setType(Material.AIR);
		}
	}

}
