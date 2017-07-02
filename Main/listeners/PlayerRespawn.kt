package Main.listeners

import Main.Util.NPCManager
import Main.Main
import br.com.tlcm.cc.API.CoreD
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

import Main.espectador.Items.getSkullLog
import com.mojang.authlib.GameProfile
import net.minecraft.server.v1_12_R1.*
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer

/**
 * Created by GMDAV on 04/05/2017.
 */
open class PlayerRespawn : Listener {


    @EventHandler
    open fun PlayerRespawn(event: PlayerDeathEvent) {

        if (CoreD.isRunning()) {
            object : org.bukkit.scheduler.BukkitRunnable() {
                override fun run() {
                    NPCManager.instance.fakesPlayers.forEach({ T, _ ->
                        val EntityID = T.id

                        val sleep = PacketPlayOutBed(T, BlockPosition(T.locX.toInt(),1,T.locZ.toInt()))
                        val move = PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                                EntityID, 0.toLong(), (-40.8).toLong(), 0.toLong(), false)

                        val rename = PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER)


                        try {
                            val name = rename.javaClass.getDeclaredField("b")
                            name.isAccessible = true

                            val infos = name.get(rename) as MutableList<PacketPlayOutPlayerInfo.PlayerInfoData>
                            //infos.removeAt(0)
                            infos.add(rename.PlayerInfoData(GameProfile(T.profile.id,""),0,EnumGamemode.SURVIVAL,ChatMessage("")))
                            name.set(rename,infos)

                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }

                        val lock = event.entity.location.clone()
                        lock.y = 1.0
                        event.entity.sendBlockChange(lock, Material.BED_BLOCK,20)
                        (event.entity as CraftPlayer).handle.playerConnection.sendPacket(rename)
                        (event.entity as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutNamedEntitySpawn(T))
                        (event.entity as CraftPlayer).handle.playerConnection.sendPacket(sleep)
                        (event.entity as CraftPlayer).handle.playerConnection.sendPacket(move)
                    })

                    val test = mutableListOf<Int>()

                    event.entity.inventory.addItem(getSkullLog(event.entity.name))
                }
            }.runTaskLater(Main.instance!!, 20 * 3)
        }
    }

}
