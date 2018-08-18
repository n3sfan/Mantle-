package io.github.lethinh.mantle.event;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.MantleItemStacks;
import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.block.GenericMachine;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Le Thinh
 */
public class MachineChangedEvent implements Listener {

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        ItemStack heldItem = event.getItemInHand();
        Block block = event.getBlockPlaced();

        for (GenericMachine machineType : GenericMachine.values()) {
            ItemStack blockStack = machineType.getStackForBlock();

            if (blockStack == null) {
                continue;
            }

            if (heldItem.isSimilar(blockStack) || block.getType() == blockStack.getType() && block.getData() == blockStack.getData().getData()) {
                BlockMachine machine = machineType.createBlockMachine(block, name);
                BlockMachine.MACHINES.add(machine);
                machine.onMachinePlaced(player, heldItem);
            }
        }
    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        for (BlockMachine machine : BlockMachine.MACHINES) {
            if (!machine.block.getLocation().equals(block.getLocation())) {
                continue;
            }

            if (!machine.canBreak(player)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot break this machine because you it is locked!");
                continue;
            }

            machine.setTickStopped(true);

            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setDropItems(false);

                for (ItemStack stack : MantleItemStacks.STACKS) {
                    if (stack.getItemMeta().getLocalizedName().replace(Mantle.PLUGIN_ID + "_", "")
                            .equalsIgnoreCase(machine.machineType.getName())) {
                        machine.dropItems(stack);
                    }
                }
            }

            machine.onMachineBroken(player);
            BlockMachine.MACHINES.remove(machine);
        }
    }

    private final ConcurrentMap<Location, CopyOnWriteArrayList<String>> interactPos = new ConcurrentHashMap<>();

    @EventHandler
    public void onBlockOpened(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

        if (player.isSneaking() || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Block block = event.getClickedBlock();
        Location location = block.getLocation();

        BlockMachine.MACHINES.stream().filter(machine -> block.getLocation().equals(machine.block.getLocation()))
                .forEach(machine -> {
                    if (machine.canOpen(player)) {
                        if (machine.inventory.getSize() > 0) {
                            event.setCancelled(true);
                            player.openInventory(machine.inventory);

                            CopyOnWriteArrayList<String> players = interactPos.get(location);

                            if (players == null || players.isEmpty()) {
                                players = new CopyOnWriteArrayList<>(new String[]{name});
                            } else {
                                players.addAll(new CopyOnWriteArrayList<>(new String[]{name}));
                            }

                            interactPos.put(location, players);
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You cannot open this machine because it is locked!");
                    }
                });
    }

    @EventHandler
    public void onInventoryClicked(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ClickType clickType = event.getClick();
        InventoryAction action = event.getAction();
        SlotType slotType = event.getSlotType();
        ItemStack clicked = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        int slot = event.getRawSlot();
        InventoryView view = event.getView();
        HumanEntity player = event.getWhoClicked();

        for (BlockMachine machine : BlockMachine.MACHINES) {
            if (!machine.inventory.getName().equals(inventory.getName())) {
                continue;
            }

            CopyOnWriteArrayList<String> players = interactPos.get(machine.block.getLocation());

            if (players == null || players.isEmpty()) {
                continue;
            }

            if (players.contains(player.getName())) {
                boolean cancel = machine.onInventoryInteract(clickType, action, slotType, clicked, cursor, slot, view, player);

                if (cancel) {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClosed(InventoryCloseEvent event) {
        String player = event.getPlayer().getName();

        for (Map.Entry<Location, CopyOnWriteArrayList<String>> entry : interactPos.entrySet()) {
            Location location = entry.getKey();
            CopyOnWriteArrayList<String> players = entry.getValue();

            if (players == null) {
                continue;
            }

            if (players.contains(player)) {
                players.remove(player);

                if (players.isEmpty()) {
                    interactPos.remove(location);
                } else {
                    interactPos.put(location, players);
                }
            }
        }
    }

}
