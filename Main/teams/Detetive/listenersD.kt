package Main.teams.Detetive

import Main.ItemsEdit
import Main.Main
import Main.Runnable.BukkitRunnable
import Main.Util.Deatharea
import Main.Util.Effects
import Main.teams.Assasino.AssasinMain
import Main.teams.PacketControl
import Main.teams.PlayerType
import Main.teams.Vítima.Victimain
import Main.Util.NPCManager
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import net.minecraft.server.v1_12_R1.EnumParticle
import org.bukkit.Bukkit
import org.bukkit.Instrument
import org.bukkit.Note
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList

/**
 * Created by GMDAV on 18/04/2017.
 */
open class listenersD : Listener {

    private val random = Random()
    private var podeusar = ArrayList<String>()
    private val DelaySeconds = 5


//    @EventHandler(ignoreCancelled = true)
//    open fun Detetiveclick(event: PlayerInteractEvent) {
//        if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.LEFT_CLICK_BLOCK) {
//            if (detetiveMain.insance.hasdeath(event.clickedBlock.location) != null) {
//                if (event.item != null && event.item.hasItemMeta()) {
//                    if (event.item.itemMeta == ItemsEdit.tesouraDetetive.itemMeta) {
//                        if (detetiveMain.insance.hasplayerinside(event.player.name)) {
//                            if (!podeusar.contains(event.player.name)) {
//
//
//                                event.player.sendMessage("§1Analisando o corpo...")
//
//                                object : org.bukkit.scheduler.BukkitRunnable() {
//                                    override fun run() {
//                                        if (random.nextInt(100) >= 50) {
//
//                                            detetiveMain.insance.hasdeath(event.)!!
//                                                    .forEach { P -> P.damagesTakenByPlayer.forEach(Consumer<String> { event.player.sendMessage(it) }) }
//
//                                            podeusar.add(event.player.name)
//
//                                            event.player.level = DelaySeconds
//                                            event.player.exp = 1f
//
//
//                                            val random = Random()
//                                            val locations = detetiveMain.insance.hasdeath(event.clickedBlock.location)
//
//                                            event.player.openInventory(locations!![random.nextInt(locations.size)].damagesTakenInventory)
//
//
//                                            object : BukkitRunnable {
//                                                override fun run() {
//                                                    if (podeusar.contains(event.player.name)) {
//                                                        podeusar.remove(event.player.name)
//                                                    }
//                                                }
//                                            }.runTaskLater(Main.instance!!, 20 * DelaySeconds)
//
//
//                                        } else {
//                                            event.player.sendMessage("§4Você não conseguiu analisar o corpo")
//                                            podeusar.add(event.player.name)
//                                            event.player.level = DelaySeconds
//                                            event.player.exp = 1F
//
//                                            object : BukkitRunnable {
//                                                override fun run() {
//                                                    if (podeusar.contains(event.player.name)) {
//                                                        podeusar.remove(event.player.name)
//                                                    }
//                                                }
//                                            }.runTaskLater(Main.instance!!, 20 * DelaySeconds)
//
//
//                                        }
//
//                                        val Durab = Math.floor(event.player.itemInHand.durability + event.player.itemInHand.type.maxDurability * 0.2).toShort()
//                                        event.player.itemInHand.durability = Durab
//
//                                        if (Durab >= 238) {
//                                            event.player.inventory.remove(event.player.itemInHand)
//                                            event.player.playSound(event.player.location, Sound.ENTITY_ITEM_BREAK, 100f, 100f)
//                                            event.player.sendMessage("§cSua tesoura quebrou !")
//                                        }
//
//                                    }
//                                }.runTaskLater(Main.instance, (20 * DelaySeconds).toLong())
//
//
//                            } else {
//                                event.player.sendMessage("§4Você já está analisando um corpo")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//    }

