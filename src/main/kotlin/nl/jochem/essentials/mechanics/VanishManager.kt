package nl.jochem.essentials.mechanics

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.adventure.bossbar.BossBarManager
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import nl.jochem.essentials.utils.RGBBuilder
import java.util.UUID
import java.util.function.Predicate

object VanishManager {

    private val invisPlayers: ArrayList<UUID> = ArrayList()
    private val bossBar : BossBar = BossBar.bossBar(Component.text("Vanish").color(TextColor.color(RGBBuilder(170,0,204))), 1f, BossBar.Color.PURPLE, BossBar.Overlay.NOTCHED_12)
    private val bm : BossBarManager = BossBarManager()
    private val handler = EventNode.type("vanish", EventFilter.PLAYER)

    init {
        MinecraftServer.getGlobalEventHandler().addChild(handler)
        handler.addListener(PlayerSpawnEvent::class.java, this::onJoin)
    }

    private fun onJoin(event: PlayerSpawnEvent) {
        if(invisPlayers.contains(event.player.uuid)) {
            bm.addBossBar(event.player, bossBar)
            event.player.setVisible(false)
            return
        }
    }

    private fun Player.setVisible(vanish: Boolean) {
        updateViewableRule { vanish }
    }

    fun addPlayer(player : Player) {
        if(invisPlayers.contains(player.uuid)) return
        invisPlayers.add(player.uuid)
        bm.addBossBar(player, bossBar)
        player.setVisible(false)
    }

    fun removePlayer(player : Player) {
        if(!invisPlayers.contains(player.uuid)) return
        invisPlayers.remove(player.uuid)
        bm.removeBossBar(player, bossBar)
        player.setVisible(true)
    }

    fun checkVanish(player: UUID): Boolean {
        return invisPlayers.contains(player)
    }

}