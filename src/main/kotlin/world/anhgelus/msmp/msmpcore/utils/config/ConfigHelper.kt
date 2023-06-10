package world.anhgelus.msmp.msmpcore.utils.config

import world.anhgelus.msmp.msmpcore.PluginBase

/**
 * Helper for configs (getting them)
 */
open class ConfigHelper {
    private lateinit var plugin: PluginBase

    /**
     * Init the config helper
     *
     * Already called by the plugin base
     *
     * @param plugin The plugin
     */
    fun init(plugin: PluginBase) {
        this.plugin = plugin
    }

    /**
     * Get a config
     *
     * @param name The name of the config
     * @return The config
     */
    fun getConfig(name: String): Config {
        return Config(plugin, name)
    }
}