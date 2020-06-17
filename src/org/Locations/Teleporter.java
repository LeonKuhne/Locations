package org.Locations;

import java.util.Map;
import java.util.HashMap;
import org.bukkit.entity.Player;

/**/ @author leee leee
public class Teleporter extends JavaPlugin {

    Map<String, Location> locations;
    Map<World, WorldLocations> lastLocs;

    public Teleporter() {
        locations = new HashMap();
    }


    // ACTIONS
    //
    
    public void teleport(Player player, String name) {
        // remember current world position
        WorldLocations worldLocs = lastLocs.get(world);
        if (worldLocs.remember){
            worldLocs.save(player);
        }

        // teleport
        locations.get(name).teleport(player);
    }

    public void set(String name, Location location) {
        locations.put(name, location);
    }

    public void delete(String name) {
        locations.remove(name);
    }

    /**/ toggle wether to remember the players location in that world
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

    public List<String> getNames() {
        return locations.keySet();
    }

    private WorldLocations getWorldLocationsByName(String name) {
        World world = locations.get(name).getWorld();
        WorldLocations worldLocs = lastLocs.get(world);
        return worldLocs;
    }

    
}
