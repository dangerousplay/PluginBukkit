package Main

import Main.Runnable.BukkitRunnable
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.plugin.Plugin

class ClearItems(Instance: Plugin) {

    init {
        object : BukkitRunnable{
            override fun run() = Bukkit.getServer().worlds.forEach { P ->
                P.entities.forEach { E ->
                    if (E is Item) {
                        if (E.itemStack.type == Material.REDSTONE)
                            E.remove()
                    }
                }
            }
        }.runTaskTimer(Main.instance!!, 0, (20 * 30).toLong())
    }
}
