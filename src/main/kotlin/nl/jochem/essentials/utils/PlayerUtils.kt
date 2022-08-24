package nl.jochem.essentials.utils

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import java.util.*

fun UUID.getPlayer() : Player? {
    return MinecraftServer.getConnectionManager().getPlayer(this)
}