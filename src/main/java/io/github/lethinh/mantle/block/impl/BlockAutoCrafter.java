package io.github.lethinh.mantle.block.impl;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.block.GenericMachine;
import io.github.lethinh.mantle.utils.ItemStackFactory;
import io.github.lethinh.mantle.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.*;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Le Thinh
 */
public class BlockAutoCrafter extends BlockMachine {

    private static final short PLAN_INPUT_SLOT = 0;
    private static final short RESULT_SLOT = 9;
    private static final short INGREDIENTS_START_SLOT = 27;
    private static final short INGREDIENTS_END_SLOT = 45;

    public BlockAutoCrafter(Block block, String... players) {
        super(GenericMachine.AUTO_CRAFTER, block, 45, "Auto Crafter", players);

        // Inventory
        for (int i = PLAN_INPUT_SLOT + 1; i < RESULT_SLOT; ++i) {
            inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
        }

        for (int i = RESULT_SLOT + 1; i < INGREDIENTS_START_SLOT; ++i) {
            inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
        }
    }

    @Override
    public void handleUpdate(Mantle plugin) {
        runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY * 3L, DEFAULT_PERIOD);
    }

    @Override
    public void work() {
        ItemStack plan = inventory.getItem(PLAN_INPUT_SLOT);
        ItemStack toCraft = inventory.getItem(RESULT_SLOT);

        if (plan == null) {
            return;
        }

        String display = Utils.decryptBase64(plan.getItemMeta().getDisplayName());

        if (StringUtils.isBlank(display)) {
            return;
        }

        String[] split = display.split(":");

        if (split.length < 2) {
            return;
        }

        String namespace = null;
        String key = null;

        try {
            namespace = split[0];
            key = split[1];
        } catch (Throwable e) {
        }

        Iterator<Recipe> iterator = Bukkit.recipeIterator();

        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();

            if (!(recipe instanceof Keyed)) {
                continue;
            }

            Keyed keyed = (Keyed) recipe;
            NamespacedKey namespacedKey = keyed.getKey();

            if (!namespacedKey.getNamespace().equalsIgnoreCase(namespace)
                    || !namespacedKey.getKey().equalsIgnoreCase(key)) {
                continue;
            }

            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe shaped = (ShapedRecipe) recipe;

                if (extractIngredients(shaped.getIngredientMap().values())) {
                    output(toCraft, shaped.getResult().clone());
                }
            } else if (recipe instanceof ShapelessRecipe) {
                ShapelessRecipe shapeless = (ShapelessRecipe) recipe;

                if (extractIngredients(shapeless.getIngredientList())) {
                    output(toCraft, shapeless.getResult().clone());
                }
            }
        }
    }

    @Override
    public void dropItems(ItemStack stack) {
        World world = block.getWorld();
        Location location = block.getLocation();
        world.dropItemNaturally(location, stack);

        ItemStack plan = inventory.getItem(PLAN_INPUT_SLOT);
        ItemStack result = inventory.getItem(RESULT_SLOT);

        if (plan != null) {
            world.dropItemNaturally(location, plan);
        }

        if (result != null) {
            world.dropItemNaturally(location, result);
        }

        for (int i = INGREDIENTS_START_SLOT; i < INGREDIENTS_END_SLOT; ++i) {
            ItemStack ingredient = inventory.getItem(i);

            if (ingredient != null) {
                world.dropItemNaturally(location, ingredient);
            }
        }
    }

    @SuppressWarnings("SuspiciousListRemoveInLoop")
    private boolean extractIngredients(Collection<ItemStack> collection) {
        if (collection.isEmpty() || inventory.getItem(RESULT_SLOT) != null && inventory.getItem(RESULT_SLOT).getAmount() == 64) {
            return false;
        }

        Set<ItemStack> ingredients = new HashSet<>(collection);
        Map<Integer, ItemStack> toTake = new HashMap<>();

        for (ItemStack ingredient : ingredients) {
            int typeMask = 0;

            for (int j = INGREDIENTS_START_SLOT; j < INGREDIENTS_END_SLOT; ++j) {
                if (toTake.size() == ingredients.size()) {
                    break;
                }

                ItemStack stack = inventory.getItem(j);

                if (stack == null) {
                    continue;
                }

                @SuppressWarnings("deprecation")
                int id = stack.getTypeId();

                if (ingredient.getType() != stack.getType() || (typeMask & 1 << id) != 0) {
                    continue;
                }

                if (stack.getAmount() < ingredient.getAmount()) {
                    continue;
                }

                ItemStack clone = stack.clone();
                toTake.put(j, new ItemStackFactory(clone).setAmount(clone.getAmount() - getNeededAmount(collection, ingredient)).build());
                typeMask |= 1 << id;
            }
        }

        if (toTake.size() >= ingredients.size()) {
            for (Entry<Integer, ItemStack> entry : toTake.entrySet()) {
                int slot = entry.getKey();
                ItemStack stack = entry.getValue();

                if (stack == null) {
                    continue;
                }

                inventory.setItem(slot, stack);
            }

            return true;
        }

        return false;
    }

    private void output(ItemStack current, ItemStack output) {
        if (current == null) {
            inventory.setItem(RESULT_SLOT, output);
        } else if (current.getType() == output.getType() && current.getAmount() < 64) {
            ItemStack increase = new ItemStackFactory(current).setAmount(current.getAmount() + output.getAmount())
                    .build();
            inventory.setItem(RESULT_SLOT, increase);
        }
    }

    private int getNeededAmount(Collection<ItemStack> ingredients, ItemStack stack) {
        return ingredients.stream().filter(ingredient -> ingredient.isSimilar(stack)).mapToInt(ItemStack::getAmount).sum();
    }

    /* Callbacks */
    @Override
    public boolean onInventoryInteract(ClickType clickType, InventoryAction action, SlotType slotType,
                                       ItemStack clicked, ItemStack cursor, int slot, InventoryView view) {
        if (clicked == null || clicked.getAmount() == 0 || Material.STAINED_GLASS_PANE != clicked.getType()) {
            return false;
        }

        return slot > PLAN_INPUT_SLOT && slot < RESULT_SLOT || slot > RESULT_SLOT && slot < INGREDIENTS_START_SLOT;
    }

}
