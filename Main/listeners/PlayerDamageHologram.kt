package Main.listeners

import Main.Main
import Main.Util.Hologram
import br.com.tlcm.cc.API.CoreD
import net.minecraft.server.v1_12_R1.PacketPlayOutAttachEntity
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

/**
 * Created by GMDAV on 14/05/2017.
 */
class PlayerDamageHologram : Listener {

    @EventHandler(ignoreCancelled = true)
    private fun PlayerDamage(event: EntityDamageEvent) {
        if (CoreD.isRunning()) {
            if (event.entity is Player) {

                try {
                    val Damage = event.damage
                    val Tag = Hologram(event.entity.location.clone().add(0.0, 0.3, 0.0), "§c-$Damage ❤")

                    object : BukkitRunnable() {
                        val random = Random()

                        internal var Time = 43

                        override fun run() {

                            val addX: Double = if(random.nextBoolean()) (random.nextDouble()*-1/Math.pow(5.0,0.5)) else random.nextDouble()/Math.pow(5.0,0.5)
                            val addY: Double = if(random.nextBoolean()) (random.nextDouble()*-1/Math.pow(5.0,0.5)) else random.nextDouble()/Math.pow(5.0,0.5)
                            val addZ: Double = if(random.nextBoolean()) (random.nextDouble()*-1/Math.pow(5.0,0.5)) else random.nextDouble()/Math.pow(5.0,0.5)

                            if (Time > 0) {
                                Tag.ArmorStand.teleport(Tag.ArmorStand.location.clone().add(addX,addY,addZ))
                                Time -= 3
                            } else {
                                Tag.remove()
                                cancel()
                            }
                        }
                    }.runTaskTimer(Main.instance, 0, 2)

                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }
    }
}
