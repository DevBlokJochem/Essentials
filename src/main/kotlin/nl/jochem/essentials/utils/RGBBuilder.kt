package nl.jochem.essentials.utils

import net.kyori.adventure.util.RGBLike

class RGBBuilder(red : Int, green : Int, blue : Int) : RGBLike {

    private val red : Int
    private val green : Int
    private val blue : Int

    init {
        this.red = red
        this.green = green
        this.blue = blue
    }

    override fun red(): Int {
        return red
    }

    override fun green(): Int {
        return green
    }

    override fun blue(): Int {
        return blue
    }
}