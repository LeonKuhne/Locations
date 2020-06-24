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

    private static File findShortcutFile(Plugin plugin) throws IOException {
        // find
        File file = new File(plugin.getDataFolder(), "leeslocs.yml");

        // or create
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("leeslocs.yml", false);
        }
        
        return file;
    }
    
    public static FileConfiguration findConfig(Plugin plugin) {
        FileConfiguration config = new YamlConfiguration();
        try {

            // load the file into a config
            File file = findShortcutFile(plugin);
            if (file.exists()) {
                config.load(file);
                return config;
            }

        } catch (IOException e) {
            plugin.getLogger().info("Failed to load saved locations, IO error");
        }

        return null;
    }

    public static void loadShortcuts(Plugin plugin) {
        // load config
        FileConfiguration config = findConfig();

        // read in the commands
        System.out.println(" -" + config.getKeys(false));
        for (String name : config.getKeys(false)) {
            Location loc = config.getLocation(name);
            Locations.tele.set(name, loc);
        }
    }

    public static void saveShortcuts(Plugin plugin, Map<String, Location> locations) {
        // load config
        FileConfiguration config = findConfig();

        for (String name : locations.keySet()) {
            Location loc = locations.get(name);

            // add to config
            config.set(name, loc);

            // add the location to the config

            config.setDefaults();

            
            // save the config file
            config.save();
        }
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
