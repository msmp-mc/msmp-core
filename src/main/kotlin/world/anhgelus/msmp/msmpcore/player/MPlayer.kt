package world.anhgelus.msmp.msmpcore.player

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import world.anhgelus.msmp.msmpcore.MSMPCore
import world.anhgelus.msmp.msmpcore.utils.MessageParser
import world.anhgelus.msmp.msmpcore.utils.config.Config
import java.util.UUID

/**
 * Custom Player class to store information about the player
 *
 * @see MPlayerManager
 * @author Anhgelus Morhtuuzh
 * @param player the player
 * @param maxLives the max lives of the player
 * @param remainingLives the remaining lives of the player
 */
class MPlayer private constructor(val player: Player, maxLives: Int, remainingLives: Int) {

    /**
     * Representation of the lives of the player
     *
     * @param remaining the remaining lives of the player
     * @param max the max lives of the player
     */
    data class Lives(var remaining: Int, var max: Int) {
        /**
         * Check if the player could be alive
         * @return true if the player could be alive
         */
        fun couldBeAlive(): Boolean {
            return remaining > 0
        }
    }

    /**
     * Pure representation of the mplayer with every important information
     *
     * @param isImmortal if the player is immortal
     * @param remainingLives the remaining lives of the player
     * @param player the player
     */
    data class PureData(val isImmortal: Boolean, val remainingLives: Int, val player: UUID)

    val lives: Lives
    var isImmortal: Boolean = false
        private set
    var isOnline: Boolean = false
        private set

    init {
        this.lives = Lives(remainingLives, maxLives)
        updateOnlineStatus()
    }

    /**
     * Check if the player is alive
     */
    fun isAlive(): Boolean {
        if (isImmortal) return true
        return lives.couldBeAlive()
    }

    /**
     * Set the immortal status of the player
     */
    fun setImmortal(isImmortal: Boolean) {
        this.isImmortal = isImmortal
    }

    /**
     * Should be called when the player died
     *
     * @param event the EntityDamageEvent
     */
    fun died(event: EntityDamageEvent) {
        if (isImmortal) {
            (event.entity as Player).health = 20.0
        } else {
            lives.remaining--
        }
        val section = Config(MSMPCore.INSTANCE, "death").get()
        val message = section.getString("base")!!
        MessageParser.parseDeathMessage(message, event, section.getConfigurationSection("causes")!!)
    }

    /**
     * Update the online status of the player
     *
     * Should be used when the player join or leave the server
     *
     * Automatically called when a new instance of MPlayer is created
     */
    fun updateOnlineStatus() {
        val bPlayer = Bukkit.getPlayer(player.uniqueId)
        if (bPlayer != null) {
            isOnline = bPlayer.isOnline
        } else {
            isOnline = false
        }
        MSMPCore.LOGGER.info("Player ${player.name}'s online status has been updated: $isOnline")
    }

    /**
     * Get the pure data of the player
     *
     * Should be used for saving data in config files
     *
     * @return the pure data of the player
     */
    fun toPureData(): PureData {
        return PureData(isImmortal, lives.remaining, player.uniqueId)
    }

    companion object {
        /**
         * If you want to get a MPlayer instance, do not use this to get a MPlayer instance!
         *
         * Create a new MPlayer instance with max lives.
         *
         * @see MPlayerManager.get
         */
        fun fromPlayer(player: Player): MPlayer {
            return MPlayer(player, MPlayerManager.maxLives, MPlayerManager.maxLives)
        }

        /**
         * If you want to get a MPlayer instance, do not use this to get a MPlayer instance!
         *
         * Create a new MPlayer instance from pure data.
         *
         * @see MPlayerManager.get
         */
        fun fromPureData(pure: PureData): MPlayer {
            return MPlayer(Bukkit.getPlayer(pure.player)!!, MPlayerManager.maxLives, pure.remainingLives).also {
                it.isImmortal = pure.isImmortal
            }
        }
    }
}