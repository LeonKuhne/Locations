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
import java.io.FileNotFoundException;
import org.bukkit.Server;

public class Util {

    private static File findFile(Plugin plugin) {
        try {

            // find or create
            File file = new File(plugin.getDataFolder(), "leeslocs.yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                plugin.saveResource("leeslocs.yml", false);
            }
            return file;

        }
        return null;
    }
    
    public static FileConfiguration loadConfig(Plugin plugin, File file) {
        FileConfiguration config = new YamlConfiguration();
        if (file != null && file.exists()) {

            try {
                config.load(file);
            } catch (FileNotFoundException e) {
                plugin.getLogger().info("Couldn't find config file for saved locations");
            }

            return config;
        }
        return null;
    }

    public static void loadShortcuts(Plugin plugin) {
        // load config
        File file = findFile(plugin);
        FileConfiguration config = loadConfig(plugin, file);

        // read in the locations
        System.out.println(" -" + config.getKeys(false));
        for (String name : config.getKeys(false)) {
            Location loc = config.getLocation(name);
            Locations.tele.set(name, loc);
        }
    }

    public static void saveShortcuts(Plugin plugin, Map<String, Location> locations) {
        // load config
        File file = findFile(plugin);
        FileConfiguration config = loadConfig(plugin, file);

        // save the locations
        for (String name : locations.keySet()) {
            Location loc = locations.get(name);

            try {
                // save to files
                config.set(name, loc);
                config.save(file);
            } catch (IOException e) {
                plugin.getLogger().info("Failed to save locations, IO error");
            }
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
