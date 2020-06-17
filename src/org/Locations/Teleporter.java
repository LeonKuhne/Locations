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
        lastLocs = new HashMap();
    }


    // ACTIONS
    //
    
    public void teleport(Player player, String name) {
        World world = player.getWorld();

        // save world location
        if (lastLocs.containsKey(world)) {
            WorldLocations worldLocs = lastLocs.get(world);
            if (worldLocs != null && worldLocs.remember){
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
        worldLocs.remember = !worldLocs.remember;
        return worldLocs.remember;
    }
    
    public void delay(String name, int delay) {
        WorldLocations worldLocs = getWorldLocationsByName(name);
        worldLocs.delay = delay;
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

    private WorldLocations getWorldLocationsByName(String name) throws Exception {
        if (locations.containsKey(name)) {
            World world = locations.get(name).getWorld();
            WorldLocations worldLocs;

            // search for world details, if they exist
            if (lastLocs.containsKey(world)) {
                worldLocs = lastLocs.get(world);

            // create world details
            } else {
                worldLocs = new WorldLocation()
                lastLocs.put(world, worldLocs);
            }
            
            return worldLocs;
        }

        throw new Exception("Location not defined yet");
    }

    public String toString() {
        return getNames().toString();
    }
}
