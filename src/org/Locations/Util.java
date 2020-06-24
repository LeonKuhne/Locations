
import java.util.Map;
import java.util.HashMap;


public class Util {

    private File file;
    private FileConfiguration config;
    
    public static Map<String, Command> loadShortcuts(String dir) {
        file = new File(dir, "leeslocs.yml");
        if (file.exists()) {
            try {
                config.load(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void saveShortcuts(String dir) {
        
        file = new File(dir, "leeslocs.yml");
        if (!file.exists()) {

            // create
            file.getParentFile().mkdirs();
            saveResource("custom.yml", false);

        } else {

            // update
            
        
        }
    }
    
}
