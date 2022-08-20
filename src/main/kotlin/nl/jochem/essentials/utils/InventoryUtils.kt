package nl.jochem.essentials.utils

import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack

fun Player.clearInv() {
    for(i in 1..inventory.size) {
        inventory.setItemStack(i-1, ItemStack.AIR)
    }
}