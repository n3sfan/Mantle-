/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.io;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.block.GenericMachine;
import io.github.lethinh.mantle.io.direct.CustomDataManager;
import io.github.lethinh.mantle.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class IOMachines {

    // Utility class, no need to get the constructor
    private IOMachines() {

    }

    public static void saveMachines() throws IOException {
        if (BlockMachine.MACHINES.isEmpty()) {
            return;
        }

        File out = new File(Mantle.instance.getDataFolder(), "machines.yml");

        if (!out.exists()) {
            if (!out.createNewFile()) {
                throw new IOException("Couldn't create file machines.yml!");
            }
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
            if (!dir.mkdirs()) {
                throw new IOException("Couldn't create directory MachinesData!");
            }
        }

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (!file.delete()) {
                throw new IOException("Couldn't delete file " + file.getName());
            }
        }

        for (BlockMachine machine : BlockMachine.MACHINES) {
            File out = new File(dir, Utils.serializeLocation(machine.block.getLocation()) + ".yml");
            FileConfiguration machineData = new YamlConfiguration();
            Inventory inventory = machine.inventory;

            if (inventory != null) {
                // Inventory
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
            }

            // Additional data
            machineData.set("tickStopped", machine.isTickStopped());
            machineData.set("accessiblePlayers", machine.accessiblePlayers);

            ConfigurationSection customDataSection = machineData.createSection("customData");
            machine.writeCustomData().forEach(customDataSection::set);

            machineData.save(out);
        }
    }

    public static void loadMachinesData() throws IOException {
        if (BlockMachine.MACHINES.isEmpty()) {
            return;
        }

        File dir = new File(Mantle.instance.getDataFolder(), "MachinesData");

        if (!dir.canRead()) {
            return;
        }

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Couldn't create directory MachinesData");
            }
        }

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            // Doesn't have permission to read file
            if (!file.canRead()) {
                continue;
            }

            Location location = Utils.deserializeLocation(Utils.getFileNameNoExtension(file));

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

}
