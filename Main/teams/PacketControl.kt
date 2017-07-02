package Main.teams

import Main.teams.Detetive.detetiveMain
import Main.teams.Vítima.Victimain
import org.apache.commons.lang3.Validate
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import Main.Util.PacketUtil

import java.util.*
import sun.audio.AudioPlayer.player
import net.minecraft.server.v1_12_R1.*
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.Score


/**
 * Created by GMDAV on 13/05/2017.
 */
class PacketControl private constructor() {

    /**
     * Uma classe manager criada para evitar crash's e packets enviados duas vezes para um mesmo player
     * @sigleton
     */

    private val PacketsAndPlayers = Hashtable<String, MutableCollection<String>>()

    /**
     * Compara a existência do player dentro da lista de já enviados.
     * @param player o player a ser comparado.
     * *
     * @return a existência do player
     */

    fun containsplayer(player: String): Boolean {
        return PacketsAndPlayers.containsKey(player)
    }

    /**
     * Verifica se já foi enviado esse packet ao player em questão.
     * @param player o player
     * *
     * @param Packet o packet a ser comparado.
     * *
     * @return se já foi enviado.
     */

    fun PlayerContainsPacket(player: String, Packet: String): Boolean {
        return PacketsAndPlayers.containsKey(player) && PacketsAndPlayers[player]!!.contains(Packet)
    }

    /**
     * Adiciona o player a lista de players que já receberam este packet
     * @param player o player que recebeu o packet
     * *
     * @param Packet o packet do player
     */

    fun add(player: String, Packet: String) {
        if (containsplayer(player)) {
            val Packets = PacketsAndPlayers[player]!!
            Packets.add(Packet)
            PacketsAndPlayers.replace(player, Packets)
        } else {
            val Packets = ArrayList<String>()
            Packets.add(Packet)
            PacketsAndPlayers.put(player, Packets)
        }
    }

    /**
     * Atualiza um time de um player para todos usando packets safe-thread
     * @param PLayer o player que será enviado o packet
     * *
     * @param type o nome do time
     * *
     * @param color a cor do time
     */

    fun UpdateTeamSafe(PLayer: String, type: PlayerType, color: String) {
        val ScoreBoard = PacketPlayOutScoreboardTeam()

        when (type) {
            PlayerType.Vitima -> {
                PacketUtil.setTeamPrefix(ScoreBoard, color + "§2Inocente" + "§a ")
                PacketUtil.setTeamSuffix(ScoreBoard, color + " §a§oAmigo")
            }
            PlayerType.Assasino -> {
                PacketUtil.setTeamPrefix(ScoreBoard, color + "§4Assasino" + "§c ")
                PacketUtil.setTeamSuffix(ScoreBoard, color + " §c§oInimigo")
            }
        }

        PacketUtil.setTeamName(ScoreBoard, PLayer)
        PacketUtil.setTeamDisplayName(ScoreBoard, PLayer)
        PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
        PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(PLayer))
        PacketUtil.setTeamMode(ScoreBoard, 2)
        PacketUtil.setNameTagVisible(ScoreBoard, ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS)
        PacketUtil.setTEAM_COLISIONRULE(ScoreBoard, ScoreboardTeamBase.EnumTeamPush.ALWAYS)

        val PlayersSeram = ArrayList<Player>(Victimain.instance.bukkitPlayers)

        if (detetiveMain.insance.getPlayer() != null) {
            PlayersSeram.addAll(detetiveMain.insance.getBukkitPlayers())
        }

