package Main.Util

import com.mojang.authlib.GameProfile
import net.minecraft.server.v1_12_R1.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.CraftServer
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.*
import org.bukkit.entity.Entity


import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by GMDAV on 20/04/2017.
 */
class NPCManager {

    private val FakesPlayers = Hashtable<EntityPlayer, Hologram>()

    private val entityid = ArrayList<Int>()

    fun spawnCorpse(player: Player): EntityPlayer {


        val nms = (Bukkit.getServer() as CraftServer).server
        val nmsWorld = (Bukkit.getWorlds()[0] as CraftWorld).handle
        var randomUUID = UUID.randomUUID()

        while (Bukkit.getPlayer(randomUUID) != null) {
            randomUUID = UUID.randomUUID()
        }

        val Fake = EntityPlayer(nms, nmsWorld, GameProfile(randomUUID, player.name), PlayerInteractManager(nmsWorld))


        Fake.setLocation(player.location.x, player.location.y, player.location.z, 0f, 0f)

        val Under = player.location.clone()
        Under.add(0.0,-1.0,-1.0)

        FakesPlayers.put(Fake,Hologram(Under,"§4§lMorto §r§4${player.name}"))

        val npc = PacketPlayOutNamedEntitySpawn(Fake)

        val sleep = PacketPlayOutBed(Fake, BlockPosition(player.location.blockX,1,player.location.blockZ))

        val move = PacketPlayOutEntity.PacketPlayOutRelEntityMove(
                Fake.id, 0.toLong(),
                (-40.8).toLong(), 0.toLong(), false)

        val rename = PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER)

        val yUsed: Location?

        try {
//            val a = npc.javaClass.getDeclaredField("a")
//            a.isAccessible = true
//            a.set(npc, EntityID)
//            val b = npc.javaClass.getDeclaredField("b")
//            b.isAccessible = true
//            b.set(npc, gameprofile.id)
//            val c = npc.javaClass.getDeclaredField("c")
//            c.isAccessible = true
//            c.setDouble(npc, player.location.x)
//            val d = npc.javaClass.getDeclaredField("d")
//            d.isAccessible = true
//            d.setDouble(npc, player.location.y + 1.0f / 16.0f)
//            val e = npc.javaClass.getDeclaredField("e")
//            e.isAccessible = true
//            e.setDouble(npc, player.location.z)
//            val f = npc.javaClass.getDeclaredField("f")
//            f.isAccessible = true
//            f.setByte(npc, (player.location.yaw * 256.0f / 360.0f).toInt().toByte())
//            val g = npc.javaClass.getDeclaredField("g")
//            g.isAccessible = true
//            g.setByte(npc,
//                    (player.location.pitch * 256.0f / 360.0f).toInt().toByte())
//            val i = npc.javaClass.getDeclaredField("h")
//            i.isAccessible = true
//            i.set(npc, Fake.dataWatcher)

            val name = rename.javaClass.getDeclaredField("b")
            name.isAccessible = true

            val infos = name.get(rename) as MutableList<PacketPlayOutPlayerInfo.PlayerInfoData>
            //infos.removeAt(0)
            infos.add(rename.PlayerInfoData(GameProfile(randomUUID,""),0,EnumGamemode.SURVIVAL,ChatMessage("")))
            name.set(rename,infos)

            val sleepid = sleep.javaClass.getDeclaredField("a")
            sleepid.isAccessible = true
            sleepid.setInt(sleep,Fake.id)

            val sleepblock = sleep.javaClass.getDeclaredField("b")
            sleepblock.isAccessible = true
            sleepblock.set(sleep,BlockPosition(player.location.blockX, 1,
                    player.location.blockZ))

        } catch (x: Exception) {
            x.printStackTrace()
        }

        for (p in Bukkit.getServer().onlinePlayers) {
            val lock = player.location.clone()
            lock.y = 1.0
            p.sendBlockChange(lock,Material.BED_BLOCK,20)
            (p as CraftPlayer).handle.playerConnection.sendPacket(rename)
            p.handle.playerConnection.sendPacket(npc)
            p.handle.playerConnection.sendPacket(sleep)
            p.handle.playerConnection.sendPacket(move)

        }

        //Fake.bukkitEntity.location.block.type = Material.AIR
        entityid.add(Fake.id)

        currentEntId += 1
        return Fake
    }

    val fakesPlayers: Map<EntityPlayer, Hologram>
        get() = this.FakesPlayers

    fun addFakesPlayer(player: EntityPlayer, Holograma: Hologram) {
        this.FakesPlayers.put(player, Holograma)
    }

    companion object {
        val instance = NPCManager()

        private var currentEntId = 1337

        private fun getNonClippableBlockUnderPlayer(p: Player, addToYPos: Int): Location? {
            val loc = p.location
            if (loc.blockY <= 1) {
                return null
            }
            for (y in loc.blockY downTo 1) {
                val m = loc.world
                        .getBlockAt(loc.blockX, y, loc.blockZ).type
                if (m.isSolid) {
                    return Location(loc.world, loc.x, (y + addToYPos).toDouble(),
                            loc.z)
                }
            }
            return null
        }
    }

    fun getNextEntityId(): Int {
        try {
            val entityCount = net.minecraft.server.v1_12_R1.Entity::class.java.getDeclaredField("entityCount")
            entityCount.isAccessible = true
            val id = entityCount.getInt(null as Any?)
            entityCount.setInt(null as Any?, id + 1)
            return id
        } catch (var3: Exception) {
            var3.printStackTrace()
            return Math.round(Math.random() * 2.147483647E9 * 0.25).toInt()
        }

    }

    fun contains(entity : Entity) : Boolean{
        if(entity is Player){
            return FakesPlayers.filter {it.key.id == (entity as CraftPlayer).handle.id}.isNotEmpty()
        }
        return false
    }







}
