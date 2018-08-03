package io.github.lethinh.mantle.loader;

import java.util.concurrent.CopyOnWriteArrayList;

import io.github.lethinh.mantle.Mantle;

/**
 * Created by Le Thinh
 */
public interface ILoader {

	CopyOnWriteArrayList<ILoader> LOADERS = new CopyOnWriteArrayList<>();

	void load(Mantle plugin) throws Exception;

	default void preLoad() {
		LOADERS.add(this);
	}

}
