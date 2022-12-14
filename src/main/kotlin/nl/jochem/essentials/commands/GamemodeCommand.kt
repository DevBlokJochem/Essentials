package nl.jochem.essentials.commands

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentString
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import net.minestom.server.command.builder.suggestion.SuggestionEntry
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.utils.entity.EntityFinder
import nl.jochem.essentials.config.messagesConfig
import nl.jochem.essentials.utils.msg
class GamemodeCommand : Command("gamemode", "gm") {
    private fun usage(sender: CommandSender, context: CommandContext) {
        sender.msg("§cUsage: /gamemode <gamemode> [player]")
    }

    private fun executeOnSelf(
        sender: CommandSender,
        context: CommandContext,
        gameMode: ArgumentString,
    ) {
        if (sender !is Player) {
            sender.sendMessage(messagesConfig.only_player)
            return
        }
        if(!sender.hasPermission("essentials.gamemode.self")) {
            return sender.msg(messagesConfig.no_permission)
        }
        var gamemode = replaceGamemodes(context[gameMode])
        if(gamemode == null) {
            sender.msg(messagesConfig.invalid_value)
            return
        }
        sender.gameMode = gamemode

        sender.msg(messagesConfig.gamemode_self)
    }

    private fun executeOnOther(
        sender: CommandSender,
        context: CommandContext,
        gameMode: ArgumentString,
    ) {
        var gamemode = replaceGamemodes(context[gameMode])
        if(gamemode == null) {
            sender.msg(messagesConfig.invalid_value)
            return
        }
        val targetFinder = context.get<EntityFinder>("player")
        val target = targetFinder.findFirstPlayer(sender)
        if(target == null || gamemode == null) {
            sender.msg(messagesConfig.invalid_value)
            return
        }
        if(!sender.hasPermission("essentials.gamemode.other")) {
            return sender.msg(messagesConfig.no_permission)
        }
        target.gameMode = gamemode

        sender.msg(messagesConfig.gamemode_other)
        target.msg(messagesConfig.gamemode_self)
    }

    private fun targetCallback(sender: CommandSender, exception: ArgumentSyntaxException) {
        sender.msg(messagesConfig.invalid_value)
    }

    private fun gameModeCallback(sender: CommandSender, exception: ArgumentSyntaxException) {
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

        val gamemodesList : ArrayList<String> = ArrayList()

        gamemodesList.add("creative")
        gamemodesList.add("survival")
        gamemodesList.add("spectator")
        gamemodesList.add("adventure")

        val gamemodes = ArgumentString("gamemodes")
        gamemodes.setSuggestionCallback { _, _, suggestion ->
            gamemodesList.forEach {
                suggestion.addEntry(SuggestionEntry(it))
            }
        }

        setArgumentCallback({ sender: CommandSender, exception: ArgumentSyntaxException ->
            targetCallback(
                sender,
                exception
            )
        }, player)
        setArgumentCallback({ sender: CommandSender, exception: ArgumentSyntaxException ->
            gameModeCallback(
                sender,
                exception
            )
        }, gamemodes)

        addSyntax({ sender: CommandSender, context: CommandContext -> executeOnSelf(sender, context, gamemodes) }, gamemodes)
        addSyntax({ sender: CommandSender, context: CommandContext -> executeOnOther(sender, context, gamemodes) }, gamemodes, player)
    }

    private fun replaceGamemodes(input: String) : GameMode? {
        return when(input) {
            "0" -> GameMode.SURVIVAL
            "1" -> GameMode.CREATIVE
            "2" -> GameMode.ADVENTURE
            "3" -> GameMode.SPECTATOR
            "c" -> GameMode.CREATIVE
            "s" -> GameMode.SURVIVAL
            "sp" -> GameMode.SPECTATOR
            "a" -> GameMode.ADVENTURE
            "creative" -> GameMode.CREATIVE
            "survival" -> GameMode.SURVIVAL
            "spectator" -> GameMode.SPECTATOR
            "adventure" -> GameMode.ADVENTURE
            else -> null
        }
    }

}