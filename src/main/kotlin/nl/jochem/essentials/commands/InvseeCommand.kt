package nl.jochem.essentials.commands

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.entity.Player
import net.minestom.server.utils.entity.EntityFinder
import nl.jochem.essentials.config.MessagesConfig
import nl.jochem.essentials.mechanics.InvseeHandler
import nl.jochem.essentials.utils.msg

class InvseeCommand : Command("invsee") {
    private fun usage(sender: CommandSender, context: CommandContext) {
        sender.msg("§cUsage: /invsee [player]")
    }

    private val messagesConfig = MessagesConfig().getConfig()

    private fun executeOnPlayer(
        sender: CommandSender,
        context: CommandContext,
    ) {
        if (sender !is Player) {
            sender.sendMessage(messagesConfig.only_player)
            return
        }

        val targetFinder = context.get<EntityFinder>("player")
        val target = targetFinder.findFirstPlayer(sender)
        if(target == null) {
            sender.msg(messagesConfig.invalid_value)
            return
        }
        if(!sender.hasPermission("essentials.invsee.view")) {
            return sender.msg(messagesConfig.no_permission)
        }

        InvseeHandler(sender as Player, target)

    }

    private fun targetCallback(sender: CommandSender, exception: ArgumentSyntaxException) {
        sender.msg(messagesConfig.invalid_value)
    }

    init {
        setDefaultExecutor { sender: CommandSender, context: CommandContext ->
            usage(
                sender,
                context
            )
        }
        val player = ArgumentType.Entity("player")
            .onlyPlayers(true)
            .singleEntity(true)


        setArgumentCallback({ sender: CommandSender, exception: ArgumentSyntaxException ->
            targetCallback(
                sender,
                exception
            )
        }, player)

        addSyntax({ sender: CommandSender, context: CommandContext -> executeOnPlayer(sender, context) }, player)
    }
}