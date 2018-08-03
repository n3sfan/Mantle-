package io.github.lethinh.mantle.utils;

import java.util.Arrays;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Le Thinh
 */
public class ItemStackFactory {

	private final ItemStack stack;
	private final ItemMeta itemMeta;

	public ItemStackFactory(ItemStack stack) {
		this.stack = stack;
		this.itemMeta = stack.getItemMeta();
	}

	public ItemStackFactory setAmount(int amount) {
		stack.setAmount(amount);
		return this;
	}

	public ItemStackFactory setDisplayName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}

	public ItemStackFactory setLocalizedName(String name) {
		itemMeta.setLocalizedName(name);
		return this;
	}

	public ItemStackFactory addEnchant(Enchantment enchantment, int level) {
		itemMeta.addEnchant(enchantment, level, true);
		return this;
	}

	public ItemStackFactory setLore(String... lores) {
		itemMeta.setLore(Arrays.asList(lores));
		return this;
	}

	public ItemStackFactory addFlags(ItemFlag... flags) {
		itemMeta.addItemFlags(flags);
		return this;
	}

	public ItemStack build() {
		stack.setItemMeta(itemMeta);
		return stack;
	}

}
