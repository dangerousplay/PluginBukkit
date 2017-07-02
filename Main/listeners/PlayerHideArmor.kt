package Main.listeners

import net.minecraft.server.v1_12_R1.EnumItemSlot
import net.minecraft.server.v1_12_R1.Packet
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

/**
 * Created by GMDAV on 25/06/2017.
 */
class PlayerHideArmor : Listener {
    val Delay : Long = 3
    val PlayersUsando = LinkedList<String>()


    init{
        object : BukkitRunnable(){
            override fun run() {
                Bukkit.getOnlinePlayers().forEach { Player ->
                    Bukkit.getOnlinePlayers().filter{!it.name.equals(Player.name,ignoreCase = true)}.forEach { P ->
                        synchronized(PlayersUsando) {
                            if (!PlayersUsando.contains(Player.name)) {
                                val Packets = ArrayList<Packet<*>>()
                                Packets.add(PacketPlayOutEntityEquipment(Player.entityId, EnumItemSlot.FEET, CraftItemStack.asNMSCopy(null)))
                                Packets.add(PacketPlayOutEntityEquipment(Player.entityId, EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(null)))
                                Packets.add(PacketPlayOutEntityEquipment(Player.entityId, EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(null)))
                                Packets.add(PacketPlayOutEntityEquipment(Player.entityId, EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(null)))

                                Packets.forEach { (P as CraftPlayer).handle.playerConnection.sendPacket(it) }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.Main.instance!!,0,20)
    }

    @EventHandler
    fun PlayerDamage(event: EntityDamageByEntityEvent){
        if(event.entity is Player && event.damager is Player){
            val player = event.entity as Player
            val Packets = ArrayList<Packet<*>>()
            val ArmorContents = player.inventory.armorContents

            Packets.add(PacketPlayOutEntityEquipment(event.entity.entityId,EnumItemSlot.FEET,CraftItemStack.asNMSCopy(ArmorContents[0])))
            Packets.add(PacketPlayOutEntityEquipment(event.entity.entityId,EnumItemSlot.LEGS,CraftItemStack.asNMSCopy(ArmorContents[1])))
            Packets.add(PacketPlayOutEntityEquipment(event.entity.entityId,EnumItemSlot.CHEST,CraftItemStack.asNMSCopy(ArmorContents[2])))
            Packets.add(PacketPlayOutEntityEquipment(event.entity.entityId,EnumItemSlot.HEAD,CraftItemStack.asNMSCopy(ArmorContents[3])))

            synchronized(PlayersUsando) {
                PlayersUsando.add(event.entity.name)
                object : BukkitRunnable() {
                    override fun run() {
                        PlayersUsando.remove(event.entity.name)
                    }
                }.runTaskLater(Main.Main.instance!!, 20 * Delay)
            }
        }
    }



}