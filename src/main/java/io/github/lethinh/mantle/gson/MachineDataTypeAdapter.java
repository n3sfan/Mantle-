/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.gson;

import com.google.gson.*;
import io.github.lethinh.mantle.gson.direct.CustomData;
import io.github.lethinh.mantle.gson.direct.CustomDataManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemFlag;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class MachineDataTypeAdapter implements JsonSerializer<MachineData>, JsonDeserializer<MachineData> {

    @Override
    public MachineData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        MachineData machineData = new MachineData();

        machineData.setInvSize(object.getAsJsonPrimitive("invSize").getAsInt());
        machineData.setInvTitle(object.getAsJsonPrimitive("invTitle").getAsString());

        // ItemStacks in Inventory
        JsonArray array = object.getAsJsonArray("stacks");
        StackData[] stacks = new StackData[array.size()];

        for (int i = 0; i < stacks.length; ++i) {
            JsonElement element = array.get(i);
            JsonObject stackObject = element.getAsJsonObject();
            StackData stack = new StackData();

            // ItemStack
            stack.setSlot(stackObject.getAsJsonPrimitive("slot").getAsInt());

            // Needed elements to make an ItemStack
            stack.setType(stackObject.getAsJsonPrimitive("type").getAsString());
            stack.setAmount(stackObject.getAsJsonPrimitive("amount").getAsInt());
            stack.setData(stackObject.getAsJsonPrimitive("data").getAsByte());

            if (stackObject.has("damage")) {
                stack.setDamage(stackObject.getAsJsonPrimitive("damage").getAsShort());
            }

            // ItemMeta
            if (stackObject.has("displayName")) {
                stack.setDisplayName(stackObject.getAsJsonPrimitive("displayName").getAsString());
            }

            if (stackObject.has("localizedName")) {
                stack.setLocalizedName(stackObject.getAsJsonPrimitive("localizedName").getAsString());
            }

            if (stackObject.has("lore")) {
                JsonArray loreArray = stackObject.getAsJsonArray("lore");
                String[] lore = IntStream.range(0, loreArray.size()).mapToObj(j -> loreArray.get(j).getAsString()).toArray(String[]::new);
                stack.setLore(lore);
            }

            // Enchantments
            if (stackObject.has("enchants")) {
                JsonArray enchantArray = stackObject.getAsJsonArray("enchants");
                List<EnchantEntry> enchants = new ArrayList<>();

                for (JsonElement enchantElement : enchantArray) {
                    JsonObject enchantObject = enchantElement.getAsJsonObject();

                    for (Map.Entry<String, JsonElement> entry : enchantObject.entrySet()) {
                        EnchantEntry enchant = new EnchantEntry();
                        enchant.setName(entry.getKey());
                        enchant.setLevel(entry.getValue().getAsInt());
                        enchants.add(enchant);
                    }
                }

                stack.setEnchants(enchants.toArray(new EnchantEntry[0]));
            }

            // ItemFlags
            if (stackObject.has("flags")) {
                JsonArray flagArray = stackObject.getAsJsonArray("flags");
                ItemFlag[] flags = IntStream.range(0, flagArray.size()).mapToObj(j -> ItemFlag.valueOf(flagArray.get(j).getAsString())).toArray(ItemFlag[]::new);
                stack.setFlags(flags);
            }
        }

        machineData.setStacks(stacks);

        // Additional data
        machineData.setStoppedTick(object.getAsJsonPrimitive("stoppedTick").getAsBoolean());

        JsonArray playerArray = object.getAsJsonArray("allowedPlayers");
        String[] allowedPlayers = IntStream.range(0, playerArray.size()).mapToObj(j -> playerArray.get(j).getAsString()).toArray(String[]::new);
        machineData.setAllowedPlayers(allowedPlayers);

        JsonArray customDataArray = object.getAsJsonArray("customData");
        CustomDataManager customDataManager = new CustomDataManager();

        for (JsonElement customElement : customDataArray) {
            JsonObject customObject = customElement.getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : customObject.entrySet()) {
                JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();

                CustomData custom = new CustomData();
                custom.setKey(entry.getKey());

                if (primitive.isBoolean()) {
                    custom.setValue(primitive.getAsBoolean());
                } else if (primitive.isNumber()) {
                    custom.setValue(primitive.getAsNumber());
                } else if (primitive.isString()) {
                    custom.setValue(primitive.getAsString());
                }

                customDataManager.put(custom);
            }
        }

        machineData.setCustomDataManager(customDataManager);

        return machineData;
    }

    @Override
    public JsonElement serialize(MachineData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        // ItemStacks in Inventory
        object.addProperty("invSize", src.getInvSize());
        object.addProperty("invTitle", src.getInvTitle());

        JsonArray stackArray = new JsonArray();

        for (StackData stack : src.getStacks()) {
            if (stack == null) {
                continue;
            }

            JsonObject stackObject = new JsonObject();

            stackObject.addProperty("slot", stack.getSlot());
            stackObject.addProperty("type", stack.getType());
            stackObject.addProperty("amount", stack.getAmount());
            stackObject.addProperty("data", stack.getData());

            if (stack.getDamage() > 0) {
                stackObject.addProperty("damage", stack.getDamage());
            }

            if (StringUtils.isNotBlank(stack.getDisplayName())) {
                stackObject.addProperty("displayName", stack.getDisplayName());
            }

            if (StringUtils.isNotBlank(stack.getLocalizedName())) {
                stackObject.addProperty("localizedName", stack.getLocalizedName());
            }

            // Lore
            if (stack.getLore() != null && stack.getLore().length > 0) {
                JsonArray loreArray = new JsonArray();

                for (String lore : stack.getLore()) {
                    loreArray.add(lore);
                }

                stackObject.add("lore", loreArray);
            }

            // Enchantments
            if (stack.getEnchants() != null && stack.getEnchants().length > 0) {
                JsonArray enchantArray = new JsonArray();
                JsonObject enchantObject = new JsonObject();

                for (EnchantEntry enchant : stack.getEnchants()) {
                    enchantObject.addProperty(enchant.getName(), enchant.getLevel());
                }

                enchantArray.add(enchantObject);
                stackObject.add("enchants", enchantArray);
            }

            // ItemFlags
            if (stack.getFlags() != null && stack.getFlags().length > 0) {
                JsonArray flagArray = new JsonArray();

                for (ItemFlag flag : stack.getFlags()) {
                    flagArray.add(flag.name());
                }

                stackObject.add("flags", flagArray);
            }

            stackArray.add(stackObject);
        }

        object.add("stacks", stackArray);

        // Additional data
        object.addProperty("stoppedTick", src.isStoppedTick());

        JsonArray playerArray = new JsonArray();

        for (String player : src.getAllowedPlayers()) {
            playerArray.add(player);
        }

        object.add("allowedPlayers", playerArray);

        JsonArray customDataArray = new JsonArray();
        JsonObject customObject = new JsonObject();

        for (CustomData customData : src.getCustomDataManager().getDataList()) {
            String key = customData.getKey();

            if (customData.isBooleanValue()) {
                customObject.addProperty(key, customData.getValueAsBoolean());
            } else if (customData.isNumberValue()) {
                customObject.addProperty(key, customData.getValueAsNumber());
            } else if (customData.isStringValue()) {
                customObject.addProperty(key, customData.getValueAsString());
            }
        }

        customDataArray.add(customObject);
        object.add("customData", customDataArray);

        return object;
    }

}
