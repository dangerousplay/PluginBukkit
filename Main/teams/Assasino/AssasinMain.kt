package Main.teams.Assasino

import Main.ItemsEdit
import Main.teams.Detetive.detetiveMain
import Main.teams.PacketControl
import br.com.tlcm.cc.API.CoreDPlayer
import Main.Main
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import Main.Util.PacketUtil
import net.minecraft.server.v1_12_R1.ScoreboardTeamBase

import java.util.*
import java.util.stream.Collectors

/**
 * Created by GMDAV on 18/04/2017.
 */
class AssasinMain {
    private val player = HashSet<String>()

    private val AssasinosNomes = ArrayList<String>()

    @Synchronized fun setPlayer(Player: Collection<String>) {


        var ScoreBoard: PacketPlayOutScoreboardTeam

        this.player.addAll(Player)
        this.AssasinosNomes.addAll(Player)

        //        removeScoreBoard(player);

        for (P in Player) {
            Bukkit.getPlayer(P).sendMessage("§4§lVocê é um assasino!")
            Bukkit.getPlayer(P).inventory.addItem(ItemsEdit.laminadoran)
            Bukkit.getServer().logger.info(P + " assasino")

            Bukkit.getPlayer(P).sendMessage("                             ")
            Bukkit.getPlayer(P).sendMessage("§a§lObjetivos:")
            Bukkit.getPlayer(P).sendMessage("                             ")
            Bukkit.getPlayer(P).sendMessage("§f- Use sua espada para matar os inocentes")
            Bukkit.getPlayer(P).sendMessage("§f- Proteja os assasinos")
            Bukkit.getPlayer(P).sendMessage("§f- Engane os inocentes com o seu voto de desconfiar")
            Bukkit.getPlayer(P).sendMessage("§f- Mate o detetive")

            ScoreBoard = PacketPlayOutScoreboardTeam()

            PacketUtil.setTeamName(ScoreBoard, P)
            PacketUtil.setTeamDisplayName(ScoreBoard, P)
            PacketUtil.setTeamPrefix(ScoreBoard, "§4§lAssasino §c")
            PacketUtil.setTeamSuffix(ScoreBoard, " §a§oAmigo")
            PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
            PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(P))
            PacketUtil.setTeamMode(ScoreBoard, 0)

            (Bukkit.getPlayer(P) as CraftPlayer).handle.playerConnection.sendPacket(ScoreBoard)

        }


        for (P in Bukkit.getServer().onlinePlayers) {

            ScoreBoard = PacketPlayOutScoreboardTeam()

            if (detetiveMain.insance.hasplayerinside(P.name)) {
                PacketUtil.setTeamName(ScoreBoard, P.name)
                PacketUtil.setTeamDisplayName(ScoreBoard, P.name)
                PacketUtil.setTeamPrefix(ScoreBoard, "§1§lDetetive §9")
                PacketUtil.setTeamSuffix(ScoreBoard, " §c§oInimigo")
                PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
                PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(P.name))
                PacketUtil.setTeamMode(ScoreBoard, 0)
                PacketUtil.setNameTagVisible(ScoreBoard, ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS)
                PacketUtil.setTEAM_COLISIONRULE(ScoreBoard, ScoreboardTeamBase.EnumTeamPush.ALWAYS)


                for (R in player) {
                    if (!P.name.equals(R, ignoreCase = true))
                        if (!PacketControl.instance.PlayerContainsPacket(R, P.name)) {
                            PacketControl.instance.add(R, P.name)
                            (Bukkit.getPlayer(R) as CraftPlayer).handle.playerConnection.sendPacket(ScoreBoard)
                        }
                }

                continue
            }

            if (AssasinMain.instance.hasplayerinside(P.name)) {
                PacketUtil.setTeamName(ScoreBoard, P.name)
                PacketUtil.setTeamDisplayName(ScoreBoard, P.name)
                PacketUtil.setTeamPrefix(ScoreBoard, "§4§lAssasino §c")
                PacketUtil.setTeamSuffix(ScoreBoard, " §a§oAmigo")
                PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
                PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(P.name))
                PacketUtil.setTeamMode(ScoreBoard, 0)
                PacketUtil.setNameTagVisible(ScoreBoard,ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS)
                PacketUtil.setTEAM_COLISIONRULE(ScoreBoard,ScoreboardTeamBase.EnumTeamPush.ALWAYS)


                for (R in player) {
                    if (!P.name.equals(R, ignoreCase = true))
                        if (!PacketControl.instance.PlayerContainsPacket(R, P.name)) {
                            PacketControl.instance.add(R, P.name)
                            (Bukkit.getPlayer(R) as CraftPlayer).handle.playerConnection.sendPacket(ScoreBoard)
                        }
                }


