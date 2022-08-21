package nl.jochem.essentials

import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension
import nl.jochem.essentials.commands.*
import nl.jochem.essentials.config.RegisterMessagesConfig
import nl.jochem.essentials.config.RegisterSettingsConfig
import nl.jochem.essentials.config.settingsConfig
import java.io.File

class Main : Extension() {

    override fun initialize() {
        registerConfigs()
        registerCommands()
        println("Essentials is now enabled!")
    }

    override fun terminate() {

        println("Essentials is now disabled!")
    }

    private fun registerCommands() {
        val commandManager = MinecraftServer.getCommandManager()
        if(settingsConfig.gamemode) commandManager.register(GamemodeCommand())
        if(settingsConfig.invsee) commandManager.register(InvseeCommand())
        if(settingsConfig.clear) commandManager.register(ClearCommand())
        if(settingsConfig.fly) commandManager.register(FlyCommand())
        if(settingsConfig.vanish) commandManager.register(VanishCommand())
        if(settingsConfig.reload) commandManager.register(ReloadCommand())
    }

    private fun registerConfigs() {
        if(!File("extensions/essentials").exists()) { File("extensions/essentials").mkdirs() }
        RegisterMessagesConfig
        RegisterSettingsConfig.init()
    }

}