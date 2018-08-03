package io.github.lethinh.mantle.multiblock;

import org.bukkit.Location;

import java.util.List;

/**
 * Created by Le Thinh
 */
public class MultiBlockStructure<T extends MultiBlockTracker> {

    private final MultiBlockController<T> controller;
    private final Location center;
    private final List<Location> parts;

    public MultiBlockStructure(MultiBlockController<T> controller, Location center, List<Location> parts) {
        this.controller = controller;
        this.center = center;
        this.parts = parts;
    }

    /* Getters */
    public MultiBlockController<T> getController() {
        return controller;
    }

    public Location getCenter() {
        return center;
    }

    public List<Location> getParts() {
        return parts;
    }

}
