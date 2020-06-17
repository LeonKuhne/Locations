package org.Locations;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author leee leee
 */
public class Locations extends JavaPlugin {

    private Teleporter tele;
    private Map<String, Command> shortcuts;

    
    // DEFAULT
    //

    @Override
    public void onEnable() {
        shortcuts = new HashMap();
        tele = new Teleporter(this);
        getLogger().info("starting");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("stopping");
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] args) {;
        // is player
        if (cs instanceof Player) {
            Player player = (Player) cs;

            // admin commands
            if (player.isOp() && (string.equals("locs") || string.equals("locations"))) {
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

        } else if (args.size() >= 2) {
            String cmd = args.remove(0).toLowerCase();
            String name = args.remove(0).toLowerCase();

            switch(cmd) {

                case "set":
                    try {
                        registerTeleport(name);
                        tele.set(name, player.getLocation());
                        help(player, "Added location " + ChatColor.GREEN + name);
                    } catch (Exception e) {
                        error(player, e.getMessage() + ChatColor.RED + name);
                    }
                    return;

                case "delete":
                    try {
                        unregisterTeleport(name);
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

                        help(player, "Location remembering for " + ChatColor.GOLD + player.getWorld().getName() + ChatColor.RESET + " is set to " + ChatColor.GREEN + remember);

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

                        help(player, "Delay for " + ChatColor.GOLD + player.getWorld().getName() + ChatColor.RESET + " is set to " + ChatColor.GREEN + delay);
                    } catch (Exception e) {
                        error(player, e.getMessage());
                    }
                    return;

                default:
                    help(player, "unknown command: " + ChatColor.RED + cmd);
            }
        }

        // help
        help(player, "available commands:");
        help(player, ChatColor.GREEN + "set [name]");
        help(player, ChatColor.GREEN + "delete [name]");
        help(player, ChatColor.GREEN + "remember [name] [true/false]");
        help(player, ChatColor.GREEN + "delay [name] [sec]");
        help(player, ChatColor.GREEN + "(nothing)");
        return;
    }

    
    // REGISTER COMMANDS
    //

    public void registerTeleport(String string) throws Exception {
        TeleportCommand tpCommand = new TeleportCommand(string, tele);
        getCommandMap().register(string, tpCommand);
        shortcuts.put(string, tpCommand);
    }

    public void unregisterTeleport(String string) throws Exception {
        Command tpCommand = shortcuts.remove(string);
        tpCommand.unregister(getCommandMap());
    }

    private CommandMap getCommandMap() throws Exception {
        Field bukkitCmdMap = getServer().getClass().getDeclaredField("commandMap");
        bukkitCmdMap.setAccessible(true);
        return (CommandMap) bukkitCmdMap.get(getServer());
    }
}
