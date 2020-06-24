
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import java.lang.reflect.Field;

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
    
    public static Map<String, Command> loadShortcuts(Plugin plugin) {
        FileConfiguration config = new YamlConfiguration();
        File file = findOrCreateConfig(plugin);
        
        if (file.exists()) {
            try {
                config.load(file);
                
                // read in the commands
                Sytem.out.println(" -" + config);
                Map shortcuts = new HashMap() {{
                    for (String name : config.getKeys()) {
                        registerTeleport(plugin, name);
                        Location loc = null; // get this from they config value pair
                        tele.set(name, loc);
                    }
                }};

                return shortcuts;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void saveShortcuts(Plugin plugin) {
        // save

    }

    public static void registerTeleport(Plugin plugin, String string) throws Exception {
        TeleportCommand tpCommand = new TeleportCommand(string, tele);
        getCommandMap(plugin).register(string, tpCommand);
        shortcuts.put(string, tpCommand);
    }

    public static void unregisterTeleport(Plugin plugin, String string) throws Exception {
        Command tpCommand = shortcuts.remove(string);
        tpCommand.unregister(getCommandMap(plugin));
    }

    private static CommandMap getCommandMap(Plugin plugin) throws Exception {
        Server server = plugin.getServer();
        Field bukkitCmdMap = server.getClass().getDeclaredField("commandMap");
        bukkitCmdMap.setAccessible(true);
        return (CommandMap) bukkitCmdMap.get(server);
    }
}