                continue
            }

            PacketUtil.setTeamName(ScoreBoard, P.name)
            PacketUtil.setTeamDisplayName(ScoreBoard, P.name)
            PacketUtil.setTeamPrefix(ScoreBoard, "§2§lInocente §a")
            PacketUtil.setTeamSuffix(ScoreBoard, " §c§oInimigo")
            PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
            PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(P.name))
            PacketUtil.setTeamMode(ScoreBoard, 0)
            PacketUtil.setNameTagVisible(ScoreBoard,ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS)
            PacketUtil.setTEAM_COLISIONRULE(ScoreBoard,ScoreboardTeamBase.EnumTeamPush.ALWAYS)


            for (R in player) {
                if (!P.name.equals(R, ignoreCase = true))
                    if (!PacketControl.instance.PlayerContainsPacket(R, P.name)) {
                        PacketControl.instance.add(R, P.name)
                        (Bukkit.getPlayer(R) as CraftPlayer).handle.playerConnection.sendPacket(ScoreBoard)
                    }
            }

        }

        object : BukkitRunnable() {
            override fun run() {
                synchronized(player) {
                    player.removeIf { P -> Bukkit.getPlayer(P) == null || CoreDPlayer.isSpectator(P) }
                }
            }
        }.runTaskTimer(Main.instance, 0, 20)


    }

    fun getPlayer(): Set<String> {
        return player
    }

    fun hasplayerinside(player: String): Boolean {
        return this.player.contains(player)
    }

    fun removePlayer(player: String) {
        this.player.remove(player)
    }

//    private fun sendToall(Players: Collection<String>, packet: Packet) {
//        Players.forEach { P -> (Bukkit.getPlayer(P) as CraftPlayer).handle.playerConnection.sendPacket(packet) }
//    }
//
//    private fun SendIfNotPresent(Players: Collection<String>, Player: String, packet: Packet) {
//        Players.filter {it.equals(Player,ignoreCase = true)}.forEach { P -> (Bukkit.getPlayer(P) as CraftPlayer).handle.playerConnection.sendPacket(packet) }
//    }
//
//    private fun removeScoreBoard(Players: Collection<String>) {
//
//        var ScoreBoard: PacketPlayOutScoreboardTeam
//
//        for (P in Bukkit.getServer().onlinePlayers) {
//
//            ScoreBoard = PacketPlayOutScoreboardTeam()
//
//            if (detetiveMain.insance.hasplayerinside(P.name)) {
//                PacketUtil.setTeamName(ScoreBoard, P.name)
//                PacketUtil.setTeamDisplayName(ScoreBoard, P.name)
//                PacketUtil.setTeamPrefix(ScoreBoard, "§1§lDetetive §r")
//                PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
//                PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(P.name))
//                PacketUtil.setTeamMode(ScoreBoard, 1)
//
//                SendIfNotPresent(Players, P.name, ScoreBoard)
//                continue
//            }
//
//            if (Victimain.instance.hasplayerinside(P.name)) {
//                PacketUtil.setTeamName(ScoreBoard, P.name)
//                PacketUtil.setTeamDisplayName(ScoreBoard, P.name)
//                PacketUtil.setTeamPrefix(ScoreBoard, "§6§lSuspeito §e")
//                PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
//                PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(P.name))
//                PacketUtil.setTeamMode(ScoreBoard, 1)
//
//                SendIfNotPresent(Players, P.name, ScoreBoard)
//            }
//        }
//
//    }

    fun containsignorecase(player: String): Optional<String> {
        val Set = this.player.stream().filter { P -> P.equals(player, ignoreCase = true) }.collect(Collectors.toList())
        return Optional.ofNullable(if (Set.isEmpty()) null else Set[0])
    }

    val bukkitPlayers: MutableList<Player>?
        get() = player.stream().map{Bukkit.getPlayer(it)}.collect(Collectors.toList())

    override fun toString(): String {
        val Retorno = StringBuilder()

        for (i in AssasinosNomes.indices) {

            val Size = AssasinosNomes.size - 1
            if (i == AssasinosNomes.size - 1) {
                Retorno.append(AssasinosNomes[i]).append(".")
                continue
            }

            if (i == AssasinosNomes.size - 2) {
                Retorno.append(AssasinosNomes[i]).append(" e ")
                continue
            }

            Retorno.append(AssasinosNomes[i]).append(", ")
        }

        return Retorno.toString()
    }

    companion object {
        val instance = AssasinMain()
    }
}
