package com.gmail.litalways.BreedSpawnControl.Listeners;

import com.gmail.litalways.BreedSpawnControl.BreedSpawnControl;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.List;

public class BreedListener implements Listener {

    private BreedSpawnControl plugin;

    public BreedListener(BreedSpawnControl instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onBreedAnimal(EntityBreedEvent event) {
        boolean debug = plugin.getConfig().getBoolean("Debug");
        if (debug) {
            String item = event.getBredWith() != null ? event.getBredWith().getType().toString() : "NULL";
            String player = event.getBreeder() != null ? event.getBreeder().getName() : "NULL";
            String father = event.getFather() != null ? event.getFather().getType().name() + "(" + event.getFather().getUniqueId().toString() + ")" : "NULL";
            String mother = event.getMother() != null ? event.getMother().getType().name() + "(" + event.getMother().getUniqueId().toString() + ")" : "NULL";
            String child = event.getEntity() != null ? event.getEntity().getType().name() + "(" + event.getEntity().getUniqueId().toString() + ")" : "NULL";
            int exp = event.getExperience();
            plugin.getLogger().warning("============================================================");
            plugin.getLogger().warning(String.format("Item Name: %s", item));
            plugin.getLogger().warning(String.format("Player Name: %s", player));
            plugin.getLogger().warning(String.format("Father UUID: %s", father));
            plugin.getLogger().warning(String.format("Mother UUID: %s", mother));
            plugin.getLogger().warning(String.format("Child UUID: %s", child));
            plugin.getLogger().warning(String.format("Exp Amount: %s", exp));
        }
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("BreedAnimals");
        for (String key : section.getKeys(false)) {
            if (debug) {
                plugin.getLogger().warning("[ " + key + " ] Check start");
            }
            if (key.equalsIgnoreCase(event.getEntity().getType().name())) {
                if (plugin.getConfig().getBoolean("BreedAnimals." + key + ".Cancel")) {
                    event.setCancelled(true);
                    if (debug) {
                        plugin.getLogger().warning("[ " + key + " ] event canceled");
                    }
                    break;
                }
                int experience = plugin.getConfig().getInt("BreedAnimals." + key + ".Experience");
                if (experience >= 0) {
                    event.setExperience(experience);
                    if (debug) {
                        plugin.getLogger().warning("[ " + key + " ] experience is set to " + experience);
                    }
                }
                List<String> commands = plugin.getConfig().getStringList("BreedAnimals." + key + ".RunCustom.Commands");
                for (String cmd : commands) {
                    if (cmd.startsWith("bypass:")) {
                        if (event.getBreeder() instanceof Player) {
                            List<String> permissions = plugin.getConfig().getStringList("BreedAnimals." + key + ".RunCustom.TempPermissions");
                            Player player = (Player) event.getBreeder();
                            try {
                                String cmdStr = cmd.substring(7);
                                if (debug) {
                                    plugin.getLogger().warning("[ " + key + " ] run bypass command: " + cmdStr);
                                }
                                for (String perm : permissions) {
                                    plugin.permission.playerAdd(player, perm);
                                }
                                player.performCommand(cmdStr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                for (String perm : permissions) {
                                    plugin.permission.playerRemove(player, perm);
                                }
                            }
                        }
                    } else if (cmd.startsWith("console:")) {
                        String cmdStr = cmd.substring(8);
                        if (debug) {
                            plugin.getLogger().warning("[ " + key + " ] run console command: " + cmdStr);
                        }
                        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmdStr);
                    } else {
                        if (debug) {
                            plugin.getLogger().warning("[ " + key + " ] run command: " + cmd);
                        }
                        if (event.getBreeder() instanceof Player) {
                            Player player = (Player) event.getBreeder();
                            player.performCommand(cmd);
                        }
                    }
                }
                if (plugin.getConfig().getBoolean("BreedAnimals." + key + ".RunCustom.RemoveChild")) {
                    event.getEntity().remove();
                    if (debug) {
                        plugin.getLogger().warning("[ " + key + " ] child removed.");
                    }
                }
            }
        }
        if (debug) {
            plugin.getLogger().warning("============================================================");
        }
    }
}
