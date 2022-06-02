package nl.jochem.placeholderapi.defaultplaceholders.playerplaceholders

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import nl.jochem.placeholderapi.api.Placeholder

object XpLevel : Placeholder {
    override fun getName(): String {
        return "xp_level"
    }

    override fun getPlaceholderString(player: Player?): String {
        if(player != null) { return player.level.toString() }
        return "null"
    }
}