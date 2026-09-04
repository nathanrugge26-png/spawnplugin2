package com.example.spawnplugin;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnPlugin extends JavaPlugin implements CommandExecutor {

    // Fixed spawn coordinates
    private static final double SPAWN_X = 304;
    private static final double SPAWN_Y = 63;
    private static final double SPAWN_Z = 1007;

    @Override
    public void onEnable() {
        getCommand("spawn").setExecutor(this);
        getCommand("c").setExecutor(this);
        getCommand("co").setExecutor(this);
        getLogger().info("SpawnPlugin has been enabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName().toLowerCase()) {
            case "spawn":
                return handleSpawn(sender);
            case "c":
                return handleCreative(sender);
            case "co":
                return handleSurvival(sender);
            default:
                return false;
        }
    }

    private boolean handleSpawn(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        World world = player.getWorld();
        Location spawnLocation = new Location(world, SPAWN_X, SPAWN_Y, SPAWN_Z);

        player.teleport(spawnLocation);
        player.sendMessage("Teleported to spawn.");
        return true;
    }

    private boolean handleCreative(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (player.getGameMode() == GameMode.CREATIVE) {
            sender.sendMessage("You are already in Creative mode.");
            return true;
        }

        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage("You are now in Creative mode.");
        return true;
    }

    private boolean handleSurvival(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (player.getGameMode() == GameMode.SURVIVAL) {
            sender.sendMessage("You are already in Survival mode.");
            return true;
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage("You are now in Survival mode.");
        return true;
    }
}
