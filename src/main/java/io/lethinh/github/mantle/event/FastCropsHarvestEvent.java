package io.lethinh.github.mantle.event;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.utils.Utils;

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

		if (Utils.isGrowable(block.getType()) && !block.getType().equals(Material.SOIL)
				&& block.getData() == CropState.RIPE.getData()) {
			block.getDrops().forEach(drop -> {
				world.dropItemNaturally(block.getLocation(), new ItemStack(block.getType(), (int) Math.random() + 1));
				world.dropItemNaturally(block.getLocation(), drop);
			});

			block.setType(Material.AIR);
		}
	}

}
