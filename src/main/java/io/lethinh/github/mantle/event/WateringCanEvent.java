package io.lethinh.github.mantle.event;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.MantleItemStacks;
import io.lethinh.github.mantle.utils.AreaManager;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Created by Le Thinh
 */
public class WateringCanEvent implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack heldItem = event.getItem();

		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		if (!Utils.areStacksEqualIgnoreDurability(MantleItemStacks.WATERING_CAN, heldItem)) {
			return;
		}

		Block block = event.getClickedBlock();
		Material material = block.getType();
		World world = block.getWorld();

		if (Utils.isGrowable(material) || Material.SAPLING.equals(material)) {
			int growthMask = 0;
			AreaManager manager = new AreaManager(block, 1, 1, 1, true);
			manager.scanBlocks();

			if (manager.noBlocks()) {
				return;
			}

			for (Block surrounding : manager.getScannedBlocks()) {
				if (surrounding.isEmpty() || !Utils.isGrowable(material) || surrounding.getData() == 7) {
					continue;
				}

				if ((growthMask & 1 << surrounding.getData()) == 0) {
					surrounding.setData((byte) (surrounding.getData() + 1));
					growthMask |= 1 << surrounding.getData();
					world.spawnParticle(Particle.WATER_DROP, surrounding.getLocation(), 30, 3, 3, 3);

					if (!player.getGameMode().equals(GameMode.CREATIVE)) {
						heldItem.setDurability((short) (heldItem.getDurability() - 1));
					}
				} else {
					growthMask &= ~(1 << surrounding.getData());
				}
			}
		}
	}

}
