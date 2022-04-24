package com.survival.core.commands

import com.survival.core.config.messagesConfig
import com.survival.core.utils.msg
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.entity.Player
import net.minestom.server.utils.entity.EntityFinder

class GameRuleCommand : Command("gamerule") {
    private fun usage(sender: CommandSender, context: CommandContext) {
        sender.msg("§cUsage: /gamerule <gamerule> true/false")
    }

    private fun executeOnRule(
        sender: CommandSender,
        context: CommandContext,
    ) {
        if (!sender.isPlayer) {
            sender.sendMessage(messagesConfig.only_player)
            return
        }
        val player = sender as Player
        if(!sender.hasPermission("survivalcore_fly_self")) {
            return sender.msg(messagesConfig.no_permission)
        }

        if(player.isAllowFlying) {
            player.isAllowFlying = false
            player.msg(messagesConfig.fly_self_off)
        }else {
            player.isAllowFlying = true
            player.msg(messagesConfig.fly_self_on)
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

        addSyntax({ sender: CommandSender, context: CommandContext -> executeOnRule(sender, context) })
    }
}