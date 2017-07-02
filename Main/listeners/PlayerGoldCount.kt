package Main.listeners

import Main.teams.PacketControl
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.events.CoreDStoreEvent
import net.minecraft.server.v1_12_R1.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.inventory.Inventory
import sun.audio.AudioPlayer
import java.util.*
import java.util.function.BiFunction

/**
 * Created by GMDAV on 22/06/2017.
 */
class PlayerGoldCount : Listener {
    val GoldPlayer = Hashtable<String,ScoreboardScore>()

    @EventHandler
    fun PlayerTake(event: PlayerPickupItemEvent) {
        if(CoreD.isRunning()) {
            if (event.item.name.contains("ingot",ignoreCase = true) && event.item.name.contains("gold",ignoreCase = true)) {
                val Amount = event.item.itemStack.amount

                GoldPlayer.computeIfPresent(event.player.name,{_,V ->  V.score += Amount; V})
                PacketControl.instance.UpdateSafeScoreBoard(event.player,GoldPlayer[(event.player.name)]!!,Bukkit.getOnlinePlayers().filter { it.name != event.player.name})
            }
        }
    }

    @EventHandler
    fun Playerdrop(event: PlayerDropItemEvent){
        if(CoreD.isRunning()) {
            if (event.itemDrop.name.contains("ingot",ignoreCase = true) && event.itemDrop.name.contains("gold",ignoreCase = true)) {

                val Amount = event.itemDrop.itemStack.amount

                GoldPlayer.computeIfPresent(event.player.name, {_,V -> if(V.score - Amount < 0) { V.score = 0; V} else { V.score -= Amount; V}} )
                PacketControl.instance.UpdateSafeScoreBoard(event.player,GoldPlayer[(event.player.name)]!!,Bukkit.getOnlinePlayers().filter { it.name != event.player.name })
            }
        }
    }

    @EventHandler
    fun PlayerBuy(event: CoreDStoreEvent){
        if(CoreD.isRunning()){
            if(event.storeItem.type == Material.GOLD_INGOT){
                val Amount = event.storeItem.amount
                GoldPlayer.computeIfPresent(event.player.name,{_,V ->  V.score += Amount; V})
                PacketControl.instance.UpdateSafeScoreBoard(event.player,GoldPlayer[(event.player.name)]!!,Bukkit.getOnlinePlayers().filter { it.name != event.player.name})
            }
        }
    }

    @EventHandler
    fun PlayerDeath(event: PlayerDeathEvent){
        GoldPlayer.remove(event.entity.name)
    }

    companion object{
        val instance = PlayerGoldCount()
    }

    fun Start(){
        val Scoreboard = Scoreboard()
        val Base = ScoreboardObjective(Scoreboard,"§6Ouros", ScoreboardBaseCriteria("dummy"))

        Bukkit.getOnlinePlayers().forEach { Player ->
            val temp = ScoreboardScore(Scoreboard, Base, Player.name)
            temp.score = (Player.inventory as CraftInventory).getQuantityByMaterial(Material.GOLD_INGOT)
            GoldPlayer.put(Player.name,temp)
        }


        Bukkit.getOnlinePlayers().forEach { Player ->

            GoldPlayer.filter { !it.key.equals(Player.name,ignoreCase = true)}.forEach { K, V ->
                run {
                    val ScoreScore = ScoreboardScore(Scoreboard, Base, K)
                    ScoreScore.score = V.score

                    val Packet = PacketPlayOutScoreboardObjective(Base, 0)
                    val Display = PacketPlayOutScoreboardDisplayObjective(2, Base)

                    val Score = PacketPlayOutScoreboardScore(ScoreScore)

                    (Player as CraftPlayer).handle.playerConnection.sendPacket(Packet)
                    Player.handle.playerConnection.sendPacket(Display)
                    Player.handle.playerConnection.sendPacket(Score)
                }
            }
        }
    }

    fun Inventory.getQuantityByMaterial(Material: Material) : Int {
        return this.contents.toList().stream().filter { it?.type?.equals(Material) ?: false }.mapToInt { it.amount }.sum()
    }

    fun ScoreboardScore.resetScore() : ScoreboardScore{
        this.score = 0
        return this
    }
}
