package Main.espectador

import Main.Util.Deatharea
import Main.teams.Detetive.detetiveMain
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

import java.util.Optional

open class EspecInteract : Listener {


    @EventHandler(ignoreCancelled = true)
    open fun Use(event: PlayerInteractEvent) {
        if (CoreD.isRunning() && CoreDPlayer.isSpectator(event.player)) {
            if (event.player.itemInHand != null && event.player.itemInHand.hasItemMeta()) {
                if (event.player.itemInHand.itemMeta.displayName.equals(event.player.name, ignoreCase = true)) {
                    val Death = detetiveMain.insance.getlocalcrime(event.player.name)

                    Death.ifPresent({ P -> event.player.openInventory(P.damagesTakenInventory) })
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    open fun Throw(event: PlayerDropItemEvent) {
        if (CoreDPlayer.isSpectator(event.player))
            event.isCancelled = true
    }


}
