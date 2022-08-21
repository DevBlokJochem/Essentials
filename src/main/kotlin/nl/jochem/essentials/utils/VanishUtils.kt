package nl.jochem.essentials.utils

import net.minestom.server.entity.Player
import nl.jochem.essentials.mechanics.VanishManager

fun Player.isVanish(): Boolean {
    return VanishManager.checkVanish(uuid)
}