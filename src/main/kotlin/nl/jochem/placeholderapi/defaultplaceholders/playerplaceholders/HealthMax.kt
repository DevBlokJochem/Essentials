package nl.jochem.placeholderapi.defaultplaceholders.playerplaceholders

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import nl.jochem.placeholderapi.api.Placeholder

object HealthMax : Placeholder {
    override fun getName(): String {
        return "health_max"
    }

    override fun getPlaceholderString(player: Player?): String {
        if(player != null) { return player.maxHealth.toString() }
        return "null"
    }

    override fun getPlaceholderComponent(player: Player?): Component {
        return Component.text(getPlaceholderString(player))
    }
}