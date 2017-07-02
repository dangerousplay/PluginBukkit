package Main.listeners

import Main.Util.Deatharea
import Main.Util.NPCManager
import Main.Util.SpawnUtil
import Main.teams.Detetive.detetiveMain
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import net.minecraft.server.v1_12_R1.EntityPlayer
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.*

import java.util.*

/**
 * Created by GMDAV on 18/04/2017.
 */
open class PlayerDeath : Listener {
    private val LocationsPlayersDeaths = ArrayList<EntityPlayer>()
    private val random = Random()

    private val PlayersQuaseSangrou = Hashtable<String, Int>()
    private val entityDamageTaken = Hashtable<String, Hashtable<String, Double>>()

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    open fun onplayerdeath(evento: PlayerDeathEvent) {
        if (CoreD.isRunning()) {

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    open fun Playerkillcloser(event: PlayerDeathEvent) {
        if (CoreD.isRunning() && event.entity.killer != null && event.entity != null) {

            val entity = NPCManager.instance.spawnCorpse(event.entity)

            LocationsPlayersDeaths.add(entity)

            Bukkit.getServer().onlinePlayers
                    .filter { P -> !P.name.equals(event.entity.name, ignoreCase = true) }
                    .forEach { P -> P.playSound(event.entity.location, Sound.ENTITY_CAT_PURREOW, 100f, 100f) }

            if (event.entity.lastDamageCause.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                if (event.entity.killer != null) {
                    event.entity.killer.sendMessage("§aVocê matou o(a) §e" + event.entity.name)

                    if (!PlayersQuaseSangrou.containsKey(event.entity.killer.name)) {
                        PlayersQuaseSangrou.put(event.entity.killer.name, random.nextInt(100))
                    } else {
                        if (PlayersQuaseSangrou[event.entity.killer.name]!! + random.nextInt(100) >= 100) {
                            SpawnUtil.addplayertoblood(event.entity.killer)
                            event.entity.killer.sendMessage("§4Você está sujo de §lsangue!")
                        }

                    }

                    entityDamageTaken.forEach { Strin, Map ->
                        if (Strin.equals(event.entity.name, ignoreCase = true)) {
                            detetiveMain.insance.addlocalcrime(
                                    Deatharea(SpawnUtil.SpawnBlood(event.entity.location.add(0.0, 1.0, 0.0)),
                                            event.entity.name, Map, entity.id))
                        }
                    }


                    event.deathMessage = "§a" + event.entity.name + " §7foi morto por alguém"
                    return
                }
            }


            entityDamageTaken.forEach { Strin, Map ->
                if (Strin.equals(event.entity.name, ignoreCase = true)) {
                    detetiveMain.insance.addlocalcrime(
                            Deatharea(SpawnUtil.SpawnBlood(event.entity.location.add(0.0, -1.0, 0.0)),
                                    event.entity.name, Map,entity.id))
                }
            }

            event.deathMessage = "§a" + event.entity.name + " §7morreu misteriosamente"
            return
        }

        event.deathMessage = null
    }

    @EventHandler(ignoreCancelled = true)
    open fun Playertomardano(event: EntityDamageByEntityEvent) {
        if (CoreD.isRunning()) {

            if (event.entity is Player && !CoreDPlayer.isSpectator(event.entity as Player) && event.damager is Player) {

                val Victim = event.entity as Player
                val Damager = event.damager as Player
                val Damage = event.damage

                if (entityDamageTaken.containsKey(Victim.name)) {

                    entityDamageTaken.forEach { Strin, Map ->
                        if (Strin.equals(Victim.name, ignoreCase = true)) {
                            if (Map.containsKey(Damager.name)) {
                                Map.replace(Damager.name, Map[Damager.name]!! + Damage)
                            } else {
                                Map.put(Damager.name, Damage)
                            }
                        }
                    }
                } else {
                    val newmap = Hashtable<String, Double>()
                    newmap.put(Damager.name, Damage)

                    entityDamageTaken.put(Victim.name, newmap)
                }
            }
        }

    }

    val locationsPlayersDeaths: List<EntityPlayer>
        get() = this.LocationsPlayersDeaths

    fun addPlayerDeath(player: EntityPlayer) {
        this.LocationsPlayersDeaths.add(player)
    }

    fun getEntityDamageTaken(): Map<String, Map<String, Double>> {
        return entityDamageTaken
    }

    fun getDamageEntity(player: String): Map<String, Double> {
        return (entityDamageTaken as java.util.Map<String, Map<String, Double>>).getOrDefault(player, HashMap<String, Double>())
    }

    companion object {
        val instance = PlayerDeath()
    }
}
