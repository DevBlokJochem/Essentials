package nl.jochem.essentials.commands

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.entity.Player
import nl.jochem.essentials.config.MessagesConfig
import nl.jochem.essentials.mechanics.VanishManager
import nl.jochem.essentials.utils.isVanish
import nl.jochem.essentials.utils.msg

class VanishCommand : Command("vanish", "v") {

    private fun usage(sender: CommandSender, context: CommandContext) {
        sender.msg("§cUsage: /vanish")
    }

    private val messagesConfig = MessagesConfig().getConfig()

    private fun executeOnSelf(
        sender: CommandSender
    ) {
        if (sender !is Player) {
            sender.sendMessage(messagesConfig.only_player)
            return
        }
        if(!sender.hasPermission("essentials.vanish")) {
            return sender.msg(messagesConfig.no_permission)
        }
        if(sender.isVanish()) {
            VanishManager.removePlayer(sender)
        }else{
            VanishManager.addPlayer(sender)
        }
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