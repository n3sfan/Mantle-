package io.lethinh.github.mantle.loader;

import io.lethinh.github.mantle.Mantle;
import io.lethinh.github.mantle.command.CommandMantleGive;

/**
 * Created by Le Thinh
 */
public class CommandLoader implements ILoader {

	public CommandLoader() {
		LOADERS.add(this);
	}

	@Override
	public void load(Mantle plugin) throws Throwable {
		plugin.getCommand("mantlegive").setExecutor(new CommandMantleGive());
	}

}
