package io.github.lethinh.mantle.utils;

import io.github.lethinh.mantle.Mantle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.IntStream;

/**
 * Created by Le Thinh
 */
public final class Utils {

    private Utils() {

    }

    /* Internal */
    public static NamespacedKey prefixNamespaced(String key) {
        return new NamespacedKey(Mantle.instance, key);
    }

    public static String encryptBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String decryptBase64(String text) {
        return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
    }

    /* Inventory */
    public static boolean isFull(Inventory inventory) {
        return isFull(inventory, inventory.getSize());
    }

    public static boolean isFull(Inventory inventory, int realSlots) {
        if (realSlots > inventory.getSize()) {
            realSlots = inventory.getSize();
        }

        return IntStream.range(0, realSlots)
                .allMatch(i -> inventory.getItem(i) != null && inventory.getItem(i).getAmount() == 64);
    }

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
                + location.getBlockZ();
    }

    public static Location deserializeLocation(String serialization) {
        try {
            String[] split = serialization.split(",");

            if (split.length == 0) {
                return null;
            }

            return new Location(Bukkit.getWorld(split[0]), NumberConversions.toInt(split[1]),
                    NumberConversions.toInt(split[2]), NumberConversions.toInt(split[3]));
        } catch (Throwable t) {
            return null;
        }
    }

    /* World */
    public static Location stimulateAdd(Location location, double x, double y, double z) {
        return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()).add(x, y, z);
    }

    public static Location stimulateSubtract(Location location, double x, double y, double z) {
        return stimulateAdd(location, -x, -y, -z);
    }

    public static BlockFace getBlockFaceFromPlayer(Location loc, Player player) {
        Location playerLoc = player.getLocation();

        // Check for vertical faces
        if (Math.abs(playerLoc.getX() - (loc.getX() + 0.5D)) < 2D
                && Math.abs(playerLoc.getZ() - (loc.getZ() + 0.5D)) < 2D) {
            double eyesPos = playerLoc.getY() + player.getEyeHeight();

            if (eyesPos - loc.getY() > 2D) {
                return BlockFace.UP;
            }

            if (loc.getY() - eyesPos > 0D) {
                return BlockFace.DOWN;
            }
        }

        // Or horizontal faces
        BlockFace[] horizontals = new BlockFace[]{BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST};
        int index = NumberConversions.floor(playerLoc.getYaw() * 4F / 360F + 0.5D) & 3;
        return horizontals[Math.abs(index % horizontals.length)].getOppositeFace();
    }

    @SuppressWarnings("deprecation")
    public static boolean isBlockEqualStack(Block block, ItemStack stack) {
        return block != null && !block.isEmpty() && stack != null && stack.getAmount() > 0
                && block.getType() == stack.getType() && block.getData() == stack.getData().getData();
    }

    public static boolean isGrowable(Material material) {
        return material == Material.SOIL || material == Material.CROPS || material == Material.SEEDS
                || material == Material.CARROT || material == Material.BEETROOT_BLOCK || material == Material.MELON_STEM
                || material == Material.PUMPKIN_STEM;
    }

    public static boolean areStacksEqualIgnoreDurabilityAndAmount(ItemStack stackA, ItemStack stackB) {
        return stackA != null && stackB != null && stackA.getType() == stackB.getType() && stackA.hasItemMeta()
                && stackB.hasItemMeta() && Bukkit.getItemFactory().equals(stackA.getItemMeta(), stackB.getItemMeta());
    }

    public static String getColoredString(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static BlockFace[] getMainFaces() {
        return new BlockFace[]{BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST,
                BlockFace.EAST};
    }

    public static Location offsetLocation(Location location, BlockFace face) {
        return location.add(face.getModX(), face.getModY(), face.getModZ());
    }

    public static BlockFace getEntityDirection(Location location) {
        BlockFace dir = BlockFace.NORTH;
        float f = Float.MIN_VALUE;

        for (BlockFace face : getMainFaces()) {
            float f1 = (float) (location.getX() * face.getModX() + location.getY() * face.getModY()
                    + location.getZ() * face.getModZ());

            if (f1 > f) {
                f = f1;
                dir = face;
            }
        }

        return dir;
    }

}
