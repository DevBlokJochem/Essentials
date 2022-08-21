package nl.jochem.essentials.config

import com.google.gson.GsonBuilder
import java.io.File

private const val fileName = "extensions/essentials/settings.json"

object RegisterSettingsConfig {

    fun init() {
        if(!File(fileName).exists()) {
            File(fileName).createNewFile()
            File(fileName).writeText(
                GsonBuilder().setPrettyPrinting().create().toJson(Settings(
                    true, true, true, true, true))
            )
        }
    }

    fun reloadConfig() {
        settingsConfig = GsonBuilder()
            .setPrettyPrinting()
            .create()!!.fromJson(File(fileName).readText(), Settings::class.java)!!
    }

}

fun Settings.update() {
    File(fileName).writeText(
        GsonBuilder().setPrettyPrinting().create().toJson(this)
    )
    RegisterSettingsConfig.reloadConfig()
}

var settingsConfig = GsonBuilder()
    .setPrettyPrinting()
    .create()!!.fromJson(File(fileName).readText(), Settings::class.java)!!

data class Settings(
    val gamemode: Boolean,
    val invsee: Boolean,
    val clear: Boolean,
    val fly: Boolean,
    val vanish: Boolean,
)