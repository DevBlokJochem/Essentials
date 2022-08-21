package nl.jochem.essentials.commands

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.entity.Player
import net.minestom.server.utils.entity.EntityFinder
import nl.jochem.essentials.config.MessagesConfig
import nl.jochem.essentials.utils.clearInv
import nl.jochem.essentials.utils.msg

class ClearCommand : Command("clear") {
    private fun usage(sender: CommandSender, context: CommandContext) {
        sender.msg("§cUsage: /clear [player]")
    }

    private val messagesConfig = MessagesConfig().getConfig()

    private fun executeOnSelf(
        sender: CommandSender
    ) {
        if (sender !is Player) {
            sender.sendMessage(messagesConfig.only_player)
            return
        }
        val player = sender as Player
        if(!sender.hasPermission("essentials.clear.self")) {
            return sender.msg(messagesConfig.no_permission)
        }

        player.clearInv()
        sender.msg(messagesConfig.clear_self)
    }

    private fun executeOnOther(
        sender: CommandSender,
        context: CommandContext
    ) {
        val targetFinder = context.get<EntityFinder>("player")
        val target = targetFinder.findFirstPlayer(sender) ?: return sender.msg(messagesConfig.invalid_value)
        if(!sender.hasPermission("essentials.clear.other")) {
            return sender.msg(messagesConfig.no_permission)
        }

        target.clearInv()

        sender.msg(messagesConfig.clear_other)
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

        addSyntax({ sender: CommandSender, _ -> executeOnSelf(sender) })
        addSyntax({ sender: CommandSender, context: CommandContext -> executeOnOther(sender, context) }, player)
    }
}