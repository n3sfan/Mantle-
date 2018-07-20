package io.lethinh.github.mantle.event;

import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
import org.bukkit.util.BlockIterator;

import io.lethinh.github.mantle.loader.ItemStackLoader;

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

		if (!ItemStackLoader.WATERING_CAN.isSimilar(heldItem)) {
			return;
		}

		event.setCancelled(true);

		Block block = event.getClickedBlock();
		Location blockPos = block.getLocation();
		Material material = block.getType();
		World world = block.getWorld();

		if (checkMaterial(material)) {
			BlockIterator iterator = new BlockIterator(blockPos, 0, 3);
			int[] growthMask = new int[] { 0 }; // Not thread-safe

			iterator.forEachRemaining(neighbor -> {
				if (neighbor.isEmpty() || !checkMaterial(material) || neighbor.getData() == CropState.RIPE.getData()) {
					return;
				}

				if ((growthMask[0] & 1 << neighbor.getData()) == 0) {
					neighbor.setData((byte) (neighbor.getData() + 1));
					growthMask[0] |= 1 << neighbor.getData();
					world.spawnParticle(Particle.WATER_DROP, blockPos, 10, 3, 3, 3);

					if (!player.getGameMode().equals(GameMode.CREATIVE)) {
						heldItem.setDurability((short) (heldItem.getDurability() - 1));
					}
				} else {
					growthMask[0] &= ~(1 << neighbor.getData());
				}
			});
		}
	}

	private boolean checkMaterial(Material material) {
		return material.equals(Material.SOIL) || material.equals(Material.CROPS) || material.equals(Material.SEEDS)
				|| material.equals(Material.BEETROOT_SEEDS) || material.equals(Material.MELON_SEEDS)
				|| material.equals(Material.PUMPKIN_SEEDS);
	}

}
