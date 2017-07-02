package Main.Util

import Main.listeners.PacketListener
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import net.minecraft.server.v1_12_R1.PacketHandshakingInSetProtocol
import net.minecraft.server.v1_12_R1.PacketPlayInUseEntity
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEntityEvent

/**
 * Created by GMDAV on 15/06/2017.
 */
class PacketVersion(val p : Player) : ChannelInboundHandlerAdapter() {

    private val player : Player = p

    override fun channelRead(p0: ChannelHandlerContext?, msg: Any?) {
        if (msg is PacketHandshakingInSetProtocol) {
            val packet = msg

            val field = packet::class.java.getDeclaredField("a")
            field.isAccessible = true

            if(field.getInt(packet) < 47) {
                player.sendMessage("Você está na 1.7 !")
            }


        }
        super.channelRead(p0, msg)
    }

    fun registerListener(p: Player) {
        val c = getChannel(p) ?: throw NullPointerException("Couldn't get channel??")
        c.pipeline().addBefore("packet_handler", "packet_in_listener",
                PacketVersion(p))
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