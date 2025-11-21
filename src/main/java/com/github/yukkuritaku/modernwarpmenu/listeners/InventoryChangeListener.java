package com.github.yukkuritaku.modernwarpmenu.listeners;

import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;

import java.util.function.Consumer;

public record InventoryChangeListener(Consumer<Container> callback) implements ContainerListener {

    @Override
    public void containerChanged(Container container) {
        this.callback.accept(container);
    }
}
