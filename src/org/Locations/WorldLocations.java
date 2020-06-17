package org.Locations;

import java.util.Map;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author leee leee
 */
public class WorldLocations {
    
    private Map<Player, Map<World, Location>> lastLocations;

    public boolean remember;
    public int delay;

    public WorldLocations() {
        lastLocations = new HashMap();
        load();
    }


    // ACTIONS
    //

    /**
     * save players last location    
     */
    public void save(Player player) {
        Location loc = player.getLocation().clone();

        if (!lastLocations.contains(player)) {
            // create
            Map<World, Location> worlds = new HashMap();
            worlds.put(player.getWorld(), loc);
            lastlocations.put(player, worlds);

        } else {
            // add
            List<Location> worlds = lastLocations.get(player);
            worlds.put(player.getWorld(), loc);
        }
    }

    public void teleport(Player player) {
        Map<World, Location> worlds = lastLocations.get(player);
        Location loc = worlds.get(player.getWorld());
        player.teleport(loc);
    }


    // UTIL
    //

    public void load() {
        remember = false;
        delay = 0;
    }
}