    open fun Detetiveclickcrop(event: PlayerInteractEntityEvent) {
        //if (detetiveMain.insance.hasdeath(event.clickedBlock.location) != null) {
            if (event.player.itemInHand != null && event.player.itemInHand.hasItemMeta()) {
                if (event.player.itemInHand.itemMeta == ItemsEdit.tesouraDetetive.itemMeta) {
                    if (detetiveMain.insance.hasplayerinside(event.player.name)) {
                        if (NPCManager.instance.contains(event.rightClicked)) {
                            if (!podeusar.contains(event.player.name)) {


                                event.player.sendMessage("§1Analisando o corpo...")
                                podeusar.add(event.player.name)

                                object : org.bukkit.scheduler.BukkitRunnable() {
                                    override fun run() {
                                        if (random.nextInt(100) >= 50) {

                                            detetiveMain.insance.hasdeath(event.rightClicked.entityId)!!.forEach { P -> P.damagesTakenByPlayer.forEach(Consumer<String> { event.player.sendMessage(it) }) }

                                            val random = Random()
                                            val locations = detetiveMain.insance.hasdeath(event.rightClicked.entityId)

                                            event.player.openInventory(locations!![random.nextInt(locations.size)].damagesTakenInventory)

                                        } else {
                                            event.player.sendMessage("§4Você não conseguiu analisar o corpo")
                                        }

                                        event.player.level = DelaySeconds
                                        event.player.exp = 1F

                                        object : BukkitRunnable {
                                            override fun run() {
                                                if (podeusar.contains(event.player.name)) {
                                                    podeusar.remove(event.player.name)
                                                }
                                            }
                                        }.runTaskLater(Main.instance!!, 20 * DelaySeconds)

                                        val Durab = Math.floor(event.player.itemInHand.durability + event.player.itemInHand.type.maxDurability * 0.2).toShort()
                                        event.player.itemInHand.durability = Durab

                                        if (Durab >= 238) {
                                            event.player.inventory.remove(event.player.itemInHand)
                                            event.player.playSound(event.player.location, Sound.ENTITY_ITEM_BREAK, 100f, 100f)
                                            event.player.sendMessage("§cSua tesoura quebrou !")
                                        }

                                    }
                                }.runTaskLater(Main.instance, (20 * DelaySeconds).toLong())


                            } else {
                                event.player.sendMessage("§4Você já está analisando um corpo")
                            }
                        }
                    }
                }
       // }
        }

    }


    /** Este é um listener feito para capturar toda a vez que um detetive analisar um player usando a tesoura.
     * @param event é o evento capturado
     */

