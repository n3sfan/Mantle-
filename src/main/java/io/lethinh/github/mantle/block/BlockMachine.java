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
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.nbt.NBTHelper;
import io.lethinh.github.mantle.nbt.NBTTagCompound;
import io.lethinh.github.mantle.utils.Utils;

/**
 * Created by Le Thinh
 */
public abstract class BlockMachine {

	/* Static Members */
	public static final CopyOnWriteArrayList<BlockMachine> MACHINES = new CopyOnWriteArrayList<>();
	public static final Properties PROPERTIES = new Properties();
	protected static final long DEFAULT_DELAY = 20L;
	protected static final long DEFAULT_PERIOD = 10L;

	/* Instance Members */
	public final Block block;
	public Inventory inventory;
	public BukkitRunnable subThread;

	/* Default constructor */
	public BlockMachine(Block block, int invSlots, String invName) {
		this(block, Bukkit.createInventory(null, invSlots, invName));
	}

	public BlockMachine(Block block, Inventory inventory) {
		this.block = block;
		this.inventory = inventory;
	}

	/* Tick */
	public boolean stoppedTick = false;

	public void tick(Mantle plugin) {
		if (!stoppedTick) {
			handleUpdate(plugin);
		}
	}

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

	/* NBT */
	public NBTTagCompound writeToNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("Inventory", Utils.serializeInventory(inventory));
		return nbt;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		inventory = Utils.deserializeInventory(nbt.getCompoundTag("Inventory"));
	}

	/* I/O */
	public static void saveMachinesInventoriesData() throws IOException {
		if (MACHINES.isEmpty()) {
			return;
		}

		File dir = new File(Mantle.instance.getDataFolder(), "MachinesInventories");

		if (!dir.exists()) {
			dir.mkdirs();
		}

		for (File file : dir.listFiles()) {
			file.delete();
		}

		for (BlockMachine save : MACHINES) {
			File out = new File(dir, Utils.serializeLocation(save.block.getLocation()));
			NBTHelper.safeWrite(save.writeToNBT(), out);
		}
	}

	public static void loadMachinesInventoriesData() throws IOException {
		File dir = new File(Mantle.instance.getDataFolder(), "MachinesInventories");

		if (!dir.exists()) {
			dir.mkdirs();
			return;
		}

		File[] files = dir.listFiles();

		if (files.length == 0) {
			return;
		}

		for (File file : files) {
			Location location = Utils.deserializeLocation(file.getName());

			for (BlockMachine machine : MACHINES) {
				if (!machine.block.getLocation().equals(location)) {
					continue;
				}

				machine.readFromNBT(NBTHelper.read(file));
			}
		}
	}

	public static void saveMachinesData() throws IOException {
		File dir = Mantle.instance.getDataFolder();

		if (!dir.exists()) {
			dir.mkdirs();
		}

		for (BlockMachine machine : MACHINES) {
			Location location = machine.block.getLocation();
			PROPERTIES.setProperty(Utils.serializeLocation(location), machine.inventory.getName().replace(" ", "_"));
		}

		OutputStream out = new FileOutputStream(new File(dir, "machines_data.txt"));
		PROPERTIES.store(out, null);
	}

	public static void loadMachinesData() throws IOException {
		File file = new File(Mantle.instance.getDataFolder(), "machines_data.txt");

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		if (!file.exists()) {
			file.createNewFile();
			return;
		}

		InputStream in = new FileInputStream(file);

		PROPERTIES.load(in);

		if (PROPERTIES.isEmpty()) {
			return;
		}

		for (Entry<Object, Object> entry : PROPERTIES.entrySet()) {
			String loc = (String) entry.getKey();
			String type = (String) entry.getValue();

			Block block = Utils.deserializeLocation(loc).getBlock();
			BlockMachine machine = null;

			switch (type.toLowerCase()) {
			case "tree_cutter":
				BlockMachine.MACHINES.add(machine = new BlockTreeCutter(block));
				break;
			case "planter":
				BlockMachine.MACHINES.add(machine = new BlockPlanter(block));
				break;
			case "block_breaker":
				BlockMachine.MACHINES.add(machine = new BlockBlockBreaker(block));
				break;
			case "block_placer":
				BlockMachine.MACHINES.add(machine = new BlockBlockPlacer(block));
				break;
			}

			if (machine != null && machine instanceof Listener) {
				Bukkit.getServer().getPluginManager().registerEvents((Listener) machine, Mantle.instance);
			}
		}
	}

}
