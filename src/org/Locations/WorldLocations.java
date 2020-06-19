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

        if (!lastLocations.containsKey(player)) {
            // create
            Map<World, Location> worlds = new HashMap();
            worlds.put(player.getWorld(), loc);
            lastLocations.put(player, worlds);

        } else {
            // add
            Map<World, Location> worlds = lastLocations.get(player);
            worlds.put(player.getWorld(), loc);
        }
    }

    public void teleport(Player player) {
        Map<World, Location> worlds = lastLocations.get(player);
        if (worlds != null) {
            Location loc = worlds.get(player.getWorld());
            if (loc != null) {
                player.teleport(loc);
                player.sendMessage("returning you to your last location in " + ChatColor.GREEN + player.getWorld().getName());
            }
        }
    }


    // UTIL
    //

    public void load() {
        remember = false;
        delay = 0;
    }
}
