package world.anhgelus.msmp.msmpcore.player

import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity
import net.minecraft.server.network.PlayerConnection
import net.minecraft.world.entity.decoration.EntityArmorStand
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftArmorStand
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Event
import world.anhgelus.msmp.msmpcore.MSMPCore
import java.util.*

object MPlayerManager {
    private val players = mutableMapOf<Player, MPlayer>()
    private val unloadPlayers = mutableMapOf<UUID, MPlayer.PureData>()

    var maxLives: Int = 0
        private set

    /**
     * Set up the MPlayerManager
     * @param maxLives the max lives of the player
     */
    fun setup(maxLives: Int) {
        this.maxLives = maxLives
    }

    /**
     * Get the player
     *
     * @return the player from previous stored information or create a new one
     */
    fun get(player: Player): MPlayer {
        return players[player] ?: MPlayer.fromPlayer(player).also { players[player] = it }
    }

    /**
     * Update the status of the MPlayer when a player join the server
     * Also do updateOnlineStatus()
     *
     * @return the MPlayer of the player
     */
    fun connectionOfPlayer(player: Player): MPlayer {
        MSMPCore.LOGGER.info("Player ${player.name} has joined the server")
        val pure =  unloadPlayers[player.uniqueId] ?: return get(player).also { it.updateOnlineStatus() }
        return MPlayer.fromPureData(pure)
    }

    fun nameTagVisibility(player: Player, e: Event) {
        if(e.eventName.equals("PlayerQuitEvent")) return
        val stands: MutableMap<UUID, EntityArmorStand> = mutableMapOf()

        val stand: ArmorStand = player.world.spawnEntity(player.location, EntityType.ARMOR_STAND) as ArmorStand
        stand.isInvulnerable = true
        stand.isVisible = false
        stand.isSmall = true
        stand.isMarker = true
        stand.isCustomNameVisible = false

        val standNSM = (stand as CraftArmorStand).handle

        player.addPassenger(stand)

        val packet = PacketPlayOutSpawnEntity(standNSM)
        stands[player.uniqueId] = standNSM
        val connection: PlayerConnection = (player as CraftPlayer).handle.b
        connection.a(packet)
    }

    /**
     * Import the players from the config
     */
    fun importPlayersFromConfig(section: ConfigurationSection) {
        val keys = section.getKeys(false)
        keys.forEach {
            val mp: MPlayer.PureData? = MPlayer.PureData.fromString(section.getString(it)!!)
            if (mp == null) {
                MSMPCore.LOGGER.warning("Player $it has invalid data in config")
                return@forEach
            }
            val p = Bukkit.getPlayer(mp.player)
            if (p == null) {
                unloadPlayers[mp.player] = mp
                return@forEach
            }
            players[p] = MPlayer.fromPureData(mp)
        }
    }

    /**
     * Save the players to the config
     */
    fun savePlayersInConfig(section: ConfigurationSection) {
        players.forEach { (p, it) ->
            MSMPCore.LOGGER.info("Saving player ${p.name} in config")
            section.set(p.uniqueId.toString(), it.toPureData().toString())
        }
    }
}