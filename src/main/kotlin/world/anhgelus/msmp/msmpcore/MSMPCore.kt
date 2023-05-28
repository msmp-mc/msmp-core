package world.anhgelus.msmp.msmpcore

import org.bukkit.Bukkit
import world.anhgelus.msmp.msmpcore.event.MPlayerListener
import world.anhgelus.msmp.msmpcore.player.MPlayerManager
import world.anhgelus.msmp.msmpcore.utils.config.Config

class MSMPCore: PluginBase() {
    override val pluginName: String = "MSMPCore"

    lateinit var playersConf: Config

    override fun enable() {
        MPlayerManager.setup(0)
        playersConf = Config(this, "players")

        enableConfig()
        loadEvents()
    }

    private fun enableConfig() {
        val section = playersConf.get().getConfigurationSection("mplayers")!!
        MPlayerManager.importPlayersFromConfig(section)
    }

    private fun loadEvents() {
        val manager = Bukkit.getPluginManager()
        manager.registerEvents(MPlayerListener(), this)
    }

    override fun disable() {
        disableConfig()
    }

    private fun disableConfig() {
        val section = playersConf.get().getConfigurationSection("mplayers")!!
        MPlayerManager.savePlayersInConfig(section)
    }

    companion object : CompanionBase()
}