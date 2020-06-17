package org.Locations;

import java.util.Map;
import java.util.HashMap;
import org.bukkit.Location;

/**
 * @author leee leee
 */
public class WorldLocations extends JavaPlugin {
    
    private Map<Player, Set<Location>> lastLocations;

    public boolean remember;
    public int delay;

    public Teleporter() {
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
            List<Location> lastLocs = new HashSet();
            lastLocs.add(loc);
            lastlocations.put(player, lastLocs);

        } else {
            // add
            List<Location> lastLocs = lastLocations.get(player);
            lastLocs.add(loc);
        }
    }


    // UTIL
    //

    public void load() {
        remember = false;
        delay = 0;
    }
}
