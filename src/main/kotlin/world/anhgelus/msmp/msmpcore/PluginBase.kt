package world.anhgelus.msmp.msmpcore

import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

abstract class PluginBase: JavaPlugin() {
    abstract val pluginName: String

    abstract fun enable()

    abstract fun disable()

    override fun onEnable() {
        // init vars
        LOGGER = logger
        enable()
        LOGGER.info("$pluginName has been enabled!")
    }

    override fun onDisable() {
        disable()
        LOGGER.info("$pluginName has been disabled!")
    }

    open class CompanionBase {
        lateinit var INSTANCE: MSMPCore
            private set
        lateinit var LOGGER: Logger

        fun getInstance(): MSMPCore {
            return INSTANCE
        }
    }

    companion object : CompanionBase()
}