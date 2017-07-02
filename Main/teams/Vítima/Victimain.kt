package Main.teams.Vítima

import Main.ItemsEdit
import Main.Main
import Main.teams.Detetive.detetiveMain
import Main.teams.PacketControl
import Main.teams.PlayerType
import br.com.tlcm.cc.API.CoreDPlayer
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import Main.Util.PacketUtil
import net.minecraft.server.v1_12_R1.ScoreboardTeamBase

import java.util.stream.Collectors

/**
 * Created by GMDAV on 18/04/2017.
 */
class Victimain {
    private val playersvitimas = HashSet<String>()
    private val UsedItems = Hashtable<String, List<ItemsEdit.ItemsToConsume>>()
    private val Revelados = Hashtable<String, PlayerType>()


    val teams: Set<String>
        @Synchronized get() = playersvitimas


    @Synchronized fun setTeams(player: Collection<String>) {
        playersvitimas.addAll(player)
        playersvitimas.forEach { P ->
            Bukkit.getPlayer(P).sendMessage("§a§lVocê é um inocente")
            Bukkit.getPlayer(P).sendMessage("                             ")
            Bukkit.getPlayer(P).sendMessage("§a§lObjetivos:")
            Bukkit.getPlayer(P).sendMessage("                             ")
            Bukkit.getPlayer(P).sendMessage("§f- Descubra e condene os assasinos")
            Bukkit.getPlayer(P).sendMessage("§f- Proteger o detevive")
            Bukkit.getPlayer(P).sendMessage("§f- Colete ouros para fazer um arco")
            Bukkit.getPlayer(P).sendMessage("§f- Use o ouro para fazer flechas")


            Bukkit.getServer().logger.info(P + " inocente")
        }

        val random = Random()

        val Escolher = ArrayList(playersvitimas)
        val Escolhidos = ArrayList<Player>()

        for (i in 0..2) {

            if (Escolher.size == 0) {
                break
            }

            val value = if (Escolher.size == 0) 0 else Escolher.size - 1

            if (value == 0) {
                break
            }

            val index = random.nextInt(value)
            Escolhidos.add(Bukkit.getPlayer(Escolher[index]))
            Escolher.removeAt(index)
        }

        var ScoreBoard: PacketPlayOutScoreboardTeam

        val PlayersTotal = Bukkit.getOnlinePlayers()

        for (P in PlayersTotal) {
            ScoreBoard = PacketPlayOutScoreboardTeam()

            if (detetiveMain.insance.hasplayerinside(P.name)) {
                PacketUtil.setTeamName(ScoreBoard, P.name)
                PacketUtil.setTeamDisplayName(ScoreBoard, P.name)
                PacketUtil.setTeamPrefix(ScoreBoard, "§1§lDetetive §9")
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
            PacketUtil.setTeamPrefix(ScoreBoard, "§6§lSuspeito §e")
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



        for (P in player) {

            ScoreBoard = PacketPlayOutScoreboardTeam()

            PacketUtil.setTeamName(ScoreBoard, P)
            PacketUtil.setTeamDisplayName(ScoreBoard, P)
            PacketUtil.setTeamPrefix(ScoreBoard, "§2§lInocente §a")
            PacketUtil.setTeamSuffix(ScoreBoard, " §a§oAmigo")
            PacketUtil.setTeamFriendlyFire(ScoreBoard, 1)
            PacketUtil.addPlayer(ScoreBoard, Bukkit.getPlayer(P))
            PacketUtil.setTeamMode(ScoreBoard, 0)
            PacketUtil.setNameTagVisible(ScoreBoard, ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS)
            PacketUtil.setTEAM_COLISIONRULE(ScoreBoard, ScoreboardTeamBase.EnumTeamPush.ALWAYS)

            if (!PacketControl.instance.PlayerContainsPacket(P, P)) {
                PacketControl.instance.add(P, P)
                (Bukkit.getPlayer(P) as CraftPlayer).handle.playerConnection.sendPacket(ScoreBoard)
            }
        }



        Escolhidos.forEach { P ->
            P.sendMessage("§aVocê ganhou o §bArco da Salvação §a:)")
            P.inventory.addItem(ItemsEdit.arcoSalvacao)
            P.inventory.addItem(ItemStack(Material.ARROW))
        }

        object : BukkitRunnable() {
            override fun run() {
                synchronized(playersvitimas) {
                    playersvitimas.removeIf { P -> Bukkit.getPlayer(P) == null || CoreDPlayer.isSpectator(P) }
                }
            }
        }.runTaskTimer(Main.instance, 0, 20)

    }


    val teamsSize: Int
        get() = playersvitimas.size

    fun hasplayerinside(player: String): Boolean {
        return playersvitimas.contains(player)
    }

    fun containsignorecase(player: String): Optional<String> {
        val Set = this.playersvitimas.stream().filter { P -> P.equals(player, ignoreCase = true) }.collect(Collectors.toList())

        return Optional.ofNullable(if (Set.isEmpty()) null else Set[0])
    }

    fun putPlayer(player: String) {
        playersvitimas.add(player)
    }

    fun removePlayer(player: String) {
        playersvitimas.remove(player)
    }

    val usedItems: Map<String, List<ItemsEdit.ItemsToConsume>>
        get() = UsedItems

    fun addUsedItems(player: String, Item: ItemsEdit.ItemsToConsume) {
        if (UsedItems.containsKey(player)) {
            UsedItems.forEach { K, V ->
//                if (K.equals(player, ignoreCase = true)) {
//                    V.aa
//                    V.add(Item)
//                    UsedItems.replace(K, V)
//                }
            }

        } else {
            val Return = ArrayList<ItemsEdit.ItemsToConsume>()
            Return.add(Item)
            this.UsedItems.put(player, Return)
        }

    }

    val bukkitPlayers: Set<Player>?
        get() = playersvitimas.stream().map { Bukkit.getPlayer(it) }.collect(Collectors.toSet())

//    private fun SendToOthers(Players: Collection<String>, Player: String, Packet: Packet) {
//        Players.stream().filter { P -> !Player.equals(P, ignoreCase = true) }
//                .forEach { P -> (Bukkit.getPlayer(P) as CraftPlayer).handle.playerConnection.sendPacket(Packet) }
//    }

    fun addrevelado(Revelado: String, Type: PlayerType) {
        Revelados.put(Revelado, Type)
    }

    fun isRevelado(player: String, revelado: String): Boolean {
        return Revelados.containsKey(player)
    }


    override fun toString(): String {
        val Retorno = StringBuilder()
        playersvitimas.forEach { P -> Retorno.append(P).append(", ") }
        return Retorno.toString()
    }

    companion object {
        val instance = Victimain()
    }

}
