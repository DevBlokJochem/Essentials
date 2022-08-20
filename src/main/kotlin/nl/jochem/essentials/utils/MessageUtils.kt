package nl.jochem.essentials.utils

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player
import nl.jochem.placeholderapi.api.PlaceholderAPI

fun CommandSender.msg(message: String) {

    if(this is Player) {
        sendMessage(PlaceholderAPI.translatePlaceholdersToString(this, message))
    }else{
        println(PlaceholderAPI.translatePlaceholdersToString(null, message))
    }

}