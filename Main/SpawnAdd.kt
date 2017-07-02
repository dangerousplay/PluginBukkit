package Main


import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

/**
 * Created by GMDAV on 02/05/2017.
 */
class SpawnAdd : Listener {


    @EventHandler
    private fun Click(event: PlayerInteractEvent) {
        if (event.player.isOp && event.action == Action.RIGHT_CLICK_BLOCK && event.player.itemInHand.type == Material.BLAZE_ROD) {
            val Lugar = event.player.location

            try {
                val Locations = Main.instance!!.config.getStringList("Spawns")
                Locations.add("addscraploc " + Lugar.blockX + " " + Lugar.blockY + " " + Lugar.blockZ)
                Main.instance!!.config.set("Spawns", Locations)
                event.player.sendMessage(Lugar.toString())
            } catch (Ex: Exception) {
                Ex.printStackTrace()
            }


            Main.instance!!.saveConfig()

        }
    }

    companion object {

        private val Wand = ItemStack(Material.BLAZE_ROD)
    }


}
