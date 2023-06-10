package world.anhgelus.msmp.msmpcore

import org.bukkit.plugin.java.JavaPlugin
import world.anhgelus.msmp.msmpcore.utils.config.ConfigHelper
import java.util.logging.Logger

abstract class PluginBase: JavaPlugin() {
    abstract val pluginName: String
    abstract val configHelper: ConfigHelper

    abstract fun enable()

    abstract fun disable()

    override fun onEnable() {
        INSTANCE = this
        LOGGER = logger
        configHelper.init(this)

        enable()
        LOGGER.info("$pluginName has been enabled!")
    }

    override fun onDisable() {
        disable()
        LOGGER.info("$pluginName is being disabled!")
    }

    open class CompanionBase {
        lateinit var INSTANCE: PluginBase
        lateinit var LOGGER: Logger
    }

    companion object : CompanionBase()
}