package io.github.lethinh.mantle;

import java.util.HashSet;
import java.util.Set;

import io.github.lethinh.mantle.utils.ItemStackFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Le Thinh
 */
public class MantleItemStacks {

	public static final Set<ItemStack> STACKS = new HashSet<>();

	// Items
	public static final ItemStack WATERING_CAN;
	public static final ItemStack BEDROCK_BREAKER;
	public static final ItemStack ITEM_MAGNET;

	// Blocks
	public static final ItemStack TREE_CUTTER;
	// public static final ItemStack PLANTER;
	public static final ItemStack BLOCK_BREAKER;
	public static final ItemStack BLOCK_PLACER;
	public static final ItemStack MOB_MAGNET;
	public static final ItemStack SMELTERY_BLOCK;
	public static final ItemStack SMELTERY_CONTROLLER;
	public static final ItemStack TELEPORT_RECEIVER;
	public static final ItemStack TELEPORT_TRANSMITTER;
	public static final ItemStack RECIPE_ATTACHER;
	public static final ItemStack AUTO_CRAFTER;

	static {
		// Watering Can
		WATERING_CAN = new ItemStackFactory(new ItemStack(Material.DIAMOND_HOE))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.WATER_WORKER, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "_watering_can")
				.setDisplayName(ChatColor.GREEN + "Watering Can").build();

		STACKS.add(WATERING_CAN);

		// Bedrock Breaker
		BEDROCK_BREAKER = new ItemStackFactory(new ItemStack(Material.DIAMOND_PICKAXE))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES).addEnchant(Enchantment.FIRE_ASPECT, 10)
				.addEnchant(Enchantment.DIG_SPEED, 10).setLocalizedName(Mantle.PLUGIN_ID + "_bedrock_breaker")
				.setDisplayName(ChatColor.LIGHT_PURPLE + "Bedrock Breaker")
				.setLore("Break the unbreakable in a flash").build();

		STACKS.add(BEDROCK_BREAKER);

		// Item Magnet
		ITEM_MAGNET = new ItemStackFactory(new ItemStack(Material.IRON_INGOT))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.SWEEPING_EDGE, 10).setLocalizedName(Mantle.PLUGIN_ID + "_item_magnet")
				.setDisplayName(ChatColor.BLUE + "Item Magnet " + ChatColor.DARK_RED + "(Disabled)")
				.setLore("Right click to air and sneak to toggle", "Pull all items towards you within 5 blocks range")
				.build();

		STACKS.add(ITEM_MAGNET);

		// Tree Cutter
		TREE_CUTTER = new ItemStackFactory(new ItemStack(Material.DISPENSER))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.DIG_SPEED, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "_tree_cutter")
				.setDisplayName(ChatColor.DARK_RED + "Tree Cutter").build();

		STACKS.add(TREE_CUTTER);

		// Planter
//		PLANTER = new ItemStackFactory(new ItemStack(Material.NOTE_BLOCK))
//				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
//				.addEnchant(Enchantment.DIG_SPEED, 10)
//				.setLocalizedName(Mantle.PLUGIN_ID + "_planter").setDisplayName(ChatColor.DARK_GREEN + "Planter")
//				.build();
//
//		STACKS.add(PLANTER);

		// Block Breaker
		BLOCK_BREAKER = new ItemStackFactory(new ItemStack(Material.DISPENSER))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.DIG_SPEED, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "_block_breaker")
				.setDisplayName(ChatColor.LIGHT_PURPLE + "Block Breaker").build();

		STACKS.add(BLOCK_BREAKER);

		// Block Placer
		BLOCK_PLACER = new ItemStackFactory(new ItemStack(Material.DISPENSER))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.FROST_WALKER, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "_block_placer")
				.setDisplayName(ChatColor.LIGHT_PURPLE + "Block Placer").build();

		STACKS.add(BLOCK_PLACER);

		// Mob Magnet
		MOB_MAGNET = new ItemStackFactory(new ItemStack(Material.NOTE_BLOCK))
				.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
				.addEnchant(Enchantment.KNOCKBACK, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "_mob_magnet")
				.setDisplayName(ChatColor.LIGHT_PURPLE + "MobMagnet").setLore("Pull all mobs towards this block")
				.build();

		STACKS.add(MOB_MAGNET);

		// Smeltery Block
		SMELTERY_BLOCK = new ItemStackFactory(new ItemStack(Material.NETHER_BRICK))
				.addEnchant(Enchantment.FIRE_ASPECT, 10).setLocalizedName(Mantle.PLUGIN_ID + "_smeltery_block")
				.setDisplayName(ChatColor.DARK_GRAY + "Smeltery Block").build();

		STACKS.add(SMELTERY_BLOCK);

		// Smeltery Controller
		SMELTERY_CONTROLLER = new ItemStackFactory(new ItemStack(Material.OBSERVER))
				.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "_smeltery_controller")
				.setDisplayName(ChatColor.DARK_RED + "Smeltery Controller")
				.build();

		STACKS.add(SMELTERY_CONTROLLER);

		// Teleport Receiver
		TELEPORT_RECEIVER = new ItemStackFactory(new ItemStack(Material.BONE_BLOCK))
				.addEnchant(Enchantment.ARROW_KNOCKBACK, 10).setLocalizedName(Mantle.PLUGIN_ID + "_teleport_receiver")
				.setDisplayName(ChatColor.LIGHT_PURPLE + "Teleport Receiver")
				.build();

		STACKS.add(TELEPORT_RECEIVER);

		// Teleport Transmitter
		TELEPORT_TRANSMITTER = new ItemStackFactory(new ItemStack(Material.BONE_BLOCK))
				.addEnchant(Enchantment.ARROW_KNOCKBACK, 10)
				.setLocalizedName(Mantle.PLUGIN_ID + "_teleport_transmitter")
				.setDisplayName(ChatColor.DARK_RED + "Teleport Transmitter")
				.build();

		STACKS.add(TELEPORT_TRANSMITTER);

		// Recipe Attacher
		RECIPE_ATTACHER = new ItemStackFactory(new ItemStack(Material.DROPPER)).addEnchant(Enchantment.DIG_SPEED, 10)
				.addFlags(ItemFlag.HIDE_ENCHANTS).setLocalizedName(Mantle.PLUGIN_ID + "_recipe_attacher")
				.setDisplayName(ChatColor.DARK_BLUE + "Recipe Attacher").build();

		STACKS.add(RECIPE_ATTACHER);

		// Auto Crafter
		AUTO_CRAFTER = new ItemStackFactory(new ItemStack(Material.WORKBENCH)).addEnchant(Enchantment.DIG_SPEED, 10)
				.addFlags(ItemFlag.HIDE_ENCHANTS).setLocalizedName(Mantle.PLUGIN_ID + "_auto_crafter")
				.setDisplayName(ChatColor.DARK_PURPLE + "Auto Crafter").build();

		STACKS.add(AUTO_CRAFTER);
	}

}
