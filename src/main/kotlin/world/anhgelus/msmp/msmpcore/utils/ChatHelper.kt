package world.anhgelus.msmp.msmpcore.utils

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ChatHelper {
    val success = ChatColor.GREEN
    val warning = ChatColor.YELLOW
    val error = ChatColor.RED
    val fatal = ChatColor.DARK_RED
    val info = ChatColor.GRAY

    fun sendToPlayer(player: Player, message: String) {
        player.sendMessage(message)
    }

    fun sendSuccessToPlayer(player: Player,message: String) {
        sendToPlayer(player,"$success $message")
    }

    fun sendWarningToPlayer(player: Player,message: String) {
        sendToPlayer(player,"$warning $message")
    }

    fun sendErrorToPlayer(player: Player,message: String) {
        sendToPlayer(player,"$error $message")
    }

    fun sendFatalToPlayer(player: Player,message: String) {
        sendToPlayer(player,"$fatal $message")
    }

    fun sendInfoToPlayer(player: Player,message: String) {
        sendToPlayer(player,"$info $message")
    }

    fun send(message: String) {
        Bukkit.broadcastMessage(message)
    }

    fun sendSuccess(message: String) {
        send("$success $message")
    }

    fun sendWarning(message: String) {
        send("$warning $message")
    }

    fun sendError(message: String) {
        send("$error $message")
    }

    fun sendFatal(message: String) {
        send("$fatal $message")
    }

    fun sendInfo(message: String) {
        send("$info $message")
    }
}