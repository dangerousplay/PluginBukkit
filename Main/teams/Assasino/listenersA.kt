package Main.teams.Assasino

import Main.ItemsEdit
import Main.Main
import Main.Runnable.BukkitRunnable
import Main.listeners.PlayerUseItems
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import net.minecraft.server.v1_12_R1.EntityPlayer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

import java.util.*

/**
 * Created by GMDAV on 18/04/2017.
 */
open class listenersA : Listener {

    private val Podeusar = Hashtable<String, BukkitTask>()
    private val DelaySeconds = 20

    @EventHandler(ignoreCancelled = true)
    open fun PlayerDamage(event: EntityDamageByEntityEvent) {
        if (CoreD.isRunning()) {
            if (event.entity is Player && !CoreDPlayer.isSpectator(event.entity as Player) && event.damager is Player) {

                val damager = event.damager as Player

                if (damager.inventory.itemInHand != null && damager.inventory.itemInHand.hasItemMeta()) {

                    if (damager.inventory.itemInHand.itemMeta == ItemsEdit.laminadoran.itemMeta) {

                        if (Podeusar.containsKey(damager.name)) {
                            event.isCancelled = true
                            damager.sendMessage("§4Você não pode matar agora, limpando espada")
                        }
                    }
                }
            }
        }
    }


    @EventHandler(ignoreCancelled = true)
    open fun AssasinTakeItem(event: PlayerPickupItemEvent) {
        if (CoreD.isRunning()) {
            if (AssasinMain.instance.hasplayerinside(event.player.name)) {
                if (event.item.itemStack.type == Material.BOW) {
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    open fun CraftItem(event: CraftItemEvent) {
        if (event.recipe.result.type == Material.BOW) {
            event.isCancelled = true
        }
    }


    @EventHandler(ignoreCancelled = true)
    open fun AssasinDead(event: PlayerDeathEvent) {

        if (event.entity.killer != null) {

            val damager = event.entity.killer

            if (AssasinMain.instance.hasplayerinside(event.entity.killer.name) && AssasinMain.instance.hasplayerinside(event.entity.name)) {

                event.entity.killer.sendMessage("§4Você matou um assasino!")
                val random = Random()

                val Vida = CoreDPlayer.getHealth(event.entity)

                val Damage = random.nextDouble()*10

                val Dano = (Damage + 2) * 100 / 20

                event.entity.killer.damage(Damage + 2)

                event.entity.killer.sendMessage("§cVocê perdeu " + Dano.toInt() + "% de sua vida total.")

            }

            damager.exp = 1F
            damager.level = DelaySeconds
            damager.playSound(damager.location, Sound.ENTITY_ARROW_HIT_PLAYER, 100f, 100f)

            Podeusar.put(damager.name,

                    object : BukkitRunnable {
                        override fun run() {
                            Podeusar.remove(damager.name) }
                    }.runTaskLater(Main.instance!!, 20 * DelaySeconds))


            if (AssasinMain.instance.hasplayerinside(event.entity.name)) {
                AssasinMain.instance.removePlayer(event.entity.name)
            }
        }

        if (event.entity.lastDamageCause != null && event.entity.lastDamageCause.cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            if (AssasinMain.instance.hasplayerinside(event.entity.name)) {
                if (event.entity.lastDamageCause.entity.hasMetadata("player")) {
                    if (Bukkit.getPlayer(event.entity.lastDamageCause.entity.getMetadata("player")[0].asString()) != null) {

                        val Killer = Bukkit.getPlayer(event.entity.lastDamageCause.entity.getMetadata("player")[0].asString())

                        if (AssasinMain.instance.hasplayerinside(Killer.name)) {

                            Killer.sendMessage("§4Você matou um assasino!")
                            val random = Random()

                            val Vida = CoreDPlayer.getHealth(event.entity)

                            val Damage = random.nextDouble()*10

                            val Dano = (Damage + 2) * 100 / 20

                            Killer.damage(Damage + 2)

                            Killer.sendMessage("§cVocê perdeu " + Dano.toInt() + "% de sua vida total.")

                        }
                    }
                }
            }
        }
    }

    fun Delaytouse() {
        object : BukkitRunnable {
            override fun run() = Podeusar.forEach { P, _ ->
                if (Bukkit.getPlayer(P) == null) {
                    Podeusar.remove(P)
                    return@forEach
                }

                val player = Bukkit.getPlayer(P)

                if((player.exp - (1F / (DelaySeconds))) >= 0) {
                    player.exp = (player.exp - (1F / (DelaySeconds)))
                }
                player.level = player.level - 1
            }
        }.runTaskTimer(Main.instance!!, 0, 20)
    }

}
