package world.anhgelus.msmp.msmpcore.event

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import world.anhgelus.msmp.msmpcore.player.MPlayerManager

class MPlayerListener: Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        MPlayerManager.connectionOfPlayer(e.player)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerQuit(e: PlayerQuitEvent) {
        MPlayerManager.get(e.player).updateOnlineStatus()
    }

    @EventHandler
    fun onPlayerDeath(e: EntityDamageEvent) {
        if (e.entity !is Player) return
        if (e.finalDamage <= 0) return
        MPlayerManager.get(e.entity as Player).died(e)
    }
}