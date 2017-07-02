package Main.espectador

import org.bukkit.Material
import org.bukkit.SkullType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

import java.util.Arrays
import java.util.Collections


object Items {

    fun getSkullLog(player: String): ItemStack {
        val Skull = ItemStack(Material.SKULL, 1, SkullType.PLAYER.ordinal.toShort())

        Skull.itemMeta.displayName = "§aDanos Tomados " + player
        Skull.itemMeta.lore = listOf("§cUse para ver o seu histórico de dano")
        return Skull
    }

}

