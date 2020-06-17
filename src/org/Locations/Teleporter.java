package org.Locations;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author leee leee
 */
public class Teleporter {

    Map<String, Location> locations;
    Map<World, WorldLocations> lastLocs;

    public Teleporter() {
        locations = new HashMap();
    }


    // ACTIONS
    //
    
    public void teleport(Player player, String name) {
        // save world location
        WorldLocations worldLocs = getWorldLocationsByName(name);
        World world = player.getWorld();
        if (containsKey(world)) {
            WorldLocations worldLocs = lastLocs.get(world);
            if (worldLocs.remember){
                worldLocs.save(player);
            }
        }

        // teleport
        player.teleport(locations.get(name));
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
    public boolean remember(String name) {
        WorldLocations worldLocs = getWorldLocationsByName(name);
        if (worldLocs != null) {
            worldLocs.remember = !worldLocs.remember;
            return worldLocs.remember;
        }
        return false;
    }
    
    public void delay(String name, int delay) {
        WorldLocations worldLocs = getWorldLocationsByName(name);
        if (worldLocs != null) {
            worldLocs.delay = delay;
        }
        return false;
    }


    // UTIL
    //
    
    public int delay(String name) {
        WorldLocations worldLocs = getWorldLocationsByName(name);
        return worldLocs.delay;
    }

    public Set<String> getNames() {
        return locations.keySet();
    }

    private WorldLocations getWorldLocationsByName(String name) {
        if (locations.containsKey(name)) {
            World world = locations.get(name).getWorld();
            if (lastLocs.containsKey(world)) {
                WorldLocations worldLocs = lastLocs.get(world);
                return worldLocs;
            }
        }
        return null;
    }

    public String toString() {
        return getNames().toString();
    }
}
