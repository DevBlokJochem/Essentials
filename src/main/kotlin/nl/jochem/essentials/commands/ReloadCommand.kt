package nl.jochem.essentials.commands

import com.google.gson.GsonBuilder
import net.minestom.server.MinecraftServer
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import nl.jochem.essentials.config.*
import nl.jochem.essentials.utils.msg
import nl.jochem.placeholderapi.api.PlaceholderAPI
import java.io.File

class ReloadCommand : Command("reload") {

    private fun usage(sender: CommandSender, context: CommandContext) {
        sender.msg("§cUsage: /reload")
    }

    private fun executeOnSelf(
        sender: CommandSender
    ) {
        if(!sender.hasPermission("essentials.reload")) {
            return sender.msg(messagesConfig.no_permission)
        }

        //check for other extensions

        if(MinecraftServer.getExtensionManager().hasExtension("PlaceholderAPI")) {
            PlaceholderAPI.reload()
        }

        //essentials

        messagesConfig = GsonBuilder()
            .setPrettyPrinting()
            .create()!!.fromJson(File(messagesConfig.getFilename()).readText(), Messages::class.java)!!
        settingsConfig = GsonBuilder()
            .setPrettyPrinting()
            .create()!!.fromJson(File(settingsConfig.getFilename()).readText(), Settings::class.java)!!

        sender.msg(messagesConfig.reload)
    }

    init {
        setDefaultExecutor { sender: CommandSender, context: CommandContext ->
            usage(
                sender,
                context
            )
        }

        addSyntax({ sender: CommandSender, _ -> executeOnSelf(sender) })
    }
}