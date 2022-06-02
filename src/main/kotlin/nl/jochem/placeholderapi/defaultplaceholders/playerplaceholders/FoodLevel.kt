package nl.jochem.placeholderapi.defaultplaceholders.playerplaceholders

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import nl.jochem.placeholderapi.api.Placeholder

object FoodLevel : Placeholder {
    override fun getName(): String {
        return "food_level"
    }

    override fun getPlaceholderString(player: Player?): String {
        if(player != null) { return player.food.toString() }
        return "null"
    }
}