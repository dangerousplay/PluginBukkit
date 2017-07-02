package Main.Util

import jdk.nashorn.internal.objects.NativeArray.forEach
import net.minecraft.server.v1_12_R1.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.HashSet
import java.util.HashMap
import java.util.ArrayList



/**
 * Created by GMDAV on 25/06/2017.
 */
class Hologram1_7 {
    private val loc: Location
    private val dy: Double
    private val message: String
    private val world: WorldServer
    private var skull: EntityWitherSkull
    private val tags = HashMap<String, Int>()
    private val AllLocations = ArrayList<Location>()
    private val AllSkullsHorses = HashMap<EntityHorse,EntityWitherSkull>()
    private var lock = false

    constructor(loc: Location, dy: Double, message: String){
        this.loc = loc
        this.dy = dy
        this.message = message
        this.world = (loc.world as CraftWorld).handle
        skull = EntityWitherSkull(world)
        skull.setLocation(loc.x, loc.y + dy + 55, loc.z, 0f, 0f)
        (loc.world as CraftWorld).handle.addEntity(skull)
    }

    fun send(player: Player) {
        if (!lock) {
            val horse = EntityHorse(world)
            horse.setLocation(loc.x, loc.y + dy + 55.25, loc.z, 0f, 0f)
            horse.age = -1700000
            horse.customName = message
            horse.customNameVisible = true
            val spawn = PacketPlayOutSpawnEntity(horse,2)
            sendPacket(player, spawn)

            val pa = PacketPlayOutAttachEntity(horse, skull)
            sendPacket(player, pa)
            tags.put(player.name, horse.id)
            AllSkullsHorses.put(horse, skull)
            AllLocations.add(horse.bukkitEntity.location)
        } else {
            Bukkit.getLogger().info("Hologram lock")
        }
    }

    fun getEntityCurrentID(): Set<Int> {
        val Numbers = HashSet<Int>()
        tags.forEach { K, V -> Numbers.add(V) }

        return Numbers
    }

    fun getAllSkullsHorses(): Map<EntityHorse, EntityWitherSkull> {
        return AllSkullsHorses
    }

    fun getLocation(): List<Location> {

        return AllLocations
    }

    @Throws(Exception::class)
    fun remove(player: Player) {
        if (!lock) {
            val packet = PacketPlayOutEntityDestroy(tags[player.name]!!)
            sendPacket(player, packet)
            tags.remove(player.name)
        } else {
            throw Exception("There is a lock on this hologram!")
        }
    }

    @Throws(Exception::class)
    fun remove() {
        if (!lock) {
            lock = true
            for (s in tags.keys) {
                val packet = PacketPlayOutEntityDestroy(tags[s]!!)
                sendPacket(Bukkit.getPlayer(s), packet)
            }
            tags.clear()
        } else {
            throw Exception("There is a lock on this hologram!")
        }
    }

    private fun sendPacket(p: Player, packet: Packet<*>) {
        (p as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }
}