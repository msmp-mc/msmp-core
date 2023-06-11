package world.anhgelus.msmp.msmpcore

import world.anhgelus.msmp.msmpcore.event.MPlayerListener
import world.anhgelus.msmp.msmpcore.player.MPlayerManager
import world.anhgelus.msmp.msmpcore.utils.config.Config
import world.anhgelus.msmp.msmpcore.utils.config.ConfigAPI

class MSMPCore: PluginBase() {
    override val pluginName = "MSMPCore"
    override val configHelper = ConfigAPI

    private lateinit var playersConf: Config
    private lateinit var config: Config

    override fun enable() {
        LOGGER = logger
        INSTANCE = this

        config = Config(this, "config")
        playersConf = Config(this, "players")

        loadConfig()

        events.add(MPlayerListener)
    }

    private fun loadConfig() {
        MPlayerManager.setup(config.get().getInt("max-lives", 3))
        val section = playersConf.get().getConfigurationSection("mplayers") ?: return
        MPlayerManager.importPlayersFromConfig(section)
    }

    override fun disable() {
        disableConfig()
    }

    private fun disableConfig() {
        val section = playersConf.get().getConfigurationSection("mplayers") ?: playersConf.get().createSection("mplayers")
        MPlayerManager.savePlayersInConfig(section)
        playersConf.save()
    }

    companion object : CompanionBase()
}