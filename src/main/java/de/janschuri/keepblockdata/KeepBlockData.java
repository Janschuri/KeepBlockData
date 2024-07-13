package de.janschuri.keepblockdata;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class KeepBlockData extends JavaPlugin {

    private static KeepBlockData instance;
    public static final NamespacedKey BLOCK_ITEM_KEY = new NamespacedKey("keepblockdata", "item");

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static KeepBlockData getInstance() {
        return instance;
    }
}
