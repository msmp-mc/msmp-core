package world.anhgelus.msmp.msmpcore.player

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import world.anhgelus.msmp.msmpcore.MSMPCore
import world.anhgelus.msmp.msmpcore.utils.ChatHelper
import world.anhgelus.msmp.msmpcore.utils.MessageParser
import world.anhgelus.msmp.msmpcore.utils.config.ConfigAPI
import java.util.*

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
    data class PureData(val isImmortal: Boolean, val remainingLives: Int, val player: UUID) {
        override fun toString(): String {
            return "PureData(isImmortal=$isImmortal;remainingLives=$remainingLives;player=$player)"
        }

        companion object {
            fun fromString(str: String): PureData? {
                if (!str.startsWith("PureData(") && !str.endsWith(")")) return null
                val data = str.substring(9, str.length - 1).split(";")
                if (data.size != 3) return null
                try {
                    val isImmortal = data[0].substring(11).toBoolean()
                    val remainingLives = data[1].substring(15).toInt()
                    val player = UUID.fromString(data[2].substring(7))
                    return PureData(isImmortal, remainingLives, player)
                } catch (e: Exception) {
                    return null
                }
            }
        }
    }

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
        player.isInvulnerable = false
        this.isImmortal = isImmortal
    }

    /**
     * Should be called when the player died
     *
     * @param event the EntityDamageEvent
     * @param onDeath the function to call when the player died and has no more lives
     */
    fun died(event: EntityDamageEvent, onDeath: (MPlayer, EntityDamageEvent) -> Unit) {
        if (isImmortal) {
            (event.entity as Player).health = (event.entity as Player).getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
        } else {
            lives.remaining--
        }
        val section = ConfigAPI.getConfig("messages/death").get()
        val message = section.getString("base")!!
        MessageParser.parseDeathMessage(message, event, section.getConfigurationSection("causes")!!)
        if (lives.remaining == 0) onDeath(this, event)
    }

    /**
     * Should be called when the player gain a new life
     *
     * @param newLife the function to call when the player gain a new life after being dead
     */
    fun gainANewLife(newLife: (MPlayer) -> Unit) {
        if (isImmortal) {
            return
        }
        val alive = isAlive()
        lives.remaining++
        if (!alive) {
            newLife(this)
        }
    }

    /**
     * Update the online status of the player
     *
     * Should be used when the player join or leave the server
     *
     * @param isOnline the online status of the player
     */
    fun updateOnlineStatus(isOnline: Boolean) {
        val bPlayer = Bukkit.getPlayer(player.uniqueId)
        if (bPlayer != null) {
            this.isOnline = isOnline
        } else {
            this.isOnline = false
        }
        MSMPCore.LOGGER.info("Player ${player.name}'s online status has been updated: $isOnline")
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
        isOnline = bPlayer?.isOnline ?: false
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

    /**
     * Set the player's condition to default
     */
    fun toDefaultCondition() {
        toDefaultCondition(this)
    }

    companion object {
        /**
         * Internal use only!
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
         * Internal use only!
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

        /**
         * Set the player's condition to default
         *
         * @param player the player
         */
        fun toDefaultCondition(player: Player) {
            if(player.isOp){
                ChatHelper.sendInfoToPlayer(player, "As administrator you gamemode hasn't been changed")
            } else {
                player.gameMode = GameMode.SURVIVAL
            }
            player.isInvulnerable = false
            player.canPickupItems = true
            player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            player.foodLevel = 20
        }

        /**
         * Set the player's condition to default
         *
         * @param player the player
         */
        fun toDefaultCondition(player: MPlayer) {
            player.isImmortal = false
            player.lives.remaining = MPlayerManager.maxLives
            toDefaultCondition(player.player)
        }
    }
}