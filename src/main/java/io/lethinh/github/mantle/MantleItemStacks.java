package io.lethinh.github.mantle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import io.lethinh.github.mantle.utils.ItemStackFactory;

/**
 * Created by Le Thinh
 */
public class MantleItemStacks {

	public static final List<ItemStack> STACKS = new ArrayList<>();

	// Items
	public static final ItemStack WATERING_CAN;

	// Blocks
	public static final ItemStack TREE_CUTTER;
	public static final ItemStack PLANTER;
	public static final ItemStack BEDROCK_BREAKER;
	public static final ItemStack BLOCK_BREAKER;
	public static final ItemStack BLOCK_PLACER;

	static {
		// Watering Can
		WATERING_CAN = new ItemStackFactory(new ItemStack(Material.DIAMOND_HOE))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.WATER_WORKER, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "watering_can")
				.setDisplayName(ChatColor.GREEN + "Watering Can").build();

		STACKS.add(WATERING_CAN);

		// Tree Cutter
		TREE_CUTTER = new ItemStackFactory(new ItemStack(Material.DISPENSER))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.DIG_SPEED, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "tree_cutter")
				.setDisplayName(ChatColor.DARK_RED + "Tree Cutter").build();

		STACKS.add(TREE_CUTTER);

		// Planter
		PLANTER = new ItemStackFactory(new ItemStack(Material.NOTE_BLOCK))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.DIG_SPEED, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "planter").setDisplayName(ChatColor.DARK_GREEN + "Planter")
				.build();

		STACKS.add(PLANTER);

		// Bedrock Breaker
		BEDROCK_BREAKER = new ItemStackFactory(new ItemStack(Material.DIAMOND_PICKAXE))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES).addEnchant(Enchantment.FIRE_ASPECT, 10)
				.addEnchant(Enchantment.DIG_SPEED, 10).setLocalizedName(Mantle.PLUGIN_ID + "bedrock_breaker")
				.setDisplayName(ChatColor.LIGHT_PURPLE + "Bedrock Breaker")
				.setLore("Break the unbreakable in a flash").build();

		STACKS.add(BEDROCK_BREAKER);

		// Block Breaker
		BLOCK_BREAKER = new ItemStackFactory(new ItemStack(Material.DISPENSER))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.DIG_SPEED, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "block_breaker")
				.setDisplayName(ChatColor.LIGHT_PURPLE + "Block Breaker").build();

		STACKS.add(BLOCK_BREAKER);

		// Block Placer
		BLOCK_PLACER = new ItemStackFactory(new ItemStack(Material.DISPENSER))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.FROST_WALKER, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "block_placer")
				.setDisplayName(ChatColor.LIGHT_PURPLE + "Block Placer").build();

		STACKS.add(BLOCK_PLACER);
	}

}
