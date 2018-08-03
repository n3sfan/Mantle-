/*
 * Copyright (c) 2018 to Le Thinh
 */

package io.github.lethinh.mantle.gson;

import io.github.lethinh.mantle.gson.direct.CustomDataManager;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import java.util.List;

class MachinePattern {

    final Block block;
    final Inventory inventory;
    final List<String> accessiblePlayers;
    final boolean tickStopped;
    final CustomDataManager customDataManager;

    MachinePattern(Block block, Inventory inventory, List<String> accessiblePlayers, boolean tickStopped, CustomDataManager customDataManager) {
        this.block = block;
        this.inventory = inventory;
        this.accessiblePlayers = accessiblePlayers;
        this.tickStopped = tickStopped;
        this.customDataManager = customDataManager;
    }

}
