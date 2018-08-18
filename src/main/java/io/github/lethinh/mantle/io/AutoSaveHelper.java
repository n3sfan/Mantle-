package io.github.lethinh.mantle.io;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class AutoSaveHelper extends BukkitRunnable {
    private static int taskId = -1;

    public AutoSaveHelper(Plugin plugin, boolean enable, long duration){
        if(taskId != -1){
            Bukkit.getServer().getScheduler().cancelTask(taskId);
        }
        if(enable) {
            this.runTaskTimerAsynchronously(plugin, 0, duration);
            taskId = this.getTaskId();
        } else {
            taskId = -1;
        }
    }

    @Override
    public void run() {
        try {
            IOMachines.saveMachines();
            IOMachines.saveMachinesData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
