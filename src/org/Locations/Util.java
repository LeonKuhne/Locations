package org.Locations;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandMap;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import java.lang.reflect.Field;
import java.io.IOException;
import org.bukkit.Server;

public class Util {

    private static File findOrCreateConfig(Plugin plugin) {
        // find
        File file = new File(plugin.getDataFolder(), "leeslocs.yml");

        // or create
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("leeslocs.yml", false);
        }
        
        return file;
    }
    
    public static void loadShortcuts(Plugin plugin) {
        FileConfiguration config = new YamlConfiguration();
        File file = findOrCreateConfig(plugin);
        
        if (file.exists()) {
            try {
                config.load(file);
                
                // read in the commands
                System.out.println(" -" + config.getKeys(false));
                for (String name : config.getKeys(false)) {
                    Location loc = null; // get this from the config value pair
                    Locations.tele.set(name, loc);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveShortcuts(Plugin plugin) {
        // save

    }

    public static Command registerTeleport(Plugin plugin, String string) throws Exception {
        TeleportCommand tpCommand = new TeleportCommand(string, Locations.tele);
        getCommandMap(plugin).register(string, tpCommand);
        return tpCommand;
    }

    public static void unregisterTeleport(Plugin plugin, Command command) throws Exception {
        command.unregister(getCommandMap(plugin));
    }

    private static CommandMap getCommandMap(Plugin plugin) throws Exception {
        Server server = plugin.getServer();
        Field bukkitCmdMap = server.getClass().getDeclaredField("commandMap");
        bukkitCmdMap.setAccessible(true);
        return (CommandMap) bukkitCmdMap.get(server);
    }
}
