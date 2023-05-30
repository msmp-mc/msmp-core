package world.anhgelus.msmp.msmpcore.utils

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import world.anhgelus.msmp.msmpcore.player.MPlayerManager
import java.util.*
import java.util.function.Consumer

object MessageParser {
    /**
     * Parser used to parse every message
     *
     * @param id The id of the parser (must be player_name for %player_name%)
     * @param replacer The replacer of the parser
     */
    data class Parser(val id: String, val replacer: String) {
        fun generateId(): String {
            return "%$id%"
        }
        fun replace(message: String): String {
            while (message.contains(generateId())) {
                message.replace(generateId(), replacer)
            }
            return message
        }
    }

    /**
     * Parse the message with the provided parsers
     *
     * @param message The message to parse
     * @param parsers The parsers to use
     * @return The parsed message
     */
    fun parse(message: String, parsers: List<Parser>): String {
        var msg = message
        parsers.forEach(Consumer { parser -> msg = parser.replace(message) })
        return msg
    }

    /**
     * Parse the death message and send it
     *
     * @param message The message to parse
     * @param event The event that triggered the death
     * @param section The section of the config to get the cause
     */
    fun parseDeathMessage(message: String, event: EntityDamageEvent, section: ConfigurationSection) {
        if (event.entity !is Player) return

        val player = event.entity as Player
        val mplayer = MPlayerManager.get(player)
        val causeMsg = section.getString(event.cause.toString().lowercase(Locale.getDefault()))!!
        val parsers = listOf(Parser("player_name", player.displayName), Parser("player_remaining_lives",
            mplayer.lives.remaining.toString()), Parser("player_max_lives", mplayer.lives.max.toString()),
            Parser("death_message", causeMsg))

        ChatHelper.sendError(parse(message, parsers))
    }
}