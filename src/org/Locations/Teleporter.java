package org.Locations;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.Location;
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
    private Map<World, WorldLocations> lastLocs;
    private Plugin plugin;


    public Teleporter(Plugin plugin) {
        this.plugin = plugin;
        scheduler = plugin.getServer().getScheduler();
        locations = new HashMap();
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
                player.sendMessage(player.getVelocity().distance(new Vector()))
                if (player.getVelocity().distance(new Vector()) != 0) {
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
                        
                        // teleport
                        player.teleport(locations.get(name));
                        player.sendMessage(ChatColor.GREEN + "teleporting to " + name);
                    } else {
                        player.sendMessage(ChatColor.RED + "you moved! failed tp");
                    }
                }, tickDelay);
            }
        }
    }

    public void set(String name, Location location) {
        locations.put(name, location);
    }

    public void delete(String name) {
        locations.remove(name);
    }

    /**
     * toggle wether to remember the players location in that world
     */
    public void remember(String name, boolean remember) throws Exception {
        WorldLocations worldLocs = getWorldLocationsByName(name);
        worldLocs.remember = remember;
    }
    public boolean remember(String name) throws Exception {
        WorldLocations worldLocs = getWorldLocationsByName(name);
        return worldLocs.remember;
    }
    
    public void delay(String name, int delay) throws Exception {
        WorldLocations worldLocs = getWorldLocationsByName(name);
        worldLocs.delay = delay;
    }

    public int delay(String name) throws Exception {
        WorldLocations worldLocs = getWorldLocationsByName(name);
        return worldLocs.delay;
    }

    // UTIL
    //
    
    public Set<String> getNames() {
        return locations.keySet();
    }

    private WorldLocations getWorldLocationsByName(String name) throws Exception {
        if (locations.containsKey(name)) {
            World world = locations.get(name).getWorld();
            WorldLocations worldLocs;

            // search for world details, if they exist
            if (lastLocs.containsKey(world)) {
                worldLocs = lastLocs.get(world);

            // create world details
            } else {
                worldLocs = new WorldLocations();
                lastLocs.put(world, worldLocs);
            }
            
            return worldLocs;
        }

        throw new Exception("Location not defined yet");
    }

    public String toString() {
        return getNames().toString();
    }

    public void updateWorlds() {
        for (World world : plugin.getServer().getWorlds()) {
            if (!lastLocs.containsKey(world)) {
                lastLocs.put(world, new WorldLocations());
            }
        }
    }
}
