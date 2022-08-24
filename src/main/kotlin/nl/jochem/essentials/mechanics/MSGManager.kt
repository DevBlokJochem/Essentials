package nl.jochem.essentials.mechanics

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player

object MSGManager {

    fun sendMessage(messager: CommandSender, reciever: Player, message: String) {
        if(messager !is Player) {

            reciever.sendMessage("§cConsole §7-> you: $message")
        }
        val sender = messager as Player
        reciever.sendMessage("§7${sender.username} -> you: $message")
        sender.sendMessage("§7${sender.username} -> ${reciever.username}: $message")
    }

}