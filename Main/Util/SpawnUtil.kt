package Main.Util

import Main.Main
import Main.Runnable.BukkitRunnable
import br.com.tlcm.cc.API.CoreDPlayer
import com.mojang.authlib.GameProfile
import net.minecraft.server.v1_12_R1.*
import org.bukkit.*
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Stairs

import java.util.*

/**
 * Created by GMDAV on 18/04/2017.
 */
object SpawnUtil {

    private val PlayersSangrando = HashSet<String>()
    private val BloodLocation = ArrayList<Location>()

    fun SpawnScraps(locations: Collection<Location>, Item: ItemStack) {

        object : BukkitRunnable {
            override fun run() {
                val random = Random()
                locations.forEach { Location ->
                    object : BukkitRunnable {
                        override fun run() { Bukkit.getWorlds()[0].dropItem(Location, Item) }
                    }.runTaskLater(Main.instance!!, 20 * random.nextInt(15))
                }
            }
        }.runTaskTimer(Main.instance!!, 0, (20 * 30).toLong())
    }

    fun SpawnBlood(loc: Location): List<Location> {
        val Locations = ArrayList<Location>()

        for (x in loc.blockX - 1..loc.blockX) {
            for (Z in loc.blockZ..loc.blockZ + 1) {
                Locations.add(Location(loc.world, x.toDouble(), loc.blockY.toDouble(), Z.toDouble()))
            }
        }

        //Locations.forEach(P -> P.getBlock().setType(Material.REDSTONE_WIRE));
        return Locations
    }

    fun SpawnBloodStart() {

        RemoveBlooc()

        object : BukkitRunnable {
            override fun run() {
                synchronized(PlayersSangrando) {
                    if (!PlayersSangrando.isEmpty()) {
                        PlayersSangrando.forEach { P ->
                            val pl = Bukkit.getPlayer(P)
                            if (CoreDPlayer.isSpectator(P)) {
                                PlayersSangrando.remove(P)
                                return@forEach
                            }
                            val Item = pl.world.dropItem(pl.location.add(0.0, 1.5, 0.0), ItemStack(Material.REDSTONE))
                            object : BukkitRunnable {
                                override fun run() {
                                    if (pl.location.block.isEmpty && !pl.location.block.isLiquid && (pl.location.block as CraftBlock).isComplete()) {
                                        pl.location.block.type = Material.REDSTONE_WIRE
                                        BloodLocation.add(pl.location)
                                    } else {
                                        if (pl.location.add(0.0, 1.0, 0.0).block.isEmpty && !pl.location.block.isLiquid && (pl.location.block as CraftBlock).isComplete()) {
                                            pl.location.block.type = Material.REDSTONE_WIRE
                                            BloodLocation.add(pl.location)
                                        }
                                    }
                                    Item.remove()
                                }
                            }.runTaskLater(Main.instance!!, 10)
                        }
                    }
                }
            }
        }.runTaskTimer(Main.instance!!, 0, 20)
    }

    @Synchronized private fun RemoveBlooc() {

        object : BukkitRunnable {
            override fun run() {
                BloodLocation.forEach { P -> P.block.type = Material.AIR }
                BloodLocation.removeIf({ BloodLocation.contains(it) })
            }
        }.runTaskTimer(Main.instance!!, 0, (20 * 15).toLong())

    }

    @Synchronized fun addplayertoblood(player: Player) {
        PlayersSangrando.add(player.name)
    }

    @Synchronized fun removeplayerblood(player: Player) {
        if (PlayersSangrando.contains(player.name)) {
            PlayersSangrando.remove(player.name)
        }
    }


    fun spawndeathplayer(player: Player): EntityPlayer {
        val nms = MinecraftServer.getServer()
        val nmsWorld = (Bukkit.getWorlds()[0] as CraftWorld).handle
        var randomUUID = UUID.randomUUID()

        while (Bukkit.getPlayer(randomUUID) != null) {
            randomUUID = UUID.randomUUID()
        }

        val npc = EntityPlayer(nms, nmsWorld, GameProfile(randomUUID, player.name), PlayerInteractManager(nmsWorld))

        npc.setLocation(player.location.x, player.location.y, player.location.z, 0f, 0f)

        npc.health = 0f

        val PKT = PacketPlayOutSpawnEntity(npc, 2)

        Bukkit.getServer().onlinePlayers.forEach { P -> (P as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutSpawnEntityLiving(npc)) }
        Bukkit.getServer().onlinePlayers.forEach { P -> (P as CraftPlayer).handle.playerConnection.sendPacket(PKT) }
        return npc
    }

    fun spawndeathplayer(player: Player, x: Int, y: Int): EntityPlayer {
        val nms = MinecraftServer.getServer()
        val nmsWorld = (Bukkit.getWorlds()[0] as CraftWorld).handle
        var randomUUID = UUID.randomUUID()

        while (Bukkit.getPlayer(randomUUID) != null) {
            randomUUID = UUID.randomUUID()
        }

        val npc = EntityPlayer(nms, nmsWorld, GameProfile(randomUUID, player.name), PlayerInteractManager(nmsWorld))

        npc.setLocation(player.location.x, player.location.y, player.location.z, x.toFloat(), y.toFloat())

        npc.health = 0f

        val PKT = PacketPlayOutSpawnEntity(npc, 2)

        Bukkit.getServer().onlinePlayers.forEach { P -> (P as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutSpawnEntityLiving(npc)) }
        Bukkit.getServer().onlinePlayers.forEach { P -> (P as CraftPlayer).handle.playerConnection.sendPacket(PKT) }
        return npc
    }

    fun CraftBlock.isComplete() : Boolean {
        val field = CraftBlock::class.java.getDeclaredMethod("getNMSBlock")
        field.isAccessible = true

        val NMSBlock = field.invoke(this)


        if(NMSBlock is BlockStairs || NMSBlock is BlockFence || NMSBlock is BlockFenceGate
                || NMSBlock is BlockTorch || NMSBlock is BlockTrapdoor || NMSBlock is BlockSign
                || NMSBlock is BlockBed || NMSBlock is BlockChest || NMSBlock is BlockCarpet
                || NMSBlock is BlockEnchantmentTable || NMSBlock is BlockEndGateway) {
            return false
        }

        if(NMSBlock is BlockDirt && this.data == 1.toByte() || this.isLiquid){
            return false
        }

        when(this.typeId){
            126 -> return false
            44 -> return false
            182 -> return false
            205 -> return false
        }

        return true
    }


}
