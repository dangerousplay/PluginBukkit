package Main.listeners

import Main.Util.NPCManager
import Main.teams.Detetive.listenersD
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import net.minecraft.server.v1_12_R1.PacketPlayInUseEntity
import net.minecraft.server.v1_12_R1.PacketPlayInUseEntity.EnumEntityUseAction
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEntityEvent


/**
 * Created by GMDAV on 14/06/2017.
 */
class PacketListener(val p: Player) : ChannelInboundHandlerAdapter() {


    /**
     *  Captura do Packet de quando um player clica em um NPC que é apenas um Packet
     *
     *  @param ctx é o canal a ser analisado
     *  @param msg é o packet enviado do Cliente
     *  @throws Exception
     */

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is PacketPlayInUseEntity) {
            val packet = msg
                            NPCManager.instance.fakesPlayers.forEach {
                                if (it.key.id == getId(packet)) {
                                    Main.Main.instance?.ListenerDetetive?.Detetiveclickcrop(PlayerInteractEntityEvent(p, it.key.bukkitEntity))
                                    return@forEach
                                }
                            }
        }
        super.channelRead(ctx, msg)
    }

    private fun getId(packet: PacketPlayInUseEntity): Int {
        try {
            val afield = packet.javaClass.getDeclaredField("a")
            afield.isAccessible = true
            val id = afield.getInt(packet)
            afield.isAccessible = false
            return id
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }

    }

    fun registerListener(p: Player) {
        val c = getChannel(p) ?: throw NullPointerException("Couldn't get channel??")
        try {
            c.pipeline().addBefore("packet_handler", "packet_in_listener",
                    PacketListener(p))
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    fun unregisterListener(p: Player){
        val c = getChannel(p) ?: throw NullPointerException("Couldn't get channel??")
        try{
            c.pipeline().remove("packet_in_listener")
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    fun getChannel(p: Player): Channel? {
        val nm = (p as CraftPlayer).handle.playerConnection.networkManager
        try {
            return nm.channel
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}