    @EventHandler(ignoreCancelled = true)
    open fun DetetiveScanPlayer(event: PlayerInteractEntityEvent) {
        if (event.rightClicked is Player && !CoreDPlayer.isSpectator((event.rightClicked as Player).name)) {
            if (event.player.itemInHand != null && event.player.itemInHand.hasItemMeta()) {
                if (event.player.itemInHand.itemMeta == ItemsEdit.tesouraDetetive.itemMeta) {
                    val Victim = event.rightClicked as Player

                    if (!podeusar.contains(event.player.name)) {

                        if (detetiveMain.insance.isRevelado(Victim.name) || detetiveMain.insance.hasplayerinside(Victim.name)) {
                            event.player.sendMessage("§cEste player já foi relevado")
                            return
                        }

                        event.player.sendMessage("§bAnalisando o " + Victim.name)

                        Effects.SendParticle(EnumParticle.FLAME, Victim.location, 50, true)


                        Victim.sendMessage("§aO detetive está te analisando !")

                        val PE = PotionEffect(PotionEffectType.BLINDNESS, 2, 1)
                        val PR = PotionEffect(PotionEffectType.SLOW, 2, 5)

                        Victim.addPotionEffect(PE)
                        Victim.addPotionEffect(PR)


                        podeusar.add(event.player.name)

                        object : org.bukkit.scheduler.BukkitRunnable() {
                            internal var timer = 5

                            override fun run() {

                                if (timer == 0) {
                                    if (random.nextInt(100) >= 30) {
                                        if (AssasinMain.instance.hasplayerinside(Victim.name)) {
                                            Bukkit.getServer().broadcastMessage("§c" + event.player.name + " descobriu que §c" + Victim.name + " §cé um assasino")
                                            PacketControl.instance.UpdateTeamSafe(Victim.name, PlayerType.Assasino, "§4")
                                            detetiveMain.insance.addRelevados(Victim.name, PlayerType.Assasino)
                                            Victimain.instance.addrevelado(Victim.name, PlayerType.Assasino)
                                        } else {
                                            Bukkit.getServer().broadcastMessage("§a" + event.player.name + " descobriu que §a" + Victim.name + " §aé um inocente")
                                            PacketControl.instance.UpdateTeamSafe(Victim.name, PlayerType.Vitima, "§a")
                                            detetiveMain.insance.addRelevados(Victim.name, PlayerType.Vitima)
                                            Victimain.instance.addrevelado(Victim.name, PlayerType.Vitima)
                                        }
                                    } else {
                                        event.player.sendMessage("§cVocê falhou na inspeção!")
                                    }

                                    event.player.level = DelaySeconds
                                    event.player.exp = 1F

                                    object : BukkitRunnable {
                                        override fun run() {
                                            if (podeusar.contains(event.player.name)) {
                                                podeusar.remove(event.player.name)
                                            }
                                        }
                                    }.runTaskLater(Main.instance!!, 20 * DelaySeconds)

                                    val Durab = Math.floor(event.player.itemInHand.durability + event.player.itemInHand.type.maxDurability * 0.2).toShort()
                                    event.player.itemInHand.durability = Durab

                                    if (Durab >= 238) {
                                        event.player.inventory.remove(event.player.itemInHand)
                                        event.player.playSound(event.player.location, Sound.ENTITY_ITEM_BREAK, 100f, 100f)
                                        event.player.sendMessage("§cSua tesoura quebrou !")
                                    }

                                    cancel()
                                }

                                Bukkit.getOnlinePlayers().forEach { P ->
                                    when (timer) {
                                        5 -> P.playNote(P.location, Instrument.BASS_GUITAR, Note.natural(1, Note.Tone.D))
                                        4 -> P.playNote(P.location, Instrument.BASS_GUITAR, Note.natural(1, Note.Tone.B))
                                        3 -> P.playNote(P.location, Instrument.BASS_GUITAR, Note.natural(1, Note.Tone.G))
                                        2 -> P.playNote(P.location, Instrument.BASS_GUITAR, Note.natural(1, Note.Tone.A))
                                        1 -> P.playNote(P.location, Instrument.BASS_GUITAR, Note.natural(1, Note.Tone.B))
                                        else -> {
                                        }
                                    }
                                }


                                timer--
                            }
                        }.runTaskTimer(Main.instance, 0, 20)


                    } else {
                        event.player.sendMessage("§4Aguarde para examinar")
                    }

                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    open fun DetetiveDead(event: PlayerDeathEvent) {

        if (detetiveMain.insance.getPlayer() != null && event.entity.killer != null) {

            if (Victimain.instance.hasplayerinside(event.entity.name) && detetiveMain.insance.hasplayerinside(event.entity.killer.name)) {

                event.entity.killer.sendMessage("§4Você matou um §ainocente!")
                val random = Random()

                val Vida = CoreDPlayer.getHealth(event.entity)

                val Damage = random.nextInt(30).toDouble()

                val Dano = Vida * (Damage / 100)

                event.entity.killer.damage(Dano)

                event.entity.killer.sendMessage("§cVocê perdeu $Dano% de sua vida total.")
                return
            }

            if (detetiveMain.insance.getPlayer()!!.contains(event.entity.name)) {
                detetiveMain.insance.removePlayer(event.entity.name)
            }
        }

        if (detetiveMain.insance.getPlayer() != null
                && event.entity.lastDamageCause != null && event.entity.lastDamageCause.cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            if (detetiveMain.insance.hasplayerinside(event.entity.name)) {
                if (event.entity.lastDamageCause.entity.hasMetadata("player")) {
                    if (Bukkit.getPlayer(event.entity.lastDamageCause.entity.getMetadata("player")[0].asString()) != null) {

                        val Killer = Bukkit.getPlayer(event.entity.lastDamageCause.entity.getMetadata("player")[0].asString())

                        if (Victimain.instance.hasplayerinside(Killer.name)) {

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

    /**
     * Aqui foi posto um Runnable feito para remover o delay quando passar o tempo determinado.
     */

    fun Delaytouse() {
        object : org.bukkit.scheduler.BukkitRunnable() {
            override fun run() {
                if (detetiveMain.insance.getPlayer() == null || detetiveMain.insance.getPlayer()!!.isEmpty()) {
                    cancel()
                }
                podeusar.forEach {
                    val player = Bukkit.getPlayer(it)
                    if((player.exp - (1F / (DelaySeconds))) >= 0) {
                        player.exp = (player.exp - (1F / (DelaySeconds)))
                    }
                    player.level = player.level - 1
                }
            }
        }.runTaskTimer(Main.instance, 0, 20)
    }

    /**
     * Capura do evento de quando um detetive usar um arco, matar o alvo de forma instantânea.
     * @param event é o evento capturado.
     */

    @EventHandler(ignoreCancelled = true)
    open fun PlayerUseBow(event: EntityDamageByEntityEvent) {
        if (CoreD.isRunning() && detetiveMain.insance.getPlayer() != null) {
            if (event.entity is Player && !CoreDPlayer.isSpectator(event.entity as Player) && event.damager is Arrow) {
                val Victim = event.entity as Player
                val Kil = (event.damager as Arrow)

                val Killer = Kil.shooter as Player

                if (detetiveMain.insance.hasplayerinside(Killer.name)) {

                    event.damage = CoreDPlayer.getHealth(Victim)

                    if (!Victim.isDead) {
                        CoreDPlayer.setHealth(Victim, 0.0)
                    }

                    if (Victimain.instance.teams.contains((event.entity as Player).name)) {
                        (event.damager as Player).sendMessage("§4Você matou um inocente!")
                        val random = Random()

                        val Vida = CoreDPlayer.getHealth(event.damager as Player)

                        val Damage = random.nextInt(30).toDouble()

                        val Dano = Damage * 100 / Vida + 1.5

                        (event.damager as Player).damage(Dano)

                        (event.damager as Player).sendMessage("§cVocê perdeu " + Dano.toInt() + "% de sua vida total.")
                    }

                }
            }
        }
    }
}
