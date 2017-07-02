package Main.listeners

import Main.ItemsEdit
import Main.teams.Vítima.Victimain
import br.com.tlcm.cc.API.CoreD
import org.bukkit.Material
import org.bukkit.entity.Animals
import org.bukkit.entity.Cow
import org.bukkit.entity.EntityType
import org.bukkit.entity.Monster
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.player.*

/**
 * Created by GMDAV on 20/04/2017.
 */
open class disabledevents : Listener {

    @EventHandler
    open fun Achievement(event: PlayerAchievementAwardedEvent) {
        event.isCancelled = true
    }

    @EventHandler
    open fun MobSpawn(event: EntitySpawnEvent) {
        if (event.entity.type != EntityType.DROPPED_ITEM && event.entity.type != EntityType.ARMOR_STAND) {
            event.isCancelled = true
        }
    }


    @EventHandler
    open fun PlayerPickup(event: PlayerPickupItemEvent) {
        if (event.item.itemStack.type == Material.SHEARS
                || event.item.itemStack.type == Material.REDSTONE || event.item.itemStack.type == Material.DIAMOND_SWORD) {
            event.isCancelled = true
        }
    }

    @EventHandler
    open fun PlayerDropItem(event: PlayerDropItemEvent) {
        if (event.itemDrop.itemStack.type == Material.SHEARS || event.itemDrop.itemStack.type == Material.DIAMOND_SWORD) {
            event.isCancelled = true
        }
    }

    @EventHandler
    open fun Hungry(event: FoodLevelChangeEvent) {
        if (CoreD.isRunning())
            event.isCancelled = true
    }

    @EventHandler(ignoreCancelled = true)
    open fun BlockExplosion(event: EntityExplodeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    open fun PlayerSprint(event: PlayerMoveEvent) {

        if (Victimain.instance.usedItems.containsKey(event.player.name) || event.player.isSprinting) {
            Victimain.instance.usedItems.forEach({ K, V ->
                if (K.equals(event.player.name, ignoreCase = true)) {
                    if (!V.contains(ItemsEdit.ItemsToConsume.PoçãoVelocidade))
                        event.player.isSprinting = false

                }
            })
        } else {
            if (Victimain.instance.hasplayerinside(event.player.name))
                event.player.isSprinting = false
        }
    }

    @EventHandler(ignoreCancelled = true)
    open fun PlayerInteract(event: PlayerInteractEvent){
        if(event.action == Action.RIGHT_CLICK_BLOCK){
            if(event.clickedBlock.type == Material.CHEST || event.clickedBlock.type == Material.TRAPPED_CHEST ||event.clickedBlock.type == Material.ENDER_CHEST){
                event.setUseInteractedBlock(Event.Result.DENY)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    open fun PlayerInventory(event: InventoryMoveItemEvent){
        if(CoreD.isRunning() && event.initiator.name == "§c§lInvestigação do player"|| CoreD.isRunning() && event.destination.name == "§c§lInvestigação do player"){
            event.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    open fun PlayerInventoryTU(event: InventoryPickupItemEvent){
        if(CoreD.isRunning() && event.inventory.name == "§c§lInvestigação do player"){
            event.isCancelled = true
        }
    }


}
