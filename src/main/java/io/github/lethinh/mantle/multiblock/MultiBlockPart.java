package io.github.lethinh.mantle.multiblock;

import io.github.lethinh.mantle.Mantle;
import io.github.lethinh.mantle.block.BlockMachine;
import io.github.lethinh.mantle.block.GenericMachine;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by Le Thinh
 */
public class MultiBlockPart<T extends MultiBlockTracker> extends BlockMachine implements Listener {

    public MultiBlockStructure structure;
    public boolean hasController = false;

    public MultiBlockPart(GenericMachine machineType, Block block) {
        super(machineType, block, 0, "");
    }

    @Override
    public void handleUpdate(Mantle plugin) {
        runnable.runTaskTimerAsynchronously(plugin, DEFAULT_DELAY, DEFAULT_PERIOD);
    }

    @Override
    public void work() {
        if (!hasController) {
            return;
        }

        MultiBlockController controller = structure.getController();
        accessiblePlayers = controller.accessiblePlayers;
        inventory = controller.inventory;
    }

    @Override
    public boolean canOpen(Player player) {
        return !hasController || structure == null || structure.getController().canOpen(player);
    }

    @Override
    public boolean canBreak(Player player) {
        return !hasController || structure == null || structure.getController().canBreak(player);
    }

}
