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
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import java.lang.reflect.Field;
import java.io.IOException;
import java.io.FileNotFoundException;
import org.bukkit.World;

public class Util {

    // 
    // CONFIG
    //

    // find or create
    private static File findFile(Plugin plugin, String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(name, false);
        }
        return file;
    }
    
    public static FileConfiguration loadConfig(Plugin plugin, File file) {
        FileConfiguration config = new YamlConfiguration();
        if (file != null && file.exists()) {

            try {
                config.load(file);
            } catch (InvalidConfigurationException e) {
                plugin.getLogger().info("Couldn't read config file, syntax error? " + e.getMessage());
            } catch (IOException e) {
                plugin.getLogger().info("Couldn't find config file");
            }

            return config;
        }
        return null;
    }

    // 
    // WORLDS
    //
    
    public static Map<World, WorldLocations> loadWorlds(Plugin plugin) {
        Map<World, WorldLocations> worlds = new HashMap();
        
        // load config
        File file = findFile(plugin, "leeswords.yml");
        FileConfiguration config = loadConfig(plugin, file);

        // read in worlds
        for (String worldName : config.getKeys(false)) {
            WorldLocations worldLocs = config.getObject(worldName, WorldLocations.class);
            World world = plugin.getServer().getWorld(worldName);
            worlds.put(world, worldLocs);
        }

        return worlds;
    }

    public static void saveWorlds(Plugin plugin, Map<World, WorldLocations> worlds) {
        // load config
        File file = findFile(plugin, "leeswords.yml");
        FileConfiguration config = loadConfig(plugin, file);

        // add to config
        for (World world : worlds) {
            String worldName = world.getName();
            WorldLocations worldLocs = worlds.get(world);
            config.set(worldName, worldLocs);
        }

        // save
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().info("Failed to save worlds, IO error");
        }
    }

    //
    // SHORTCUTS
    //

    public static Map<String, Location> loadShortcuts(Plugin plugin) {
        Map<String, Location> locations = new HashMap();
        
        // load config
        File file = findFile(plugin, "leeslocs.yml");
        FileConfiguration config = loadConfig(plugin, file);

        // read in the locations
        for (String name : config.getKeys(false)) {
            Location loc = config.getLocation(name);
            locations.put(name, loc);
        }

        return locations;
    }

    public static void saveShortcuts(Plugin plugin, Map<String, Location> locations) {
        // load config
        File file = findFile(plugin, "leeslocs.yml");
        FileConfiguration config = loadConfig(plugin, file);

        // add to config
        for (String name : locations.keySet()) {
            Location loc = locations.get(name);
            config.set(name, loc);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().info("Failed to save locations, IO error");
        }
    }

    //
    // COMMAND REGISTRY
    //

    public static Map<String, Command> createShortcuts(Plugin plugin, Map<String, Location> locations) {
        Map<String, Command> shortcuts = new HashMap();
        for (String name : locations.keySet()) {
            try {
                Command cmd = registerTeleport(plugin, name);
                shortcuts.put(name, cmd);
            } catch (Exception e) {
                plugin.getLogger().info("failed to load command " + name);
            }
        }
        return shortcuts;
    }

    public static Command registerTeleport(Plugin plugin, String string) throws Exception {
        TeleportCommand tpCommand = new TeleportCommand(string);
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
