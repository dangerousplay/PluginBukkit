package Main.Util


import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType

class Hologram(loc: Location, message: String) {
    internal var ArmorStand: ArmorStand

    init {
        val Am = loc.world.spawnEntity(loc,EntityType.ARMOR_STAND) as ArmorStand

        Am.isCustomNameVisible = true
        Am.setGravity(false)
        Am.isInvulnerable = true
        Am.isVisible = false
        Am.boots = null
        Am.leggings = null
        Am.chestplate = null
        Am.helmet = null
        Am.customName = message
        Am.removeWhenFarAway = false

        ArmorStand = Am
    }

    fun remove() = ArmorStand.remove()

    fun respawn(){
        val Location = ArmorStand.location
        val message = ArmorStand.customName
        ArmorStand.remove()

        ArmorStand = Location.world.spawnEntity(ArmorStand.location,EntityType.ARMOR_STAND) as ArmorStand

        ArmorStand.isCustomNameVisible = true
        ArmorStand.setGravity(false)
        ArmorStand.isInvulnerable = true
        ArmorStand.isVisible = false
        ArmorStand.boots = null
        ArmorStand.leggings = null
        ArmorStand.chestplate = null
        ArmorStand.helmet = null
        ArmorStand.customName = message
        ArmorStand.removeWhenFarAway = false
    }
}