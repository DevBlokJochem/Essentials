package nl.jochem.essentials.commands

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.entity.Player
import net.minestom.server.utils.entity.EntityFinder
import nl.jochem.essentials.config.messagesConfig
import nl.jochem.essentials.utils.msg

class FlyCommand : Command("fly") {

    private fun usage(sender: CommandSender, context: CommandContext) {
        sender.msg("§cUsage: /fly [player]")
    }

    private fun executeOnSelf(sender: CommandSender) {
        if(sender !is Player) {
            sender.msg(messagesConfig.only_player)
            return
        }
        if(!sender.hasPermission("essentials.fly.self")) {
            sender.msg(messagesConfig.no_permission)
            return
        }
        if(sender.isAllowFlying) {
            sender.isAllowFlying = false
            sender.isFlying = false
            sender.msg(messagesConfig.fly_self_off)
        }else {
            sender.isAllowFlying = true
            sender.isFlying = true
            sender.msg(messagesConfig.fly_self_on)
        }
    }

    private fun executeOnOther(sender: CommandSender, context: CommandContext) {
        if(sender !is Player) {
            sender.msg(messagesConfig.only_player)
            return
        }
        if(sender.hasPermission("essentials.fly.other")) {
            sender.msg(messagesConfig.no_permission)
            return
        }
        val targetFinder = context.get<EntityFinder>("player")
        val target = targetFinder.findFirstPlayer(sender)
        if(target == null) {
            sender.msg(messagesConfig.invalid_value)
            return
        }
        if(target.isAllowFlying) {
            target.isAllowFlying = false
            target.isFlying = false
            target.msg(messagesConfig.fly_self_off)
            sender.msg(messagesConfig.fly_other_off)
        }else {
            target.isAllowFlying = true
            target.isFlying = true
            target.msg(messagesConfig.fly_self_on)
            sender.msg(messagesConfig.fly_other_on)
        }
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

        addSyntax({ sender: CommandSender, _ -> executeOnSelf(sender)})
        addSyntax({ sender: CommandSender, context: CommandContext -> executeOnOther(sender, context) }, player)
    }

}