package world.anhgelus.msmp.msmpcore.utils.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * @author Robotv2, Anhgelus Morhtuuzh
 * @see world.anhgelus.msmp.msmpcore.utils.config.ConfigHelper
 */
public class Config {

    private final Plugin main;
    private final String name;

    private final File database;
    private FileConfiguration databaseConfig = null;

    /**
     * Internal use only!
     *
     * @param main Plugin's main file
     * @param name Name of the configuration
     * @see world.anhgelus.msmp.msmpcore.utils.config.ConfigHelper
     */
    public Config(Plugin main, String name) {
        this.main = main;
        this.name = name;

        database = new File(main.getDataFolder(), name + ".yml");
        if (!database.exists()) {
            if (database.getParentFile().exists()) database.getParentFile().mkdir();
            main.saveResource(name + ".yml", false);
        }
    }

    /**
     * Get the configuration
     * @return The configuration
     */
    public FileConfiguration get() {
        if (databaseConfig == null) reload();
        return databaseConfig;
    }

    /**
     * Save the configuration
     */
    public void save() {
        try {
            get().save(database);
        } catch (IOException e) {
            main.getLogger().log(Level.SEVERE, "Error while saving the configuration " + name );
            e.printStackTrace();
        }
    }

    /**
     * Reload the configuration
     */
    public void reload() {
        databaseConfig = YamlConfiguration.loadConfiguration(database);

        final InputStream defaultStream = main.getResource(name + ".yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            databaseConfig.setDefaults(defaultConfig);
        }
    }
}