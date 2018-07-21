package io.lethinh.github.mantle.block;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

import io.lethinh.github.mantle.Mantle;

/**
 * Created by Le Thinh
 */
public abstract class BlockMachine {

	/* Static Member */
	public static final CopyOnWriteArrayList<BlockMachine> MACHINES = new CopyOnWriteArrayList<>();
	public static final Properties PROPERTIES = new Properties();
	protected static final long DEFAULT_DELAY = 20L;
	protected static final long DEFAULT_PERIOD = 10L;

	/* Instance Members */
	public final Block block;
	public final Inventory inventory;
	public BukkitRunnable subThread;

	/* Default constructor */
	public BlockMachine(Block block, int invSlots, String invName) {
		this.block = block;
		this.inventory = Bukkit.createInventory(null, invSlots, invName);
	}

	/* Tick */
	public abstract void handleUpdate(Mantle plugin);

	/* Object */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BlockMachine) {
			BlockMachine machine = (BlockMachine) obj;
			return block.getLocation().equals(machine.block.getLocation());
		} else if (obj instanceof Block) {
			return block.getLocation().equals(((Block) obj).getLocation());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return block.getLocation().hashCode();
	}

	/* I/O */
	public static void addMachineData(BlockMachine machine) {
		Location location = machine.block.getLocation();
		PROPERTIES.setProperty(location.getWorld().getName() + "," + location.getX() + ","
				+ location.getY() + "," + location.getZ(), machine.inventory.getName().replace(" ", "_"));
	}

	public static void removeMachineData(BlockMachine machine) {
		Location location = machine.block.getLocation();
		String locString = location.getWorld().getName() + "," + location.getX() + ","
				+ location.getY() + "," + location.getZ();
		PROPERTIES.remove(locString, machine.inventory.getName().replace(" ", "_"));
	}

	public static void saveMachinesData() throws IOException {
		File dir = new File(System.getProperty("user.dir"), "Mantle");

		if (!dir.exists()) {
			dir.mkdirs();
		}

		OutputStream out = new FileOutputStream(new File(dir, "machines_data.txt"));
		PROPERTIES.store(out, null);
	}

	public static void loadMachinesData() throws IOException {
		File dir = new File(System.getProperty("user.dir"), "Mantle");
		InputStream in = new FileInputStream(new File(dir, "machines_data.txt"));

		PROPERTIES.load(in);

		if (PROPERTIES.isEmpty()) {
			return;
		}

		for (Entry<Object, Object> entry : PROPERTIES.entrySet()) {
			String loc = (String) entry.getKey();
			String type = (String) entry.getValue();

			String[] split = loc.split(",");
			Location location = new Location(Bukkit.getWorld(split[0]), NumberConversions.toDouble(split[1]),
					NumberConversions.toDouble(split[2]), NumberConversions.toDouble(split[3]));
			Block block = location.getBlock();

			switch (type.toLowerCase()) {
			case "tree_cutter":
				BlockMachine.MACHINES.add(new BlockTreeCutter(block));
				break;
			case "planter":
				BlockMachine.MACHINES.add(new BlockPlanter(block));
				break;
			case "block_breaker":
				BlockMachine.MACHINES.add(new BlockBlockBreaker(block));
				break;
			case "block_placer":
				BlockMachine.MACHINES.add(new BlockBlockPlacer(block));
				break;
			}
		}
	}

}
