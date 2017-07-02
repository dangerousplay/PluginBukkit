package Main.teams.Vítima

import Main.ItemsEdit
import Main.Util.PacketUtil.getAccessibleField
import Main.teams.Detetive.detetiveMain
import br.com.tlcm.cc.API.CoreDPlayer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import java.lang.reflect.Field

import java.util.Random
import kotlin.reflect.full.functions
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.isAccessible

/**
 * Created by GMDAV on 18/04/2017.
 */
open class listenersV : Listener {

    @EventHandler(ignoreCancelled = true)
    open fun PlayerUseItem(event: PlayerItemConsumeEvent) {
        if (event.item.type == Material.GLASS_BOTTLE) {
            Victimain.instance.addUsedItems(event.player.name, ItemsEdit.ItemsToConsume.PoçãoVelocidade)
        }
    }


    @EventHandler(ignoreCancelled = true)
    open fun PlayerUseBow(event: EntityDamageByEntityEvent) {
        if (event.entity is Player && !CoreDPlayer.isSpectator(event.entity as Player) && event.damager is Arrow) {
            val Victim = event.entity as Player
            val Kil = (event.damager as Arrow)

            val Killer = Kil.shooter as Player

            if (Victim.name.equals(Killer.name, ignoreCase = true)) {
                event.isCancelled = true
                return
            }

            if (Victimain.instance.hasplayerinside(Killer.name)) {

                event.damage = CoreDPlayer.getHealth(Victim)

                if (!Victim.isDead) {
                    CoreDPlayer.setHealth(Victim, 0.0)
                }

                if (Victimain.instance.teams.contains((event.entity as Player).name) || detetiveMain.insance.hasplayerinside((event.entity as Player).name)) {

                    Killer.sendMessage("§4Você matou um inocente!")
                    val random = Random()

                    val Vida = CoreDPlayer.getHealth(Killer)

                    val Damage = random.nextDouble()*10

                    val Dano = (Damage + 2) * 100 / 20

                    Killer.damage(Damage + 2)

                    Killer.sendMessage("§cVocê perdeu $Dano% de sua vida total.")

                    if (Victimain.instance.hasplayerinside(Victim.name)) {
                        Victimain.instance.removePlayer(Victim.name)
                    } else {
                        detetiveMain.insance.removePlayer(Victim.name)
                    }

                    return

                }

                (event.entity as Player).sendMessage("§aVocê matou um assasino!")
            }
        }


    }

    @EventHandler(ignoreCancelled = true)
    open fun PlayerKillinocent(event: PlayerDeathEvent) {
        val Victim = event.entity
        var Killer: Player? = event.entity.killer


        if (Killer != null && Victimain.instance.hasplayerinside(Killer.name)) {


            if (Victimain.instance.teams.contains(Victim.name) || detetiveMain.insance.getPlayer() != null && detetiveMain.insance.hasplayerinside(event.entity.name)) {

                event.entity.killer.sendMessage("§4Você matou um inocente!")
                val random = Random()



               val  Healht : Field = getAccessibleField(PlayerDeathEvent::class.java,"health")!!

                val Vida = Healht.getDouble(event)

                val Damage = random.nextDouble()*10

                val Dano = (Damage + 2) * 100 / 20

                Killer.damage(Damage + 2)

                event.entity.killer.sendMessage("§cVocê perdeu " + Dano.toInt() + "% de sua vida total.")

                return
            }

            event.entity.sendMessage("§aVocê matou um assasino!")
        }

        if (event.entity.lastDamageCause != null && event.entity.lastDamageCause.cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            if (Victimain.instance.hasplayerinside(event.entity.name)) {
                if (event.entity.lastDamageCause.entity.hasMetadata("player")) {
                    if (Bukkit.getPlayer(event.entity.lastDamageCause.entity.getMetadata("player")[0].asString()) != null) {

                        Killer = Bukkit.getPlayer(event.entity.lastDamageCause.entity.getMetadata("player")[0].asString())

                        if (Victimain.instance.hasplayerinside(Killer!!.name)) {

                            Killer.sendMessage("§4Você matou um §ainocente!")
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

    @EventHandler
    open fun VictimDeath(event: PlayerDeathEvent) {
        if (Victimain.instance.hasplayerinside(event.entity.name)) {
            Victimain.instance.removePlayer(event.entity.name)
        }
    }

}
