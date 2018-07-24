package io.lethinh.github.mantle.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.NumberConversions;

import io.lethinh.github.mantle.nbt.Constants;
import io.lethinh.github.mantle.nbt.NBTTagCompound;
import io.lethinh.github.mantle.nbt.NBTTagList;

/**
 * Created by Le Thinh
 */
public final class Utils {

	private Utils() {

	}

	/* Inventory */
	public static boolean isFull(Inventory inventory) {
		return IntStream.range(0, inventory.getSize())
				.allMatch(i -> inventory.getItem(i) != null && inventory.getItem(i).getAmount() == 64);
	}

	public static boolean isNotEmpty(Inventory inventory) {
		return IntStream.range(0, inventory.getSize()).anyMatch(i -> inventory.getItem(i) != null);
	}

	public static NBTTagCompound serializeInventory(Inventory inventory) {
		NBTTagList nbtTagList = new NBTTagList();

		for (int i = 0; i < inventory.getSize(); ++i) {
			ItemStack stack = inventory.getItem(i);

			if (stack != null) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setInteger("Slot", i);
				writeNBTToStack(stack, itemTag);
				nbtTagList.appendTag(itemTag);
			}
		}

		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Items", nbtTagList);
		nbt.setInteger("Size", inventory.getSize());
		nbt.setString("Title", inventory.getTitle());
		return nbt;
	}

	public static Inventory deserializeInventory(NBTTagCompound nbt) {
		Inventory inventory = Bukkit.createInventory(null, nbt.getInteger("Size"), nbt.getString("Title"));
		NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
			int slot = itemTags.getInteger("Slot");

			if (slot >= 0 && slot < inventory.getSize()) {
				inventory.setItem(slot, readStackFromNBT(itemTags));
			}
		}

		return inventory;
	}

	@SuppressWarnings("deprecation")
	public static NBTTagCompound writeNBTToStack(ItemStack stack, NBTTagCompound nbt) {
		nbt.setInteger("id", stack.getTypeId());
		nbt.setByte("Amount", (byte) stack.getAmount());
		nbt.setShort("Damage", stack.getDurability());

		NBTTagCompound stackTags = new NBTTagCompound();
		ItemMeta itemMeta = stack.getItemMeta();

		if (itemMeta.hasLocalizedName()) {
			stackTags.setString("localized", itemMeta.getLocalizedName());
		}

		if (itemMeta.hasDisplayName()) {
			stackTags.setString("display", itemMeta.getDisplayName());
		}

		if (itemMeta.hasLore()) {
			stackTags.setString("lore", itemMeta.getLore().stream().collect(Collectors.joining(",")));
		}

		if (itemMeta.hasEnchants()) {
			NBTTagList enchantments = new NBTTagList();

			for (Entry<Enchantment, Integer> ench : itemMeta.getEnchants().entrySet()) {
				NBTTagCompound enchTag = new NBTTagCompound();
				enchTag.setInteger("id", ench.getKey().getId());
				enchTag.setInteger("level", ench.getValue());
				enchantments.appendTag(enchTag);
			}

			stackTags.setTag("enchants", enchantments);
		}

		nbt.setTag("tag", stackTags);
		return nbt;
	}

	public static ItemStack readStackFromNBT(NBTTagCompound nbt) {
		@SuppressWarnings("deprecation")
		Material material = Material.getMaterial(nbt.getInteger("id"));
		int amount = nbt.getInteger("Amount");
		short damage = nbt.getShort("Damage");
		ItemStack ret = new ItemStack(material, amount, damage);

		if (nbt.hasKey("tag", Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound stackTags = nbt.getCompoundTag("tag");

			if (stackTags.hasNoTags()) {
				return ret;
			}

			ItemMeta meta = ret.getItemMeta();

			if (stackTags.hasKey("localized", Constants.NBT.TAG_STRING)) {
				meta.setLocalizedName(stackTags.getString("localized"));
			}

			if (stackTags.hasKey("display", Constants.NBT.TAG_STRING)) {
				meta.setDisplayName(stackTags.getString("display"));
			}

			if (stackTags.hasKey("lore", Constants.NBT.TAG_STRING)) {
				List<String> lore = Arrays.asList(stackTags.getString("lore").split(","));
				meta.setLore(lore);
			}

			ret.setItemMeta(meta);
		}

		return ret;
	}

	/* Serialize & Deserialize */
	public static String serializeLocation(Location location) {
		return location.getWorld().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_"
				+ location.getBlockZ();
	}

	public static Location deserializeLocation(String serialization) {
		String[] split = serialization.split("_");
		return new Location(Bukkit.getWorld(split[0]), NumberConversions.toInt(split[1]),
				NumberConversions.toInt(split[2]), NumberConversions.toInt(split[3]));
	}

	/* World */
	public static boolean isGrowable(Material material) {
		return material == Material.SOIL || material == Material.CROPS
				|| material == Material.SEEDS
				|| material == Material.BEETROOT_SEEDS || material == Material.MELON_SEEDS
				|| material == Material.PUMPKIN_SEEDS;
	}

	public static boolean areStacksEqualIgnoreDurability(ItemStack stackA, ItemStack stackB) {
		return stackA != null && stackB != null && stackA.getType() == stackB.getType() && stackA.hasItemMeta()
				&& stackB.hasItemMeta() && Bukkit.getItemFactory().equals(stackA.getItemMeta(), stackB.getItemMeta());
	}

	public static Collection<Block> getSurroundingBlocks(Block center, int dist, boolean yLayer) {
		return getSurroundingBlocks(center, dist, dist, dist, yLayer, block -> !block.isEmpty() && !block.isLiquid());
	}

	public static Collection<Block> getSurroundingBlocks(Block center, int xDist, int yDist, int zDist, boolean yLayer,
			Predicate<Block> predicate) {
		List<Block> ret = new ArrayList<>();

		for (int x = -xDist; x <= xDist; ++x) {
			for (int z = -zDist; z <= zDist; ++z) {
				if (yLayer) {
					for (int y = 0; y <= yDist; ++y) {
						Block neighborBlock = center.getRelative(x, y, z);

						if (predicate == null || predicate.test(neighborBlock)) {
							ret.add(neighborBlock);
						}
					}
				} else {
					Block neighborBlock = center.getRelative(x, 0, z);

					if (predicate == null || predicate.test(neighborBlock)) {
						ret.add(neighborBlock);
					}
				}
			}
		}

		return Collections.unmodifiableCollection(ret);
	}


	public static String getColoredString(String s){
	    return ChatColor.translateAlternateColorCodes('&', s);
    }
}
