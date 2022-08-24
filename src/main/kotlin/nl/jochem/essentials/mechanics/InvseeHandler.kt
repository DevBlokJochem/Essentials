package nl.jochem.essentials.mechanics

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryClickEvent
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.inventory.PlayerInventory
import nl.jochem.essentials.config.messagesConfig
import nl.jochem.essentials.utils.msg

class InvseeHandler(player: Player, target: Player) {

    private val player : Player
    private val target : Player
    private val inventory = Inventory(InventoryType.CHEST_5_ROW, "§6§lInventory ${target.username}")
    private val playerHandler = EventNode.event("invsee-player-${player.uuid}", EventFilter.PLAYER) { obj -> obj.player == player || obj.player == target }
    private val inventoryHandler = EventNode.event("invsee-inv-${player.uuid}", EventFilter.INVENTORY) { obj -> obj.inventory == null || obj.inventory == inventory}
    private val handler = MinecraftServer.getGlobalEventHandler()

    init {
        this.player = player
        this.target = target
        start()
        inventory.sync(target.inventory)
        player.openInventory(inventory)
    }

    private fun start() {
        handler.addChild(playerHandler)
        handler.addChild(inventoryHandler)
        playerHandler.addListener(PlayerDisconnectEvent::class.java, this::onDisconnect)
        inventoryHandler.addListener(InventoryClickEvent::class.java, this::onClick)
        inventoryHandler.addListener(InventoryPreClickEvent::class.java, this::onClick)
        inventoryHandler.addListener(InventoryCloseEvent::class.java, this::onClose)
    }

    private fun remove() {
        handler.removeChild(playerHandler)
        handler.removeChild(inventoryHandler)
    }

    private fun onDisconnect(event: PlayerDisconnectEvent) {
        remove()
        if(event.player == target) player.msg(messagesConfig.invsee_target_disconnect)
    }

    private fun onClose(event: InventoryCloseEvent) {
        if(event.inventory != inventory) return
        remove()
    }

    private fun onClick(event: InventoryClickEvent) {
        if(event.player == target) {
            inventory.sync(target.inventory)
            return
        }
        if(player.hasPermission("essentials.invsee.edit")) {
            target.inventory.sync(inventory)
            return
        }
    }

    private fun onClick(event: InventoryPreClickEvent) {
        if(event.player == player && !event.player.hasPermission("essentials.invsee.edit") ) {
            event.isCancelled = true
        }
    }

    private fun PlayerInventory.sync(inv: Inventory) {
        for(i in 1.. inv.size) {
            if(inv.getItemStack(i-1) != getItemStack(i-1)) setItemStack(i-1, inv.getItemStack(i-1))
        }
    }

    private fun Inventory.sync(inv : PlayerInventory) {
        for(i in 1.. inv.size) {
            if(i == inv.size) return
            if(inv.getItemStack(i-1) != getItemStack(i-1)) setItemStack(i-1, inv.getItemStack(i-1))
        }
    }

}