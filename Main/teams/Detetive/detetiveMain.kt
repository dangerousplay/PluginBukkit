package Main.teams.Detetive

import Main.ItemsEdit
import Main.Util.Deatharea
import Main.teams.PlayerType
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import Main.Util.PacketUtil
import Main.listeners.PacketListener
import net.minecraft.server.v1_12_R1.ScoreboardTeamBase

import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

/**
 * Created by GMDAV on 18/04/2017.
 */
class detetiveMain {
    private var player: MutableList<String>? = null
    private val locaisdocrime = Hashtable<Deatharea,Int>()
    private val Revelados = Hashtable<String, PlayerType>()

    @Synchronized fun setPlayer(player: MutableList<String>) {
        this.player = player

        player.forEach { player ->

            Bukkit.getPlayer(player).sendMessage("§1§lVocê é um detetive!")
            Bukkit.getPlayer(player).sendMessage("                             ")
            Bukkit.getPlayer(player).sendMessage("§a§lObjetivos:")
            Bukkit.getPlayer(player).sendMessage("                             ")
            Bukkit.getPlayer(player).sendMessage("§f- Use sua tesoura para descobrir os assasinos")
            Bukkit.getPlayer(player).sendMessage("§f- Proteja os inocentes")
            Bukkit.getPlayer(player).sendMessage("§f- Condene os assasinos e absolva os inocentes")


//            Bukkit.getServer().onlinePlayers.filter { P -> !P.name.equals(player, ignoreCase = true) }
//                    .forEach { P -> P.sendMessage("§1§l$player §9é o detetive") }

            Bukkit.getPlayer(player).inventory.addItem(ItemsEdit.tesouraDetetive)


            var ScoreBoard: PacketPlayOutScoreboardTeam

            ScoreBoard = PacketPlayOutScoreboardTeam()

            PacketUtil.setTeamName(ScoreBoard, player)
            PacketUtil.setTeamDisplayName(ScoreBoard, player)
            PacketUtil.setTeamPrefix(ScoreBoard, "§1§lDetetive §9")
            PacketUtil.setTeamSuffix(ScoreBoard, " §a§oAmigo")
            PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
            PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(player))
            PacketUtil.setTeamMode(ScoreBoard, 0)
            PacketUtil.setNameTagVisible(ScoreBoard, ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS)
            PacketUtil.setTEAM_COLISIONRULE(ScoreBoard, ScoreboardTeamBase.EnumTeamPush.ALWAYS)

            (Bukkit.getPlayer(player) as CraftPlayer).handle.playerConnection.sendPacket(ScoreBoard)


            Bukkit.getServer().onlinePlayers.filter{!player.equals(it.name,ignoreCase = true)}.forEach { P ->
                ScoreBoard = PacketPlayOutScoreboardTeam()

                PacketUtil.setTeamName(ScoreBoard, P.name)
                PacketUtil.setTeamDisplayName(ScoreBoard, P.name)
                PacketUtil.setTeamPrefix(ScoreBoard, "§6§lSuspeito §e")
                PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
                PacketUtil.addPlayer(ScoreBoard, P)
                PacketUtil.setTeamMode(ScoreBoard, 0)
                PacketUtil.setNameTagVisible(ScoreBoard, ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS)
                PacketUtil.setTEAM_COLISIONRULE(ScoreBoard, ScoreboardTeamBase.EnumTeamPush.ALWAYS)

                if (!P.name.equals(player, ignoreCase = true))
                    (Bukkit.getPlayer(player) as CraftPlayer).handle.playerConnection.sendPacket(ScoreBoard)

            }

            Bukkit.getServer().onlinePlayers.filter{player.equals(it.name,ignoreCase = true) && it.name != player}.forEach { P ->
                PacketUtil.setTeamName(ScoreBoard, P.name)
                PacketUtil.setTeamDisplayName(ScoreBoard, P.name)
                PacketUtil.setTeamPrefix(ScoreBoard, "§1§lDetetive §9")
                PacketUtil.setTeamSuffix(ScoreBoard, " §a§oAmigo")
                PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
                PacketUtil.addPlayer(ScoreBoard, P)
                PacketUtil.setTeamMode(ScoreBoard, 0)
                PacketUtil.setNameTagVisible(ScoreBoard, ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS)
                PacketUtil.setTEAM_COLISIONRULE(ScoreBoard, ScoreboardTeamBase.EnumTeamPush.ALWAYS)

                (Bukkit.getPlayer(player) as CraftPlayer).handle.playerConnection.sendPacket(ScoreBoard)
            }

            PacketListener(Bukkit.getPlayer(player)).registerListener(Bukkit.getPlayer(player))
        }

        Bukkit.getOnlinePlayers().filter { !player.contains(it.name) }.forEach {it.sendMessage("§1Os detetives são: ${toString()}")}
    }

    private fun MutableList<String>.equalsIgnorecase(string: String) = this.filter { it.equals(string,true) }

    internal fun List<String>.equalsIgnorecase(string: String) = this.filter { it.equals(string,true) }

    fun getPlayer() = this.player

    fun hasplayerinside(player: String): Boolean = this.player != null && this.player!!.filter{ it.equals(player,ignoreCase = true)}.isNotEmpty()

    fun removePlayer(player: String) {
        this.player?.remove(player)
    }

    fun addlocalcrime(crime: Deatharea) {
        this.locaisdocrime.put(crime,crime.EntityIDs)
    }

    fun hasdeath(int: Int) : List<Deatharea>?{
        return locaisdocrime.filter { it.value == int }.map { it.key }.toMutableList()
    }

    fun getlocalcrime(player: String): Optional<Deatharea> {

        val Player = locaisdocrime.filter { P -> P.key.player.equals(player, ignoreCase = true) }.map{it.key}.toList()


        return Optional.ofNullable(if (Player.isEmpty()) null else Player[0])
    }

    @Synchronized
    fun getBukkitPlayers() : MutableList<Player> = if(this.player != null) this.player!!.map { Bukkit.getPlayer(it)}.toMutableList() else ArrayList<Player>()

    fun removelocalcrime(crime: Deatharea) {
        if (locaisdocrime.contains(crime)) {
            locaisdocrime.remove(crime)
        }
    }

    fun addRelevados(Revelado: String, type: PlayerType) = Revelados.put(Revelado, type)


    fun isRevelado(player: String): Boolean = Revelados.containsKey(player)

    companion object {
        val insance = detetiveMain()
    }

    override fun toString(): String {
        val MessageBuilder = StringBuilder()

        for (i in player!!.indices) {

            if (i == player!!.size - 1) {
                MessageBuilder.append(player!![i]).append(".")
                continue
            }

            if (i == player!!.size - 2) {
                MessageBuilder.append(player!![i]).append(" e ")
                continue
            }

            MessageBuilder.append(player!![i]).append(", ")
        }

        return MessageBuilder.toString()
    }


}
