package io.lethinh.github.mantle.event;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.MantleItemStacks;

/**
 * Created by Le Thinh
 */
public class BedrockBreakEvent implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockDamageEvent event) {
		ItemStack stack = event.getItemInHand();
		Player player = event.getPlayer();
		Block block = event.getBlock();
		World world = player.getWorld();
		Location blockPos = block.getLocation();

		if (!MantleItemStacks.BEDROCK_BREAKER.equals(stack) || Material.BEDROCK != block.getType()
				|| player.getGameMode() != GameMode.CREATIVE) {
			return;
		}

		world.playEffect(blockPos, Effect.STEP_SOUND, block.getTypeId());
		world.dropItemNaturally(blockPos, new ItemStack(Material.BEDROCK));
		block.setType(Material.AIR);
	}

}
