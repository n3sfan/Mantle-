/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.block.GenericMachine;
import io.github.lethinh.mantle.gson.direct.CustomDataManager;
import io.github.lethinh.mantle.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class IOMachines {

    // Utility class, no need to get the constructor
    private IOMachines() {

    }

    private static final Gson gson = new GsonBuilder().registerTypeAdapter(MachineData.class, new MachineDataTypeAdapter()).setPrettyPrinting().create();

    public static void saveMachines() throws IOException {
        if (BlockMachine.MACHINES.isEmpty()) {
            return;
        }

        File out = new File(Mantle.instance.getDataFolder(), "machines.yml");

        if (!out.exists()) {
            out.createNewFile();
        }

        FileConfiguration machines = new YamlConfiguration();
        BlockMachine.MACHINES.forEach(machine -> machines.set(Utils.serializeLocation(machine.block.getLocation()), machine.machineType.getName()));
        machines.save(out);
    }

    public static void loadMachines() {
        File in = new File(Mantle.instance.getDataFolder(), "machines.yml");

        if (!in.exists()) {
            return;
        }

        FileConfiguration machines = YamlConfiguration.loadConfiguration(in);

        for (String key : machines.getKeys(false)) {
            Location location = Utils.deserializeLocation(key);

            if (location == null) {
                continue;
            }

            String machineName = machines.getString(key);

            if (StringUtils.isBlank(machineName)) {
                continue;
            }

            Block block = location.getBlock();

            if (block.isEmpty() || block.isLiquid()) {
                continue;
            }

            for (GenericMachine machineType : GenericMachine.values()) {
                if (machineType.getName().equalsIgnoreCase(machineName)) {
                    BlockMachine.MACHINES.add(machineType.createBlockMachine(block));
                }
            }
        }
    }

    public static void saveMachinesData() throws IOException {
        if (BlockMachine.MACHINES.isEmpty()) {
            return;
        }

        File dir = new File(Mantle.instance.getDataFolder(), "MachinesData");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            file.delete();
        }

        for (BlockMachine machine : BlockMachine.MACHINES) {
            //  MachineData data = toData(machine);
            File out = new File(dir, Utils.serializeLocation(machine.block.getLocation()) + ".yml");
            FileConfiguration machineData = new YamlConfiguration();
            Inventory inventory = machine.inventory;


            machineData.set("invSize", inventory.getSize());
            machineData.set("invTitle", inventory.getTitle());

            ConfigurationSection stacksSection = machineData.createSection("stacks");

            for (int i = 0; i < inventory.getSize(); ++i) {
                ItemStack stack = inventory.getItem(i);

                if (stack == null) {
                    continue;
                }

                stacksSection.set("slot-" + i, stack);
            }

            machineData.set("tickStopped", machine.isTickStopped());
            machineData.set("accessiblePlayers", machine.accessiblePlayers);

            ConfigurationSection customDataSection = machineData.createSection("customData");
            machine.writeCustomData().forEach(customDataSection::set);

            machineData.save(out);
        }
    }

    public static void loadMachinesData() {
        if (BlockMachine.MACHINES.isEmpty()) {
            return;
        }

        File dir = new File(Mantle.instance.getDataFolder(), "MachinesData");

        if (!dir.canRead()) {
            return;
        }

        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            // Doesn't have permission to read file
            if (!file.canRead()) {
                continue;
            }

            Location location = Utils.deserializeLocation(file.getName());

            if (location == null) {
                continue;
            }

            for (int i = 0; i < BlockMachine.MACHINES.size(); ++i) {
                BlockMachine machine = BlockMachine.MACHINES.get(i);

                if (!machine.block.getLocation().equals(location)) {
                    continue;
                }

                FileConfiguration machineData = YamlConfiguration.loadConfiguration(file);

                // Inventory
                int invSize = machineData.getInt("invSize");
                String invTitle = machineData.getString("invTitle");
                Inventory inventory = Bukkit.createInventory(null, invSize, invTitle);

                ConfigurationSection stacksSection = machineData.getConfigurationSection("stacks");

                try {
                    for (String key : stacksSection.getKeys(false)) {
                        int slot = Integer.parseInt(key.substring("slot-".length()));
                        ItemStack stack = stacksSection.getItemStack(key);

                        if (stack == null) {
                            continue;
                        }

                        inventory.setItem(slot, stack);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                // Additional data
                boolean tickStopped = machineData.getBoolean("tickStopped");
                List<String> accessiblePlayers = machineData.getStringList("accessiblePlayers");

                CustomDataManager manager = new CustomDataManager();
                ConfigurationSection customDataSection = machineData.getConfigurationSection("customData");

                for (String key : customDataSection.getKeys(false)) {
                    Object value = customDataSection.get(key);

                    if (value == null) {
                        continue;
                    }

                    manager.put(key, value);
                }

                machine.inventory = inventory;
                machine.setTickStopped(tickStopped);
                machine.accessiblePlayers = accessiblePlayers;
                machine.readCustomData(manager);
                BlockMachine.MACHINES.set(i, machine);
            }
        }
    }

//    public static void saveMachinesData() throws IOException {
//        if (BlockMachine.MACHINES.isEmpty()) {
//            return;
//        }
//
//        File dir = new File(Mantle.instance.getDataFolder(), "MachinesData");
//
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        for (File file : Objects.requireNonNull(dir.listFiles())) {
//            file.delete();
//        }
//
//        for (BlockMachine machine : BlockMachine.MACHINES) {
//            MachineData data = toData(machine);
//            File out = new File(dir, Utils.serializeLocation(machine.block.getLocation()) + ".json");
//
//            try (FileWriter writer = new FileWriter(out)) {
//                gson.toJson(data, writer);
//            }
//        }
//    }
//
//    public static void loadMachinesData() throws FileNotFoundException {
//        if (BlockMachine.MACHINES.isEmpty()) {
//            return;
//        }
//
//        File dir = new File(Mantle.instance.getDataFolder(), "MachinesData");
//
//        if (!dir.canRead()) {
//            return;
//        }
//
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        for (File file : Objects.requireNonNull(dir.listFiles())) {
//            // Doesn't have permission to read file
//            if (!file.canRead()) {
//                continue;
//            }
//
//            Location location = Utils.deserializeLocation(file.getName());
//
//            if (location == null) {
//                continue;
//            }
//
//            JsonReader reader = new JsonReader(new FileReader(file));
//            MachineData data = gson.fromJson(reader, MachineData.class);
//            MachinePattern pattern = fromData(data, location);
//
//            if (pattern == null) {
//                continue;
//            }
//
//            for (int i = 0; i < BlockMachine.MACHINES.size(); ++i) {
//                BlockMachine to = BlockMachine.MACHINES.get(i);
//
//                if (!pattern.block.getLocation().equals(to.block.getLocation())) {
//                    continue;
//                }
//
//                to.inventory = pattern.inventory;
//                to.accessiblePlayers = pattern.accessiblePlayers;
//                to.setTickStopped(pattern.tickStopped);
//                to.readCustomData(pattern.customDataManager);
//                BlockMachine.MACHINES.set(i, to);
//            }
//        }
//    }

    /**
     * Parse {@link MachinePattern} (only machine data) from {@link MachineData}
     *
     * @param data     The machine data
     * @param location The machine location
     * @return the machine data parsed in {@link MachinePattern}
     */
    private static MachinePattern fromData(MachineData data, Location location) {
        Block block = location.getBlock();

        if (block.isEmpty() || block.isLiquid()) {
            return null;
        }

        // Inventory
        Inventory inventory = Bukkit.createInventory(null, data.getInvSize(), data.getInvTitle());

        for (StackData stackData : data.getStacks()) {
            int slot = stackData.getSlot();

            // Invalid slot
            if (slot < 0 || slot > inventory.getSize()) {
                continue;
            }

            // ItemStack
            Material material = Material.getMaterial(stackData.getType());
            int amount = stackData.getAmount();
            short damage = stackData.getDamage();

            ItemStack stack = new ItemStack(material, amount, damage);

            stack.setData(new MaterialData(material, stackData.getData()));

            // ItemMeta
            ItemMeta meta = stack.getItemMeta();

            String displayName = stackData.getDisplayName();
            String localizedName = stackData.getLocalizedName();
            String[] lore = stackData.getLore();
            EnchantEntry[] enchantEntries = stackData.getEnchants();
            ItemFlag[] flags = stackData.getFlags();

            if (StringUtils.isNotBlank(displayName)) {
                meta.setDisplayName(displayName);
            }

            if (StringUtils.isNotBlank(localizedName)) {
                meta.setLocalizedName(localizedName);
            }

            if (lore != null && lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }

            if (enchantEntries != null && enchantEntries.length > 0) {
                for (EnchantEntry enchantEntry : enchantEntries) {
                    Enchantment ench = Enchantment.getByName(enchantEntry.getName());

                    if (ench == null) {
                        continue;
                    }

                    meta.addEnchant(ench, enchantEntry.getLevel(), true);
                }
            }

            if (flags != null && flags.length > 0) {
                meta.addItemFlags(flags);
            }

            stack.setItemMeta(meta);

            // Set item to the inventory
            inventory.setItem(slot, stack);
        }

        // Additional data
        boolean tickStopped = data.isStoppedTick();
        List<String> accessiblePlayers = new ArrayList<>(Arrays.asList(data.getAllowedPlayers()));
        CustomDataManager customDataManager = data.getCustomDataManager();

        return new MachinePattern(block, inventory, accessiblePlayers, tickStopped, customDataManager);
    }

    /**
     * Save machine to {@link MachineData}
     *
     * @param machine The machine
     * @return the parsed machine data
     */
    private static MachineData toData(BlockMachine machine) {
        MachineData data = new MachineData();
        Inventory inventory = machine.inventory;

        if (inventory.getSize() > 0 && machine.getRealSlots() > 0) {
            data.setInvSize(inventory.getSize());
            data.setInvTitle(inventory.getTitle());

            // ItemStacks
            List<StackData> stacks = new ArrayList<>();

            for (int i = 0; i < inventory.getSize(); ++i) {
                ItemStack stack = inventory.getItem(i);

                if (stack == null) {
                    continue;
                }

                StackData stackData = new StackData();

                stackData.setSlot(i);
                stackData.setType(stack.getType().name());
                stackData.setAmount(stack.getAmount());
                stackData.setData(stack.getData().getData());
                stackData.setDamage(stack.getDurability());

                // ItemMeta
                ItemMeta meta = stack.getItemMeta();

                if (meta.hasDisplayName()) {
                    stackData.setDisplayName(meta.getDisplayName());
                }

                if (meta.hasLocalizedName()) {
                    stackData.setLocalizedName(meta.getLocalizedName());
                }

                if (meta.hasLore()) {
                    stackData.setLore(meta.getLore().toArray(new String[0]));
                }

                if (meta.hasEnchants()) {
                    EnchantEntry[] enchants = meta.getEnchants().entrySet().stream().map(entry -> new EnchantEntry().setName(entry.getKey().getName()).setLevel(entry.getValue())).toArray(EnchantEntry[]::new);
                    stackData.setEnchants(enchants);
                }

                stackData.setFlags(meta.getItemFlags().toArray(new ItemFlag[0]));
                stacks.add(stackData);
            }

            data.setStacks(stacks.toArray(new StackData[0]));
        }

        // Additional data
        data.setStoppedTick(machine.isTickStopped());
        data.setAllowedPlayers(machine.accessiblePlayers.toArray(new String[0]));
        data.setCustomDataManager(machine.writeCustomData());

        return data;
    }

}
