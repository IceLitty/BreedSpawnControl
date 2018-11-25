package com.gmail.litalways.BreedSpawnControl.Commands;

import com.gmail.litalways.BreedSpawnControl.BreedSpawnControl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {

    private BreedSpawnControl plugin;

    public Reload(BreedSpawnControl plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("All allow commands:");
            commandSender.sendMessage("/bsc reload - Reload the plugin.");
            return true;
        } else {
            if (strings[0].equalsIgnoreCase("reload")) {
                if (commandSender.isOp() || commandSender.hasPermission("bsc.reload")) {
                    plugin.reloadConfig();
                    commandSender.sendMessage("BreedSpawnControl reload successfully.");
                    return true;
                }
            }
        }
        return false;
    }
}
