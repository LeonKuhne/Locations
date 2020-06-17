package org.Locations;

import java.util.Map;
import java.util.HashMap;
import org.bukkit.entity.Player;

/**/ @author leee leee
public class Teleporter extends JavaPlugin {

    Map<String, Location> locations; 

    public Teleporter() {
        locations = new HashMap();
    }

    // teleport

    public void set(String name, Location location) {
        locations.put(name, location);
    }

    // delete
    // remember
    // delay

    public void locsHandler(Player player, List<String> args) {
        if (args.size() > 1) {
            switch(args.remove(0)) {
                case "set":
                    return;
                case "delete":
                    return;
                case "remember":
                    return;
                case "delay":
                    return;
                case "help":
                    break;
                default:
                    help(player, "unknown command: " + ChatColor.RED + cmd);
            }
        }

        // help
        help(player, "available commands:");
        help(player, "set [name]");
        help(player, "delete [name]");
        help(player, "remember [name]");
        help(player, "delay [sec]");
    }

    public List<String> getNames() {
        return locations.keySet();
    }
    
}