        sendpacket(ScoreBoard, PlayersSeram)
    }

    fun UpdateTeamSafe(PLayer: String, Prefix: String, Suffix: String, Receiver: Player) {
        val ScoreBoard = PacketPlayOutScoreboardTeam()

        PacketUtil.setTeamName(ScoreBoard, PLayer)
        PacketUtil.setTeamDisplayName(ScoreBoard, PLayer)
        PacketUtil.setTeamPrefix(ScoreBoard, Prefix)
        PacketUtil.setTeamSuffix(ScoreBoard, Suffix)
        PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
        PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(PLayer))
        PacketUtil.setTeamMode(ScoreBoard, 2)

        sendpacket(ScoreBoard, Receiver)
    }

    fun UpdateTeamSafe(PLayer: String, Prefix: String, Suffix: String, Receiver: Collection<Player>) {
        Validate.noNullElements(Receiver, "Recebidores NULOS")
        Validate.notNull(PLayer, "Player NULO !")

        val ScoreBoard = PacketPlayOutScoreboardTeam()

        PacketUtil.setTeamName(ScoreBoard, PLayer)
        PacketUtil.setTeamDisplayName(ScoreBoard, PLayer)
        PacketUtil.setTeamPrefix(ScoreBoard, Prefix)
        PacketUtil.setTeamSuffix(ScoreBoard, Suffix)
        PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
        PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(PLayer))
        PacketUtil.setTeamMode(ScoreBoard, 2)
        PacketUtil.setNameTagVisible(ScoreBoard,ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS)
        PacketUtil.setTEAM_COLISIONRULE(ScoreBoard,ScoreboardTeamBase.EnumTeamPush.ALWAYS)

        sendpacket(ScoreBoard, Receiver)
    }

    fun UpdateTeamSafe(PLayer: String, NameTeam: String, Prefix: String, Suffix: String, vararg Receiver: Player) {
        Validate.noNullElements(Receiver, "Recebidores NULOS")
        Validate.notNull(PLayer, "Player NULO !")

        val ScoreBoard = PacketPlayOutScoreboardTeam()

        PacketUtil.setTeamName(ScoreBoard, PLayer)
        PacketUtil.setTeamDisplayName(ScoreBoard, NameTeam)
        PacketUtil.setTeamPrefix(ScoreBoard, Prefix)
        PacketUtil.setTeamSuffix(ScoreBoard, Suffix)
        PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
        PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(PLayer))
        PacketUtil.setTeamMode(ScoreBoard, 2)
        PacketUtil.setNameTagVisible(ScoreBoard,ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS)
        PacketUtil.setTEAM_COLISIONRULE(ScoreBoard,ScoreboardTeamBase.EnumTeamPush.ALWAYS)

        sendpacket(ScoreBoard, *Receiver)
    }

    fun SendScoreBoard(Player: Player){

        val Scoreboard = Scoreboard()
        val Base = ScoreboardObjective(Scoreboard,"§6Ouros",ScoreboardBaseCriteria("dummy"))
        val ScoreScore = ScoreboardScore(Scoreboard,Base, Player.name)
        ScoreScore.score = 0

        val Packet = PacketPlayOutScoreboardObjective(Base,0)
        val Display = PacketPlayOutScoreboardDisplayObjective(2,Base)

        val Score = PacketPlayOutScoreboardScore(ScoreScore)

        Bukkit.getOnlinePlayers().filter{it.name != Player.name}.forEach { P ->
            run {
                (P as CraftPlayer).handle.playerConnection.sendPacket(Packet)
                P.handle.playerConnection.sendPacket(Display)
                P.handle.playerConnection.sendPacket(Score)
            }
        }
    }

    fun UpdateSafeScoreBoard(Player: Player, NewScore: Int, Receiver: Collection<Player>){
        val ScoreBoard = Scoreboard()

        val ScoreBase = ScoreboardObjective(ScoreBoard,"§6Ouros",ScoreboardBaseCriteria("dummy"))

        val ScoreBoardScore = ScoreboardScore(ScoreBoard,ScoreBase,Player.name)
        ScoreBoardScore.score = NewScore

        val Objective = PacketPlayOutScoreboardObjective(ScoreBase,0)
        val Display  = PacketPlayOutScoreboardDisplayObjective(2,ScoreBase)

        val Score = PacketPlayOutScoreboardScore(ScoreBoardScore)

        Receiver.forEach {
            (it as CraftPlayer).handle.playerConnection.sendPacket(Objective)
            it.handle.playerConnection.sendPacket(Display)
            it.handle.playerConnection.sendPacket(Score)
         }

    }

    fun UpdateSafeScoreBoard(Player: Player, NewScore: Int, vararg Receiver: Player){

        val ScoreBoard = Scoreboard()

        val ScoreBase = ScoreboardObjective(ScoreBoard,"§6Ouros",ScoreboardBaseCriteria("dummy"))

        val ScoreBoardScore = ScoreboardScore(ScoreBoard,ScoreBase,Player.name)
        ScoreBoardScore.score = NewScore

        val Objective = PacketPlayOutScoreboardObjective(ScoreBase,1)
        val Display  = PacketPlayOutScoreboardDisplayObjective(2,ScoreBase)

        val Score = PacketPlayOutScoreboardScore(ScoreBoardScore)

        Receiver.forEach {
            (it as CraftPlayer).handle.playerConnection.sendPacket(Objective)
            it.handle.playerConnection.sendPacket(Display)
            it.handle.playerConnection.sendPacket(Score)
        }
    }

    fun UpdateSafeScoreBoard(player: Player?, scoreboardScore: ScoreboardScore, filter: List<Player>) {
        val Scoreboard = Scoreboard()
        val Base = ScoreboardObjective(Scoreboard,"§6Ouros", ScoreboardBaseCriteria("dummy"))

        val ScoreScore = ScoreboardScore(Scoreboard, Base, player?.name)
        ScoreScore.score = scoreboardScore.score

        //val Packet = PacketPlayOutScoreboardObjective(Base, 1)
        val Packet2 = PacketPlayOutScoreboardObjective(Base, 0)
        val Display = PacketPlayOutScoreboardDisplayObjective(2, Base)

        val Score = PacketPlayOutScoreboardScore(ScoreScore)


        filter.forEach {
            (it as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutScoreboardScore(player?.name))
            //it.handle.playerConnection.sendPacket(Packet)
            it.handle.playerConnection.sendPacket(Packet2)
            it.handle.playerConnection.sendPacket(Display)
            it.handle.playerConnection.sendPacket(Score)
        }
    }




    /**
     * Envia um packet a um determinado player
     * @param packet o packet a ser enviado
     * *
     * @param Player o player que receberá o packet
     */

    private fun sendpacket(packet: PacketPlayOutScoreboardTeam, vararg Player: Player) {
        Arrays.asList(*Player).forEach { P -> (P as CraftPlayer).handle.playerConnection.sendPacket(packet) }
    }

    private fun sendpacket(packet: PacketPlayOutScoreboardTeam, Player: Collection<Player>) {
        Player.forEach { P -> (P as CraftPlayer).handle.playerConnection.sendPacket(packet) }
    }

    companion object {
        val instance = PacketControl()
    }

}
