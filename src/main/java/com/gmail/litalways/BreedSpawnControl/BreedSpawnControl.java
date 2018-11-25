package com.gmail.litalways.BreedSpawnControl;

import com.gmail.litalways.BreedSpawnControl.Commands.Reload;
import com.gmail.litalways.BreedSpawnControl.Listeners.BreedListener;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BreedSpawnControl extends JavaPlugin {

    public Permission permission = null;

    @Override
    public void onEnable() {
        getPluginLoader().enablePlugin(this);
        getServer().getPluginManager().registerEvents(new BreedListener(this), this);
        getCommand("bsc").setExecutor(new Reload(this));
        saveDefaultConfig();
        if (!setupPermissions()) {
            getLogger().warning("Vault is hook failed! Plugin will disable.");
            onDisable();
        }
        getLogger().info("BreedSpawnControl is enabled!");
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    @Override
    public void onDisable() {
        getPluginLoader().disablePlugin(this);
        getLogger().info("BreedSpawnControl is disabled!");
    }
}
