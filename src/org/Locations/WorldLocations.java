package org.Locations;

import java.util.Map;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.util.Vector;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * @author leee leee
 */
@SerializableAs("Vector")
public class WorldLocations implements ConfigurationSerializable {
    
    private Map<Player, Map<String, Location>> lastLocations;

    public boolean remember;
    public int delay;


    public WorldLocations() {
        lastLocations = new HashMap();
        load();
    }

    
    // SERIALIZE
    //
    
    public Map<String, Object> serialize() {
        Map<String, Object> cereal = new HashMap();
        cereal.put("remember", remember);
        cereal.put("delay", delay);
        cereal.put("locations", lastLocations);
        return cereal;
    }

    public static Vector deserialize(Map<String, Object> args) {
        System.out.println("got args: " + args);
        return new Vector();
    }
    
    // ACTIONS
    //

    /**
     * save players last location    
     */
    public void save(Player player) {
        Location loc = player.getLocation().clone();
        String worldName = player.getWorld().getName();
        Map<String, Location> worlds;

        if (!lastLocations.containsKey(player)) {
            // create player
            worlds = new HashMap();
            lastLocations.put(player, worlds);

        } else {
            // add location to player
            worlds = lastLocations.get(player);
            worlds.put(worldName, player.getLocation());
        }
    }

    public void teleport(Player player, World world) {
        Location loc = getLastLoc(player, world);
        if (loc != null && !player.getWorld().equals(loc.getWorld())) {
            player.teleport(loc);
            player.sendMessage("returning you to your last location in " + ChatColor.GREEN + player.getWorld().getName());
        }
    }


    // UTIL
    //
    
    public Location getLastLoc(Player player, World world) {
        Map<String, Location> worlds = lastLocations.get(player);
        String worldName = world.getName();

        if (worlds != null) {
            if (worlds.containsKey(worldName)) {
                return worlds.get(worldName);
            }
        }
        return null;
    }

    public void load() {
        remember = false;
        delay = 0;
    }
}
