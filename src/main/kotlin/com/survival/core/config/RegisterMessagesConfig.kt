package com.survival.core.config

import com.google.gson.GsonBuilder
import java.io.File

object RegisterMessagesConfig {

    init {
        val name = "configs/messages.json"

        if(!File(name).exists()) {
            File(name).createNewFile()
            File(name).writeText(
                GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(MessagesConfig(
                        prefix = "<blue>[Core]</blue> ",
                        only_player = "<red>Sorry, but you can only run this command as a player.",
                        no_permission = "<red>Sorry, but you don't have the right permissions.",
                        invalid_value = "<red>Sorry, but this isn't a valid value",
                        gamemode_self = "<gray>Your gamemode has changed to <gold><bold>%player_gamemode%</bold></gold>.",
                        gamemode_other = "<gray>You have changed the gamemode of <gold><bold>%target_name%</bold></gold> to <gold><bold>%target_gamemode%</bold></gold>.",
                        fly_self_on = "<gray>You <green>can</green> fly now.",
                        fly_self_off = "<gray>You <red>can't</red> fly anymore.",
                        fly_other = "<gray>You have toggled the fly powers of <gold><bold>%target_name%</gold></bold>.",
                        ))
            )
        }
    }

}

val messagesConfig = GsonBuilder()
    .setPrettyPrinting()
    .create()!!.fromJson(File("configs/messages.json").readText(), MessagesConfig::class.java)!!

data class MessagesConfig(
    //prefix
    val prefix : String,
    //error messages
    val only_player : String,
    val no_permission : String,
    val invalid_value : String,
    //command messages
    val gamemode_self : String,
    val gamemode_other : String,
    val fly_self_on : String,
    val fly_self_off : String,
    val fly_other : String,
)