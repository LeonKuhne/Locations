package org.Locations;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.lang.Math;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.World;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * @author leee leee
 */
public class Teleporter {

    private BukkitScheduler scheduler;
    private Map<String, Location> locations;
    private Map<String, Command> shortcuts;
    private Map<World, WorldLocations> lastLocs;
    private Plugin plugin;


    public Teleporter(Plugin plugin) {
        this.plugin = plugin;
        scheduler = plugin.getServer().getScheduler();

        locations = Util.loadShortcuts(plugin);
        shortcuts = Util.createShortcuts(plugin, locations);
        lastLocs = new HashMap();

        updateWorlds();
    }


    // ACTIONS
    //
    
    public void teleport(Player player, String name) {
        World world = player.getWorld();

        if (lastLocs.containsKey(world)) {
            WorldLocations worldLocs = lastLocs.get(world);

            if (worldLocs != null) {

                // save world location
                if (worldLocs.remember){
                    worldLocs.save(player);
                }

                // if moving don't teleport
                Vector vel = player.getVelocity();
                if (vel.getX() != 0 || vel.getZ() != 0 || Math.abs(vel.getY()) >= 0.1) {
                    player.sendMessage("You're moving to fast, can't teleport");
                    return;
                }

                // teleport after delay
                int delay = worldLocs.delay;
                long tickDelay = delay * 20l;
                if (delay > 0) {
                    player.sendMessage("Stand still for " + ChatColor.AQUA + delay + ChatColor.RESET + " seconds");
                }

                // teleport after delay
                Location before = player.getLocation().getBlock().getLocation().clone();
                scheduler.runTaskLater(plugin, () -> {
                    // check if moved
                    if (tickDelay == 0 || before.equals(player.getLocation().getBlock().getLocation())) {
                        teleportNow(player, name);
                    } else {
                        player.sendMessage(ChatColor.RED + "you moved! failed tp");
                    }
                }, tickDelay);
            }
        }
    }

    private void teleportNow(Player player, String locationName) {
        // determine destination
        Location destiLoc = locations.get(locationName);
        World destiWorld = destiLoc.getWorld();
        WorldLocations destiWorldLocs = getWorldLocations(destiWorld);
        Location prevLoc = destiWorldLocs.getLastLoc(player, destiWorld);

        // if previous location exists, and world set to remember
        if (prevLoc != null && destiWorldLocs.remember) {
            if (player.getWorld().equals(destiWorld)) {
                player.sendMessage(ChatColor.RED + "You're already in that world silly");
            } else {
                player.sendMessage(ChatColor.AQUA + "Returning to " + ChatColor.GREEN + destiWorld.getName());
                player.teleport(prevLoc);
            }
        }

        // otherwise, use locations
        else {
            player.sendMessage(ChatColor.AQUA + "Teleporting to " + ChatColor.GOLD + locationName);
            player.teleport(destiLoc);
        }
    }

    // assumes player is already in the desired world
    public void back(Player player) {
        World destiWorld = player.getWorld();
        WorldLocations worldLocs = lastLocs.get(destiWorld);
        if (worldLocs != null) {
            worldLocs.teleport(player, destiWorld);
        }
    }

    public void set(String name, Location location) throws Exception {
        Command cmd = Util.registerTeleport(plugin, name);
        shortcuts.put(name, cmd);
        locations.put(name, location);
    }

    public void delete(String name) throws Exception {
        Command cmd = shortcuts.remove(name);
        Util.unregisterTeleport(plugin, cmd);
        locations.remove(name);
    }

    /**
     * toggle wether to remember the players location in that world
     */
    public void remember(String name, boolean remember) throws Exception {
        getWL(name).remember = remember;
    }
    
    public boolean remember(String name) throws Exception {
        return getWL(name).remember;
    }
    
    public void delay(String name, int delay) throws Exception {
        getWL(name).delay = delay;
    }

    public int delay(String name) throws Exception {
        return getWL(name).delay;
    }

    public void save() {
        try {
            Util.saveShortcuts(plugin, locations);
        } catch (Exception e) {
            plugin.getLogger().info("failed to save locations");
        }
    }

    // UTIL
    //
    
    public Set<String> getNames() {
        return locations.keySet();
    }

    private WorldLocations getWL(String name) throws Exception {
        WorldLocations worldLocs;
        try {
            worldLocs = getWorldLocations(name);
        } catch (Exception e) {
            worldLocs = getWorldLocationsByWorldName(name);
        }
        return worldLocs;
    }

    private WorldLocations getWorldLocationsByWorldName(String worldName) throws Exception {
        World world = plugin.getServer().getWorld(worldName);
        if (world != null)  {
            return getWorldLocations(world);
        }
        throw new Exception("World name not identified");
    }

    private WorldLocations getWorldLocations(String name) throws Exception {
        if (locations.containsKey(name)) {
            World world = locations.get(name).getWorld();
            return getWorldLocations(world);
        }
        throw new Exception("Location not defined yet");
    }

    private WorldLocations getWorldLocations(World world) {
        WorldLocations worldLocs;

        // search for world details, if they exist
        if (lastLocs.containsKey(world)) {
            worldLocs = lastLocs.get(world);
            return worldLocs;
        }
        
        // create world details, or...
        // throw new Exception("World not loaded, try /locs reload");
        worldLocs = new WorldLocations();
        lastLocs.put(world, worldLocs);
        return worldLocs;
    }

    public String toString() {
        return (locations.size() > 0) ? getNames().toString() : "None exist yet";
    }

    public void updateWorlds() {
        for (World world : plugin.getServer().getWorlds()) {
            if (!lastLocs.containsKey(world)) {
                lastLocs.put(world, new WorldLocations());
            }
        }
    }

}
