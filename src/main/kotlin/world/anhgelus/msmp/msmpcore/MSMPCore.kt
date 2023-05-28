package world.anhgelus.msmp.msmpcore

import org.bukkit.Bukkit
import world.anhgelus.msmp.msmpcore.event.MPlayerListener
import world.anhgelus.msmp.msmpcore.player.MPlayerManager
import world.anhgelus.msmp.msmpcore.utils.config.Config

class MSMPCore: PluginBase() {
    override val pluginName: String = "MSMPCore"

    lateinit var playersConf: Config
    lateinit var config: Config

    override fun enable() {
        config = Config(this, "config")
        playersConf = Config(this, "players")

        loadConfig()
        loadEvents()
    }

    private fun loadConfig() {
        MPlayerManager.setup(config.get().getInt("max-lives", 3))
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