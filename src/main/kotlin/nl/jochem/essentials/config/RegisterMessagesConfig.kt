package nl.jochem.essentials.config

import com.google.gson.GsonBuilder
import nl.jochem.placeholderapi.configs.MessagesConfig
import java.io.File

private const val fileName = "extensions/essentials/messages.json"

object RegisterMessagesConfig {

    init {
        if(!File(fileName).exists()) {
            File(fileName).createNewFile()
            File(fileName).writeText(
                GsonBuilder().setPrettyPrinting().create().toJson(Messages(
                    "&cYou can only run this command as a player.",
                    "&cSorry, but you dont have the right permissions.",
                    "&cSorry, but you gave me an invalid value.",
                    "&7Your gamemode has been changed to %player_gamemode%.",
                    "&7You have changed the gamemode.",
                "&cYour target has lost connection."))
            )
        }
    }

}

class MessagesConfig() {

    fun getConfig(): Messages {
        return GsonBuilder()
            .setPrettyPrinting()
            .create()!!.fromJson(File(fileName).readText(), Messages::class.java)!!
    }

}

data class Messages(
    val only_player : String,
    val no_permission : String,
    val invalid_value : String,
    val gamemode_self : String,
    val gamemode_other : String,
    val invsee_target_disconnect : String,
)