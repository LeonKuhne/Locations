package org.Locations;

import java.util.Map;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

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
        Location loc = getLastLoc(player);
        if (loc != null) {
            player.teleport(loc);
            player.sendMessage("returning you to your last location in " + ChatColor.GREEN + player.getWorld().getName());
        }
    }


    // UTIL
    //
    
    public Location getLastLoc(Player player) {
        Map<World, Location> worlds = lastLocations.get(player);
        if (worlds != null) {
            
            World world = player.getWorld();
            if (worlds.containsKey(world)) {
                return worlds.get(world);
            }
        }

        return null;
    }

    public void load() {
        remember = false;
        delay = 0;
    }
}
