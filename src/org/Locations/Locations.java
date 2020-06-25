package org.Locations;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * @author leee leee
 */
public class Locations extends JavaPlugin {

    Map<String, Command> shortcuts;
   
    private final HashMap<String, String> helpDesc = new HashMap() {{
        put("/locs", "list available locations");
        put("/locs set/add [name]", "creates a new location");
        put("/locs delete/remove [name]", "delete a location");
        put("/locs remember [world/name] [true/false]", "remember last location");
        put("/locs delay [world/name] [sec]", "prevents teleporting during combat");
        put("/locs reload", "reload the currently existing worlds");
    }};
    
    public static Teleporter tele;

    static {
        ConfigurationSerialization.registerClass(WorldLocations.class, "WorldLocations");
    }


    // DEFAULT
    //

    @Override
    public void onEnable() {
        shortcuts = new HashMap();
        tele = new Teleporter(this);

        getServer().getPluginManager().registerEvents(new WorldSwitchListener(), this);
        getLogger().info("loading locations");
    }
    
    @Override
    public void onDisable() {
        tele.save();
        getLogger().info("storing locations");
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] args) {;
        // is player
        if (cs instanceof Player) {
            Player player = (Player) cs;

            // admin commands
            if (player.hasPermission("leeeleees.locs") && (string.equals("locs") || string.equals("locations"))) {
                locsHandler(player, new ArrayList(Arrays.asList(args)));
                return true;
            }
		}
		
        return false;
    }


    // CHAT
    //
    
    private void error(Player player, String message) {
        help(player, ChatColor.RED + message);
    }

    private void help(Player player, String message) {
        player.sendMessage(ChatColor.GRAY + "[LOCS] " + ChatColor.RESET + message);
    }


    // LOCATION COMMAND CONTROLLER
    //

    public void locsHandler(Player player, List<String> args) {
        if (args.size() == 0) {
            // list current locations
            help(player, tele.toString());
            return;
        }
        
        else if (args.size() == 1 && args.get(0).equals("reload")) {
            help(player, "Reloading worlds");
            tele.updateWorlds();
            return;
        }

        else if (args.size() >= 2) {
            String cmd = args.remove(0).toLowerCase();
            String name = args.remove(0).toLowerCase();

            switch(cmd) {

                case "add":
                case "set":
                    try {
                        tele.set(name, player.getLocation());
                        help(player, "Set location " + ChatColor.GREEN + name);
                    } catch (Exception e) {
                        error(player, e.getMessage() + ChatColor.RED + name);
                    }
                    return;

                case "delete":
                case "remove":
                    try {
                        tele.delete(name);
                        help(player, "Deleted location " + ChatColor.GREEN + name);
                    } catch (Exception e) {
                        error(player, e.getMessage());
                    }
                    return;

                case "remember":
                    try {
                        boolean remember;

                        // set
                        if (args.size() > 0) {
                            remember = Boolean.parseBoolean(args.get(0));
                            tele.remember(name, remember);
                        }
                        
                        // get
                        else {
                            remember = tele.remember(name);
                        }

                        help(player, "Location remembering for " + ChatColor.GOLD + name + ChatColor.RESET + " is set to " + ChatColor.GREEN + remember);

                    } catch (Exception e) {
                        error(player, e.getMessage());
                    }
                    return;

                case "delay":
                    try {
                        int delay;

                        // set
                        if (args.size() > 0) {
                            delay = Integer.parseInt(args.get(0));
                            tele.delay(name, delay);
                        }

                        // get
                        else {
                            delay = tele.delay(name);
                        }

                        help(player, "Delay for " + ChatColor.GOLD + name + ChatColor.RESET + " is set to " + ChatColor.GREEN + delay);
                    } catch (Exception e) {
                        error(player, e.getMessage());
                    }
                    return;

                default:
                    help(player, "Unknown command: " + ChatColor.RED + cmd);
            }
        }

        // help
        help(player, ChatColor.GREEN + "----- Locations Help ----");
        for (Map.Entry<String, String> entry : helpDesc.entrySet()) {
            help(player, ChatColor.GREEN + entry.getKey() + ": " + ChatColor.AQUA + entry.getValue());
        }
    }
}
