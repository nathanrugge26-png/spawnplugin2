package com.example.spawnplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
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
        getLogger().info("SpawnPlugin has been enabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (command.getName().toLowerCase()) {
            case "spawn":
                return handleSpawn(sender);
            case "c":
                return handleOp(sender, args);
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

    private boolean handleOp(CommandSender sender, String[] args) {
        // Permission is also enforced declaratively via plugin.yml (spawnplugin.op),
        // but we check again here as defense in depth.
        if (!sender.hasPermission("spawnplugin.op")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /c <username>");
            return true;
        }

        String targetName = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

        if (target == null || (!target.hasPlayedBefore() && !target.isOnline())) {
            sender.sendMessage("That player has never joined this server.");
            return true;
        }

        if (target.isOp()) {
            sender.sendMessage(targetName + " is already an operator.");
            return true;
        }

        target.setOp(true);
        sender.sendMessage("Granted operator status to " + targetName + ".");

        if (target.isOnline()) {
            Player onlineTarget = target.getPlayer();
            if (onlineTarget != null) {
                onlineTarget.sendMessage("You have been granted operator status by " + sender.getName() + ".");
            }
        }

        getLogger().info(sender.getName() + " granted OP to " + targetName + " via /c command.");
        return true;
    }
}
