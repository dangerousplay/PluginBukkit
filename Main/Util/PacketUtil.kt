package Main.Util


import net.minecraft.server.v1_12_R1.EntityPlayer
import net.minecraft.server.v1_12_R1.Packet
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam
import net.minecraft.server.v1_12_R1.ScoreboardTeamBase
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.ArrayList
import java.util.Arrays
import java.util.Collections

object PacketUtil {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    fun constructTeamCreatePacket(name: String, prefix: String, suffix: String, player: Player): PacketPlayOutScoreboardTeam {
        val packet = PacketPlayOutScoreboardTeam()

        setTeamName(packet, name)
        setTeamDisplayName(packet, name)
        setTeamPrefix(packet, prefix)
        setTeamSuffix(packet, suffix)
        setTeamPlayers(packet, player)
        setTeamMode(packet, 0) // If 0 then the team is created.
        setTeamFriendlyFire(packet, 1) // 1 for on

        return packet
    }

    fun constructTeamUpdatePacket(name: String, prefix: String, suffix: String): PacketPlayOutScoreboardTeam {
        val packet = PacketPlayOutScoreboardTeam()

        setTeamName(packet, name)
        setTeamDisplayName(packet, name)
        setTeamPrefix(packet, prefix)
        setTeamSuffix(packet, suffix)
        //setPlayers(packet, player);
        setTeamMode(packet, 2) // If 2 the team team information is updated.
        setTeamFriendlyFire(packet, 1) // 1 for on

        return packet
    }

    // -------------------------------------------- //
    // SPECIFIC FIELDS UTIL
    // -------------------------------------------- //

    // http://wiki.vg/Protocol#Teams
    // String a: Team Name
    // String b: Team Display Name
    // String c: Team Prefix
    // String d: Team Suffix
    // ArrayList<String> e: Players
    // int f: Mode
    // int g: Friendly fire

    val TEAM_NAME: Field = getAccessibleField(PacketPlayOutScoreboardTeam::class.java, "a")!!
    val TEAM_DISPLAY_NAME: Field = getAccessibleField(PacketPlayOutScoreboardTeam::class.java, "b")!!
    val TEAM_PREFIX: Field = getAccessibleField(PacketPlayOutScoreboardTeam::class.java, "c")!!
    val TEAM_SUFFIX: Field = getAccessibleField(PacketPlayOutScoreboardTeam::class.java, "d")!!
    val TEAM_PLAYERS: Field = getAccessibleField(PacketPlayOutScoreboardTeam::class.java, "h")!!
    val TEAM_MODE: Field = getAccessibleField(PacketPlayOutScoreboardTeam::class.java, "i")!!
    val TEAM_FRIENDLY_FIRE: Field = getAccessibleField(PacketPlayOutScoreboardTeam::class.java, "j")!!
    val TEAM_NAMETAGVISIBLE: Field = getAccessibleField(PacketPlayOutScoreboardTeam::class.java,"e")!!
    val TEAM_COLISIONRULE: Field = getAccessibleField(PacketPlayOutScoreboardTeam::class.java,"f")!!

    fun setTeamName(packet: PacketPlayOutScoreboardTeam, teamName: String) {
        set(TEAM_NAME, packet, teamName)
    }

    fun setTeamDisplayName(packet: PacketPlayOutScoreboardTeam, teamDisplayName: String) {
        set(TEAM_DISPLAY_NAME, packet, teamDisplayName)
    }

    fun setTeamPrefix(packet: PacketPlayOutScoreboardTeam, teamPrefix: String) {
        set(TEAM_PREFIX, packet, teamPrefix)
    }

    fun setTeamSuffix(packet: PacketPlayOutScoreboardTeam, teamSuffix: String) {
        set(TEAM_SUFFIX, packet, teamSuffix)
    }

    fun setTeamPlayers(packet: PacketPlayOutScoreboardTeam, players: Collection<String>) {
        set(TEAM_PLAYERS, packet, ArrayList(players))
    }

    fun setTeamPlayers(packet: PacketPlayOutScoreboardTeam, player: String) {
        setTeamPlayers(packet, listOf(player))
    }

    fun setTeamPlayers(packet: PacketPlayOutScoreboardTeam, player: Player) {
        setTeamPlayers(packet, player.name)
    }

    fun setTeamMode(packet: PacketPlayOutScoreboardTeam, mode: Int) {
        set(TEAM_MODE, packet, mode)
    }

    fun setTeamFriendlyFire(packet: PacketPlayOutScoreboardTeam, friendlyFire: Int) {
        set(TEAM_FRIENDLY_FIRE, packet, friendlyFire)
    }

    fun setNameTagVisible(packet: PacketPlayOutScoreboardTeam,Visibility: ScoreboardTeamBase.EnumNameTagVisibility){
        set(TEAM_NAMETAGVISIBLE,packet,Visibility.e)
    }

    fun setTEAM_COLISIONRULE(packet: PacketPlayOutScoreboardTeam, Colision: ScoreboardTeamBase.EnumTeamPush) {
        set(TEAM_COLISIONRULE, packet, Colision.e)
    }

    fun addPlayer(packet: PacketPlayOutScoreboardTeam, player: Player) {
        (get(packet, TEAM_PLAYERS) as MutableCollection<String>).add(player.name)
    }

    fun getTeamPlayers(packet: PacketPlayOutScoreboardTeam): Collection<String> {
        return (get(packet, TEAM_PLAYERS) as Collection<String>?)!!
    }

    // -------------------------------------------- //
    // BASIC UTIL
    // -------------------------------------------- //

    fun sendPacket(player: Player, packet: Packet<*>) {
        val cplayer = player as CraftPlayer
        val eplayer = cplayer.handle
        eplayer.playerConnection.sendPacket(packet)
    }

    private operator fun set(field: Field, `object`: Any, value: Any) {
        try {
            field.set(`object`, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private operator fun get(packet: PacketPlayOutScoreboardTeam, field: Field): Any? {
        try {
            return field.get(packet)
        } catch (Ex: Exception) {
            Ex.printStackTrace()
        }

        return null
    }

    fun getAccessibleField(clazz: Class<*>, fieldName: String): Field? {
        try {
            val field = clazz.getDeclaredField(fieldName)
            makeAccessible(field)
            return field
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun makeAccessible(field: Field) {
        if ((!Modifier.isPublic(field.modifiers) || !Modifier.isPublic(field.declaringClass.modifiers) || Modifier.isFinal(field.modifiers)) && !field.isAccessible) {
            field.isAccessible = true
        }
    }

}