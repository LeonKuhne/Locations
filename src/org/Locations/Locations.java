package org.Locations;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author leee leee
 */
public class Locations extends JavaPlugin {

    Teleporter tele;

    @Override
    public void onEnable() {
        tele = new Teleporter();
        getLogger().info("starting");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("stopping");
    }

    private void help(Player player, String message) {
        player.sendMessage(ChatColor.GRAY + "[LOCS] " + ChatColor.RESET + message);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] args) {;
        // is player
        if (cs instanceof Player) {
            Player player = (Player) cs;
                
            // check if teleport requested
            if (tele.getNames().contains(string)) {
                tele.teleport(player, string);
                return true;
            }
            
            // command is locs or locations && player has permissions
            else if (player.isOp() && (string.equals("locs") || string.equals("locations"))) {
                locsHandler(player, new ArrayList(Arrays.toList(args)));
                return true;
            }
		}
		
        return false;
    }

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
    
}
