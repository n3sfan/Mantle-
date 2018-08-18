package io.github.lethinh.mantle.block.impl;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachineEnergized;
import io.github.lethinh.mantle.block.GenericMachine;
import io.github.lethinh.mantle.energy.EnergyCapacitor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Le Thinh
 */
public class BlockTeleportTransmitter extends BlockMachineEnergized {

    public BlockTeleportTransmitter(Block block, String... players) {
        super(GenericMachine.TELEPORT_TRANSMITTER, block, 9, "Teleport Transmitter", players);

        for (int i = 1; i < inventory.getSize(); ++i) {
            inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
        }

        setEnergyCapacitor(new EnergyCapacitor(DEFAULT_ENERGY_CAPACITY, 500, 0));
    }

    @Override
    public void handleUpdate(Mantle plugin) {
        runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY + 10L, DEFAULT_PERIOD);
    }

    @Override
    public void work() {
        ItemStack stack = inventory.getItem(0);

        if (stack == null || stack.getAmount() == 0 || BlockTeleportReceiver.WARPS.isEmpty()) {
            return;
        }

        Queue<Player> queue = new ArrayBlockingQueue<>(4); // Max is 4 players per teleport and who stand on the
        // transmitter first will be teleported first
        Location dst = null;

        for (Entry<ItemStack, Location> entry : BlockTeleportReceiver.WARPS.entrySet()) {
            ItemStack toStack = entry.getKey();

            if (toStack == null) {
                continue;
            }

            Location toLocation = entry.getValue();

            if (!toStack.equals(stack)) {
                continue;
            }

            dst = toLocation;
            block.getWorld().getNearbyEntities(block.getLocation(), 1D, 1D, 1D).stream()
                    .filter(e -> !e.isDead() && e instanceof Player)
                    .forEach(e -> queue.offer((Player) e));
        }

        while (dst != null && !queue.isEmpty()) {
            Player player = queue.poll();
            player.teleport(dst, TeleportCause.PLUGIN);
            player.setFallDistance(0F);
        }
    }

    /* Callbacks */
    @Override
    public boolean onInventoryInteract(ClickType clickType, InventoryAction action, SlotType slotType, ItemStack clicked, ItemStack cursor, int slot, InventoryView view, HumanEntity player) {
        return slot >= 1 && slot < 9;
    }

    @Override
    public int getRealSlots() {
        return 1;
    }

}
