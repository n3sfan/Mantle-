package io.github.lethinh.mantle.multiblock;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.block.GenericMachine;
import io.github.lethinh.mantle.gson.direct.CustomDataManager;
import io.github.lethinh.mantle.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;

import java.util.Objects;

/**
 * Created by Le Thinh
 */
public abstract class MultiBlockController<T extends MultiBlockTracker> extends BlockMachine {

    public T tracker;
    public MultiBlockStructure<T> structure;
    public boolean firstTime = true;
    public BlockFace direction = BlockFace.SELF;

    public MultiBlockController(GenericMachine machineType, Block block, int invSlots, String invName, String... players) {
        super(machineType, block, invSlots, invName, players);
    }

    public MultiBlockController(GenericMachine machineType, Block block, Inventory inventory, String... players) {
        super(machineType, block, inventory, players);
    }

    @Override
    public void handleUpdate(Mantle plugin) {
        runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
    }

    @Override
    public void work() {
        checkMultiblock();

        if (structure == null) {
            return;
        }

        if (firstTime) {
            accessiblePlayers.stream().map(Bukkit::getPlayerExact).filter(Objects::nonNull)
                    .forEach(p -> p.sendMessage(ChatColor.GREEN + "Multiblock at " + block.getLocation().toString()
                            + " was successfully formed!"));
            firstTime = false;
        }

        mimicWork();
    }

    public abstract void mimicWork();

    /* Callbacks */
    @Override
    public void onMachinePlaced(Player player, ItemStack heldItem) {
        direction = Utils.getBlockFaceFromPlayer(block.getLocation(), player);
        System.out.println("Placed dir: " + direction.name() + ", maybe: " + (block.getState().getData() instanceof Directional ? ((Directional) block.getState().getData()).getFacing().name() : BlockFace.NORTH.name()));
        super.onMachinePlaced(player, heldItem);
    }

    @Override
    public void onMachineBroken(Player player) {
        if (structure == null) {
            return;
        }

        for (BlockMachine machine : BlockMachine.MACHINES) {
            if (!(machine instanceof MultiBlockPart)) {
                continue;
            }

            for (Location part : structure.getParts()) {
                if (part.getBlock().isEmpty() || part.getBlock().isLiquid()
                        || !machine.block.getLocation().equals(part)) {
                    continue;
                }

                MultiBlockPart multiBlockPart = (MultiBlockPart) machine;
                multiBlockPart.hasController = false;
                multiBlockPart.structure = structure;
            }
        }
    }

    /* Multi-block */
    @SuppressWarnings("unchecked")
    public void checkMultiblock() {
        Location inside = Utils.offsetLocation(block.getLocation(), direction.getOppositeFace());
        Location center = tracker.getCenter(inside);
        this.structure = tracker.detectMultiblock(center);
    }

    @Override
    public CustomDataManager writeCustomData() {
        CustomDataManager manager = new CustomDataManager();
        manager.put("FirstTime", firstTime);
        manager.put("Direction", direction.name());
        return manager;
    }

    @Override
    public void readCustomData(CustomDataManager manager) {
        firstTime = manager.getAsBoolean("FirstTime");
        direction = BlockFace.valueOf(manager.getAsString("Direction"));
    }

}
