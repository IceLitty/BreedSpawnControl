package com.gmail.litalways.BreedSpawnControl.Listeners;

import com.gmail.litalways.BreedSpawnControl.BreedSpawnControl;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BreedListener implements Listener {

    private BreedSpawnControl plugin;

    public BreedListener(BreedSpawnControl instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onBreedAnimal(EntityBreedEvent event) {
        boolean debug = plugin.getConfig().getBoolean("Debug");
        if (debug) {
            plugin.getLogger().warning("============================================================");
        }
        if (plugin.getConfig().getStringList("EnableWorlds").contains(event.getEntity().getWorld().getName().toLowerCase())) {
            boolean worldSpecialThemSelf = plugin.getConfig().getBoolean("WorldSpecialThemSelf");
            boolean permFlag;
            if (event.getBreeder() instanceof Player) {
                Player player = (Player) event.getBreeder();
                if (player.hasPermission("bsc.*")) {
                    permFlag = true;
                    if (debug) {
                        plugin.getLogger().warning("Player have bsc.* permission node, continue.");
                    }
                } else {
                    if (player.hasPermission("bsc." + player.getWorld().getName().toLowerCase())) {
                        permFlag = true;
                        if (debug) {
                            plugin.getLogger().warning("Player have bsc." + player.getWorld().getName().toLowerCase() + " permission node, continue.");
                        }
                    } else {
                        permFlag = false;
                        if (debug) {
                            plugin.getLogger().warning("Player don't have bsc." + player.getWorld().getName().toLowerCase() + " permission node, reject.");
                        }
                    }
                }
            } else {
                permFlag = true;
                if (debug) {
                    plugin.getLogger().warning("Event not triggered by player, can't check permissions, pass.");
                }
            }
            if (permFlag) {
                if (debug) {
                    String item = event.getBredWith() != null ? event.getBredWith().getType().toString() : "NULL";
                    String player = event.getBreeder() != null ? event.getBreeder().getName() : "NULL";
                    String father = event.getFather() != null ? event.getFather().getType().name() + "(" + event.getFather().getUniqueId().toString() + ")" : "NULL";
                    String mother = event.getMother() != null ? event.getMother().getType().name() + "(" + event.getMother().getUniqueId().toString() + ")" : "NULL";
                    String child = event.getEntity() != null ? event.getEntity().getType().name() + "(" + event.getEntity().getUniqueId().toString() + ")" : "NULL";
                    int exp = event.getExperience();
                    plugin.getLogger().warning(String.format("Item Name: %s", item));
                    plugin.getLogger().warning(String.format("Player Name: %s", player));
                    plugin.getLogger().warning(String.format("Father UUID: %s", father));
                    plugin.getLogger().warning(String.format("Mother UUID: %s", mother));
                    plugin.getLogger().warning(String.format("Child UUID: %s", child));
                    plugin.getLogger().warning(String.format("Exp Amount: %s", exp));
                }
                ConfigurationSection section = plugin.getConfig().getConfigurationSection("BreedAnimals");
                for (String world : section.getKeys(false)) {
                    if (world.equalsIgnoreCase("*") || world.equalsIgnoreCase(event.getEntity().getWorld().getName())) {
                        ConfigurationSection section2 = plugin.getConfig().getConfigurationSection("BreedAnimals." + world);
                        for (String a_key : section2.getKeys(false)) {
                            String key = a_key.toUpperCase();
                            if (key.equalsIgnoreCase(event.getEntity().getType().name())) {
                                boolean start = true;
                                if (world.equalsIgnoreCase("*") &&
                                        plugin.getConfig().getBoolean("BreedAnimals." + event.getEntity().getWorld().getName().toLowerCase() + "." + key + ".Use", false)) {
                                    start = false;
                                }
                                if (start) {
                                    if (debug) {
                                        plugin.getLogger().warning("[ " + world + "." + key + " ] Check start");
                                    }
                                    boolean permFlag2;
                                    if (event.getBreeder() instanceof Player) {
                                        Player player = (Player) event.getBreeder();
                                        if (player.hasPermission("bsc.*.*")) {
                                            permFlag2 = true;
                                            if (debug) {
                                                plugin.getLogger().warning("Player have bsc.*.* permission node, continue.");
                                            }
                                        } else if (player.hasPermission("bsc." + player.getWorld().getName().toLowerCase() + ".*")) {
                                            permFlag2 = true;
                                            if (debug) {
                                                plugin.getLogger().warning("Player have bsc." + player.getWorld().getName().toLowerCase() + ".* permission node, continue.");
                                            }
                                        } else {
                                            if (player.hasPermission("bsc." + player.getWorld().getName().toLowerCase() + "." + key.toLowerCase())) {
                                                permFlag2 = true;
                                                if (debug) {
                                                    plugin.getLogger().warning("Player have bsc." + player.getWorld().getName().toLowerCase() + "." + key.toLowerCase() + " permission node, continue.");
                                                }
                                            } else {
                                                permFlag2 = false;
                                                if (debug) {
                                                    plugin.getLogger().warning("Player don't have bsc." + player.getWorld().getName().toLowerCase() + "." + key.toLowerCase() + " permission node, reject.");
                                                }
                                            }
                                        }
                                    } else {
                                        permFlag2 = true;
                                        if (debug) {
                                            plugin.getLogger().warning("Event not triggered by player, can't check permissions, pass.");
                                        }
                                    }
                                    if (permFlag2) {
                                        boolean cancel = false;
                                        if (worldSpecialThemSelf) {
                                            if (plugin.getConfig().getBoolean("BreedAnimals." + world + "." + key + ".Cancel", false)) {
                                                cancel = true;
                                            }
                                        } else {
                                            if (plugin.getConfig().getBoolean("BreedAnimals." + world + "." + key + ".Cancel", false) ||
                                                    plugin.getConfig().getBoolean("BreedAnimals.*." + key + ".Cancel", false)) {
                                                cancel = true;
                                            }
                                        }
                                        if (cancel) {
                                            event.setCancelled(true);
                                            if (debug) {
                                                plugin.getLogger().warning("[ " + world + "." + key + " ] event canceled");
                                            }
                                            break;
                                        }
                                        int experience;
                                        if (worldSpecialThemSelf) {
                                            experience = plugin.getConfig().getInt("BreedAnimals." + world + "." + key + ".Experience", -1);
                                        } else {
                                            experience = plugin.getConfig().getInt("BreedAnimals.*." + key + ".Experience", -1);
                                            int exp2 = plugin.getConfig().getInt("BreedAnimals." + world + "." + key + ".Experience", -1);
                                            if (exp2 != -1) {
                                                experience = exp2;
                                            }
                                        }
                                        if (experience >= 0) {
                                            event.setExperience(experience);
                                            if (debug) {
                                                plugin.getLogger().warning("[ " + world + "." + key + " ] experience is set to " + experience);
                                            }
                                        }
                                        List<String> commands;
                                        if (worldSpecialThemSelf) {
                                            commands = plugin.getConfig().getStringList("BreedAnimals." + world + "." + key + ".RunCustom.Commands");
                                        } else {
                                            commands = plugin.getConfig().getStringList("BreedAnimals." + world + "." + key + ".RunCustom.Commands");
                                            if (!world.equalsIgnoreCase("*")) {
                                                List<String> cmd2 = plugin.getConfig().getStringList("BreedAnimals.*." + key + ".RunCustom.Commands");
                                                if (cmd2.size() > 0) {
                                                    commands.addAll(cmd2);
                                                }
                                            }
                                        }
                                        Map<String, List<String>> tmpPermissions = new HashMap<>();
                                        List<String> oldCommands = new ArrayList<>();
                                        List<String> newCommands = new ArrayList<>();
                                        for (String cmd : commands) {
                                            String[] sp = cmd.split("#");
                                            if (sp.length > 1) {
                                                int index = Integer.parseInt(sp[0]);
                                                int startWithIndex = sp[0].length() + 1;
                                                while (newCommands.size() <= index) {
                                                    newCommands.add("");
                                                }
                                                String k = cmd.substring(startWithIndex);
                                                newCommands.set(index, k);
                                                if (k.startsWith("bypass:")) {
                                                    if (!tmpPermissions.containsKey(k)) {
                                                        tmpPermissions.put(k, new ArrayList<>());
                                                    }
                                                    List<String> permissions;
                                                    if (worldSpecialThemSelf) {
                                                        permissions = plugin.getConfig().getStringList("BreedAnimals." + world + "." + key + ".RunCustom.TempPermissions");
                                                    } else {
                                                        permissions = plugin.getConfig().getStringList("BreedAnimals." + world + "." + key + ".RunCustom.TempPermissions");
                                                        if (!world.equalsIgnoreCase("*")) {
                                                            List<String> perm2 = plugin.getConfig().getStringList("BreedAnimals.*." + key + ".RunCustom.TempPermissions");
                                                            if (perm2.size() > 0) {
                                                                permissions.addAll(perm2);
                                                            }
                                                        }
                                                    }
                                                    for (String perm : permissions) {
                                                        String[] sp2 = perm.split("#");
                                                        if (sp2.length > 1) {
                                                            int index2 = Integer.parseInt(sp2[0]);
                                                            if (index2 == index) {
                                                                tmpPermissions.get(k).add(perm.substring(sp2[0].length() + 1));
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                oldCommands.add(cmd);
                                            }
                                        }
                                        for (String cmd : oldCommands) {
                                            int index = 0;
                                            boolean canInsert = false;
                                            for (; index < newCommands.size(); index++) {
                                                if (newCommands.get(index) == null) {
                                                    canInsert = true;
                                                    break;
                                                }
                                            }
                                            if (canInsert) {
                                                while (newCommands.size() <= index) {
                                                    newCommands.add("");
                                                }
                                                newCommands.set(index, cmd);
                                            } else {
                                                newCommands.add(cmd);
                                            }
                                        }
                                        for (String cmd : newCommands) {
                                            if (cmd != null && !cmd.isEmpty()) {
                                                String originalCmd = cmd;
                                                cmd = cmd.replace("<world>", event.getEntity().getWorld().getName());
                                                if (event.getFather() != null) {
                                                    cmd = cmd.replace("<father.x>", String.valueOf(event.getFather().getLocation().getBlockX()));
                                                    cmd = cmd.replace("<father.y>", String.valueOf(event.getFather().getLocation().getBlockY()));
                                                    cmd = cmd.replace("<father.z>", String.valueOf(event.getFather().getLocation().getBlockZ()));
                                                }
                                                if (event.getMother() != null) {
                                                    cmd = cmd.replace("<mother.x>", String.valueOf(event.getMother().getLocation().getBlockX()));
                                                    cmd = cmd.replace("<mother.y>", String.valueOf(event.getMother().getLocation().getBlockY()));
                                                    cmd = cmd.replace("<mother.z>", String.valueOf(event.getMother().getLocation().getBlockZ()));
                                                }
                                                if (event.getBreeder() != null) {
                                                    cmd = cmd.replace("<breeder.x>", String.valueOf(event.getBreeder().getLocation().getBlockX()));
                                                    cmd = cmd.replace("<breeder.y>", String.valueOf(event.getBreeder().getLocation().getBlockY()));
                                                    cmd = cmd.replace("<breeder.z>", String.valueOf(event.getBreeder().getLocation().getBlockZ()));
                                                    if (event.getBreeder() instanceof Player) {
                                                        Player player = (Player) event.getBreeder();
                                                        cmd = cmd.replace("<player>", player.getName());
                                                    }
                                                }
                                                Pattern pattern = Pattern.compile("<(\\w+;)+\\w+>");
                                                Matcher matcher = pattern.matcher(cmd);
                                                while (matcher.find()) {
                                                    String[] rdmStr = matcher.group().split(";");
                                                    if (rdmStr[0].length() > 0) {
                                                        rdmStr[0] = rdmStr[0].substring(1);
                                                    }
                                                    if (rdmStr[rdmStr.length - 1].length() > 0) {
                                                        rdmStr[rdmStr.length - 1] = rdmStr[rdmStr.length - 1].substring(0, rdmStr[rdmStr.length - 1].length() - 1);
                                                    }
                                                    cmd = cmd.replace(matcher.group(), rdmStr[new Random().nextInt(rdmStr.length - 1)]);
                                                }
                                                if (cmd.startsWith("bypass:")) {
                                                    if (event.getBreeder() instanceof Player) {
                                                        List<String> permissions = tmpPermissions.get(originalCmd);
                                                        Player player = (Player) event.getBreeder();
                                                        try {
                                                            String cmdStr = cmd.substring(7);
                                                            if (permissions != null) {
                                                                if (permissions.size() > 0) {
                                                                    for (String perm : permissions) {
                                                                        if (perm.startsWith("!")) {
                                                                            plugin.permission.playerRemove(player, perm.substring(1));
                                                                            if (debug) {
                                                                                plugin.getLogger().warning("[ " + world + "." + key + " ] remove permission: " + perm.substring(1));
                                                                            }
                                                                        } else if (perm.startsWith("-")) {
                                                                            plugin.permission.playerAdd(player, perm);
                                                                            if (debug) {
                                                                                plugin.getLogger().warning("[ " + world + "." + key + " ] add negative permission: " + perm);
                                                                            }
                                                                        } else {
                                                                            plugin.permission.playerAdd(player, perm);
                                                                            if (debug) {
                                                                                plugin.getLogger().warning("[ " + world + "." + key + " ] add permission: " + perm);
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    player.setOp(true);
                                                                    if (debug) {
                                                                        plugin.getLogger().warning("[ " + world + "." + key + " ] PLAYER GET TEMP OP: " + player.getName());
                                                                    }
                                                                }
                                                            }
                                                            if (cmdStr.startsWith("/")) {
                                                                if (debug) {
                                                                    plugin.getLogger().warning("[ " + world + "." + key + " ] run bypass command: " + cmdStr);
                                                                }
                                                                player.performCommand(cmdStr.substring(1));
                                                            } else {
                                                                if (debug) {
                                                                    plugin.getLogger().warning("[ " + world + "." + key + " ] player bypass say: " + cmd);
                                                                }
                                                                player.chat(cmdStr);
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        } finally {
                                                            if (permissions != null) {
                                                                if (permissions.size() > 0) {
                                                                    for (String perm : permissions) {
                                                                        if (perm.startsWith("!")) {
                                                                            plugin.permission.playerAdd(player, perm.substring(1));
                                                                            if (debug) {
                                                                                plugin.getLogger().warning("[ " + world + "." + key + " ] add permission: " + perm.substring(1));
                                                                            }
                                                                        } else if (perm.startsWith("-")) {
                                                                            plugin.permission.playerRemove(player, perm);
                                                                            if (debug) {
                                                                                plugin.getLogger().warning("[ " + world + "." + key + " ] remove negative permission: " + perm);
                                                                            }
                                                                        } else {
                                                                            plugin.permission.playerRemove(player, perm);
                                                                            if (debug) {
                                                                                plugin.getLogger().warning("[ " + world + "." + key + " ] remove permission: " + perm);
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    player.setOp(false);
                                                                    if (debug) {
                                                                        plugin.getLogger().warning("[ " + world + "." + key + " ] REMOVE PLAYER TEMP OP: " + player.getName());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else if (cmd.startsWith("console:")) {
                                                    String cmdStr = cmd.substring(8);
                                                    if (debug) {
                                                        plugin.getLogger().warning("[ " + world + "." + key + " ] run console command: " + cmdStr);
                                                    }
                                                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmdStr);
                                                } else {
                                                    if (event.getBreeder() instanceof Player) {
                                                        Player player = (Player) event.getBreeder();
                                                        if (cmd.startsWith("/")) {
                                                            if (debug) {
                                                                plugin.getLogger().warning("[ " + world + "." + key + " ] run command: " + cmd);
                                                            }
                                                            player.performCommand(cmd.substring(1));
                                                        } else {
                                                            if (debug) {
                                                                plugin.getLogger().warning("[ " + world + "." + key + " ] player say: " + cmd);
                                                            }
                                                            player.chat(cmd);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        boolean remove = false;
                                        if (worldSpecialThemSelf) {
                                            remove = plugin.getConfig().getBoolean("BreedAnimals." + world + "." + key + ".RunCustom.RemoveChild", false);
                                        } else {
                                            if (plugin.getConfig().getBoolean("BreedAnimals." + world + "." + key + ".RunCustom.RemoveChild", false) ||
                                                    plugin.getConfig().getBoolean("BreedAnimals.*." + key + ".RunCustom.RemoveChild", false)) {
                                                remove = true;
                                            }
                                        }
                                        if (remove) {
                                            event.getEntity().remove();
                                            if (debug) {
                                                plugin.getLogger().warning("[ " + world + "." + key + " ] child removed.");
                                            }
                                        }
                                    }
                                } else if (debug) {
                                    plugin.getLogger().warning("[ " + world + "." + key + " ] World " + event.getEntity().getWorld().getName() + " is have their special config, wait their run.");
                                }
                            }
                        }
                    } else if (debug) {
                        plugin.getLogger().warning("Event triggered world is " + event.getEntity().getWorld().getName() + ", but now check world is " + world);
                    }
                }
            }
        } else {
            if (debug) {
                plugin.getLogger().warning("Event triggered world " + event.getEntity().getWorld().getName() + " is not in EnableWorlds list.");
            }
        }
        if (debug) {
            plugin.getLogger().warning("============================================================");
        }
    }
}
