package io.lethinh.github.mantle.loader;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lethinh.github.mantle.Mantle;

/**
 * Created by Le Thinh
 */
public class ItemStackLoader {

	public static final ItemStack WATERING_CAN, TREE_CUTTER, PLANTER, BEDROCK_BREAKER;

	static {
		// Watering Can
		WATERING_CAN = new ItemStack(Material.DIAMOND_HOE);

		{
			ItemMeta itemMeta = WATERING_CAN.getItemMeta();
			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
			itemMeta.addEnchant(Enchantment.WATER_WORKER, 10, true);
			itemMeta.setLocalizedName(Mantle.PLUGIN_ID + " Watering Can");
			itemMeta.setDisplayName("Watering Can");
			WATERING_CAN.setItemMeta(itemMeta);
		}

		// Tree Cutter
		TREE_CUTTER = new ItemStack(Material.DISPENSER);

		{
			ItemMeta itemMeta = TREE_CUTTER.getItemMeta();
			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
			itemMeta.addEnchant(Enchantment.DIG_SPEED, 10, true);
			itemMeta.setLocalizedName(Mantle.PLUGIN_ID + " Tree Cutter");
			itemMeta.setDisplayName("Tree Cutter");
			itemMeta.setLore(Arrays.asList("Place at the center of trees farm", "Only for 7x7 ones"));
			TREE_CUTTER.setItemMeta(itemMeta);
		}

		// Planter
		PLANTER = new ItemStack(Material.DISPENSER);

		{
			ItemMeta itemMeta = PLANTER.getItemMeta();
			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
			itemMeta.addEnchant(Enchantment.DIG_SPEED, 10, true);
			itemMeta.setLocalizedName(Mantle.PLUGIN_ID + " Planter");
			itemMeta.setDisplayName("Planter");
			itemMeta.setLore(Arrays.asList("Place at the center farm", "Only for 7x7 ones"));
			PLANTER.setItemMeta(itemMeta);
		}

		// Bedrock Breaker
		BEDROCK_BREAKER = new ItemStack(Material.DIAMOND_PICKAXE);

		{
			ItemMeta itemMeta = BEDROCK_BREAKER.getItemMeta();
			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 10, true);
			itemMeta.addEnchant(Enchantment.DIG_SPEED, 10, true);
			itemMeta.setLocalizedName(Mantle.PLUGIN_ID + " Bedrock Breaker");
			itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Bedrock Breaker");
			BEDROCK_BREAKER.setItemMeta(itemMeta);
		}
	}

}
