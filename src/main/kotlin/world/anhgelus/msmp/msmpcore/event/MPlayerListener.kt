package world.anhgelus.msmp.msmpcore.event

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import world.anhgelus.msmp.msmpcore.player.MPlayerManager

object MPlayerListener: Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        MPlayerManager.connectionOfPlayer(e.player)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerQuit(e: PlayerQuitEvent) {
        MPlayerManager.get(e.player).updateOnlineStatus(false)
    }
}