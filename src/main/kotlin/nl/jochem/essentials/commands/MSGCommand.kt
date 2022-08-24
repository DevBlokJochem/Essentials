package nl.jochem.essentials.commands

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentStringArray
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.utils.entity.EntityFinder
import nl.jochem.essentials.config.messagesConfig
import nl.jochem.essentials.mechanics.MSGManager
import nl.jochem.essentials.utils.msg

class MSGCommand : Command("msg", "m") {

    private fun usage(sender: CommandSender, context: CommandContext) {
        sender.msg("§cUsage: /msg <player> <message>")
    }

    private fun executeOnSend(sender: CommandSender, context: CommandContext, message: ArgumentStringArray) {
        if (!sender.hasPermission("essentials.msg")) {
            sender.msg(messagesConfig.no_permission)
            return
        }

        var msg = ""
        context[message].forEach {
            msg += "$it "
        }

        val targetFinder = context.get<EntityFinder>("player")
        val target = targetFinder.findFirstPlayer(sender)
        if(target == null) {
            sender.msg(messagesConfig.invalid_value)
            return
        }

        MSGManager.sendMessage(sender, target, msg)
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

        val message = ArgumentType.StringArray("message")

        setArgumentCallback({ sender: CommandSender, exception: ArgumentSyntaxException ->
            targetCallback(
                sender,
                exception
            )
        }, player)

        addSyntax({ sender: CommandSender, context: CommandContext -> executeOnSend(sender, context, message) }, player, message)
    }
}