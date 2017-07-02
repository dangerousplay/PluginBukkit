package Main.listeners

import Main.Main
import Main.Runnable.BukkitRunnable
import Main.Util.SpawnUtil
import Main.ItemsEdit
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import br.com.tlcm.cc.events.CoreDStoreEvent
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffectType
import java.util.*

/**
 * Created by GMDAV on 20/04/2017.
 */
open class PlayerUseItems : Listener {
    private val PlayersUseTNT = Hashtable<String, Map<Location, List<String>>>()
    private val LocationUsedTNT = Hashtable<Location, Map<String, List<String>>>()
    private val PlayersAtingidos = Hashtable<String, List<String>>()


    @EventHandler
    open fun PlayerBuyItem(event: CoreDStoreEvent) {

        if (event.storeItem.itemMeta == ItemsEdit.PocaoInvisibilidade().itemMeta) {
            ({ event.player.inventory.remove(event.storeItem) } as BukkitRunnable).runTaskLater(Main.instance!!, 1)


            val Meta = ItemsEdit.PocaoInvisibilidade().itemMeta
            Meta.displayName = "§aPoção de Invisibilidade"
            Meta.lore = Arrays.asList("§aUse para ficar invisível,", "Você só será revelado quando Atacar!")

            val Potion = ItemsEdit.PocaoInvisibilidade()
            Potion.itemMeta = Meta


            event.player.inventory.addItem(Potion)

        }

        if (event.storeItem.itemMeta == ItemsEdit.PocaoVelocidade().itemMeta) {
            ({ event.player.inventory.remove(event.storeItem) } as BukkitRunnable).runTaskLater(Main.instance!!, 1)


            val Meta = ItemsEdit.PocaoInvisibilidade().itemMeta
            Meta.displayName = "§bPoção de Velocidade"
            Meta.lore = listOf("§aUse para andar mais rápido,")

            val Potion = ItemsEdit.PocaoInvisibilidade()
            Potion.itemMeta = Meta


            event.player.inventory.addItem(Potion)

        }

        if (event.storeItem.type == Material.TNT) {
            val Meta = event.storeItem.itemMeta

            Meta.lore = listOf("§cUse para explodir players")
            Meta.displayName = "§4C4 BOMB"

            event.storeItem.itemMeta = Meta
        }

    }


    @EventHandler(ignoreCancelled = true)
    open fun PlayerUseItem(event: PlayerItemConsumeEvent) {
        if (event.item.type == Material.GLASS_BOTTLE) {
            SpawnUtil.removeplayerblood(event.player)
        }
    }


    @EventHandler(ignoreCancelled = true)
    open fun PlayerKillInvisible(event: EntityDamageByEntityEvent) {
        if (CoreD.isRunning()) {
            if (event.entity is Player && event.damager is Player) {
                val Victim = event.entity as Player

                if (CoreDPlayer.isSpectator(Victim)) {
                    return
                }

                val Damager = event.damager as Player
                if (Damager.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    Damager.sendMessage("§2Você perdeu a sua camuflagem")
                    Damager.removePotionEffect(PotionEffectType.INVISIBILITY)
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    @Throws(Exception::class)
    open fun PlayerUseTNT(event: EntityDamageByBlockEvent) {
        if (CoreD.isRunning()) {
            if (event.cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                if (event.entity is Player) {
                    var damage = 0.0
                    val Damage = event.javaClass.superclass.getDeclaredField("damage")
                    Damage.isAccessible = true
                    damage = Damage.getDouble(event)
                    event.damage = damage * 2
                }
            }
        }
    }

    @EventHandler
    open fun BlockPlace(event: BlockPlaceEvent) {
        if (CoreD.isRunning()) {
            val TNT: TNTPrimed

            if (event.block.type == Material.TNT) {
                if (event.player.itemInHand.amount == 1) {
                    event.player.inventory.remove(event.player.itemInHand)
                } else {
                    event.player.itemInHand.amount = event.player.itemInHand.amount - 1
                }

                event.blockPlaced.type = Material.AIR
                TNT = event.blockPlaced.location.world.spawnEntity(event.blockPlaced.location, EntityType.PRIMED_TNT) as TNTPrimed
                TNT.fireTicks = 20

            }


        }
    }

    @EventHandler
    open fun PlayerUseTnt(event: PlayerInteractEvent) {
        if (CoreD.isRunning()) {
            if (event.action == Action.LEFT_CLICK_AIR || event.action == Action.RIGHT_CLICK_AIR) {
                if (event.player.itemInHand.type == Material.TNT) {

                    if (event.player.itemInHand.amount == 1) {
                        event.player.inventory.remove(event.player.itemInHand)
                    } else {
                        event.player.itemInHand.amount = event.player.itemInHand.amount - 1
                    }

                    val TNT = event.player.location.add(0.0, 1.0, 0.0).world.spawnEntity(event.player.location, EntityType.PRIMED_TNT) as TNTPrimed
                    TNT.fireTicks = 20
                    TNT.getNearbyEntities(4.0, 4.0, 4.0)
                    TNT.velocity = event.player.eyeLocation.direction.multiply(0.5)
                    TNT.setMetadata("player", FixedMetadataValue(Main.instance!!, event.player.name!!))

                }
            }
        }
    }

    companion object {
        val instance = PlayerUseItems()
    }

}
