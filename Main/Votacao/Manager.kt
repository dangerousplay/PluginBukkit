package Main.Votacao

import Main.Runnable.BukkitRunnable
import Main.teams.Assasino.AssasinMain
import Main.teams.Detetive.detetiveMain
import Main.teams.PacketControl
import Main.teams.Vítima.Victimain
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import Main.Main
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Score

import java.util.*
import java.util.stream.Collectors

/**
 * Created by GMDAV on 18/05/2017.
 */
class Manager private constructor() {
    private val PlayersDesconfiados = Hashtable<String, Int>()
    private val PlayersCondenaram = ArrayList<String>()
    private val PlayersInocentaram = ArrayList<String>()


    private val CondenouQuem = Hashtable<String, MutableCollection<String>>()
    private val InocentouQuem = Hashtable<String, MutableCollection<String>>()

    private val TempoCondenar = 30
    private val TempoParaVotar = 3

    /**
     * Inicia o contador
     */

    fun start() {
        object : BukkitRunnable {
            override fun run() {
                atualizarScoreBoard()
            }
        }.runTaskTimer(Main.instance!!, 0, 10)
    }



    /**
     * Atualiza o Scoreboard com os acusados
     */

    @Synchronized fun atualizarScoreBoard() {
        if (CoreD.isRunning()) {

            for (online in Bukkit.getServer().onlinePlayers) {
                var obj: Objective? = online.scoreboard.getObjective(DisplaySlot.SIDEBAR)

                if (obj == null) {
                    obj = online.scoreboard.registerNewObjective("status", "dummy")
                    obj!!.displayName = "§4§lSuspeitos"
                    obj.displaySlot = DisplaySlot.SIDEBAR
                }

                val FinalObj = obj

                synchronized(PlayersDesconfiados) {

                    PlayersDesconfiados.forEach { K, V ->
                        if (V > 0) {
                            FinalObj.getScore(Bukkit.getOfflinePlayer("§c" + K)).score = V!!
                            return@forEach
                        }
                        if (detetiveMain.insance.isRevelado(K) || Bukkit.getPlayer(K) == null || CoreDPlayer.isSpectator(K)) {
                            online.scoreboard.resetScores(Bukkit.getOfflinePlayer("§c" + K))
                            online.scoreboard.resetScores(Bukkit.getOfflinePlayer("§a" + K))
                            PlayersDesconfiados.remove(K)
                        } else {
                            FinalObj.getScore(Bukkit.getOfflinePlayer("§a" + K)).score = V!!
                        }
                    }
                }

            }

            val Allplayers = ArrayList<Player>(Victimain.instance.bukkitPlayers)
            if (detetiveMain.insance.getPlayer() != null) {
                Allplayers.addAll(detetiveMain.insance.getBukkitPlayers())
            }

            synchronized(PlayersDesconfiados) {

                PlayersDesconfiados.forEach { K, V ->

                    if (detetiveMain.insance.isRevelado(K) || Bukkit.getPlayer(K) == null || CoreDPlayer.isSpectator(K)) {
                        PlayersDesconfiados.remove(K)
                        return@forEach
                    }

                    if (V > 0) {
                        PacketControl.instance.UpdateTeamSafe(K, "§5§lSuspeito §e", " §d§oDesconfiado", Allplayers)
                        return@forEach
                    }
                    if (V == 0) {
                        PacketControl.instance.UpdateTeamSafe(K, "§6§lSuspeito §e", "", Allplayers)
                    } else {
                        PacketControl.instance.UpdateTeamSafe(K, "§3§lSuspeito §e", " §b§oConfiável", Allplayers)
                    }
                }
            }
        }

    }

    fun atualizarScoreBoard(player: String) {
        Bukkit.getServer().onlinePlayers.forEach { online -> online.scoreboard.resetScores(Bukkit.getOfflinePlayer(player)) }
    }

    /**
     * Remove uma coleção de players do ScoreBoard
     * @param player os players a serem removidos
     */

    fun atualizarScoreBoard(player: Collection<String>) {

        Bukkit.getServer().onlinePlayers.forEach { online -> player.forEach { P -> online.scoreboard.resetScores(Bukkit.getOfflinePlayer(P)) } }

        removePlayer(player)
    }

    /**
     * Adiciona um player ao ScoreBoard.
     * @param Player o player a ser adicionado
     */

    @Synchronized fun addPlayer(Player: String) {

        if (PlayersDesconfiados.containsKey(Player)) {
            PlayersDesconfiados.replace(Player, PlayersDesconfiados[Player]!! + 1)
        } else {
            PlayersDesconfiados.put(Player, 1)
        }

    }

    /**
     * Remove um player da lista de quem desconfiou
     * @param Player o player a ser removido
     */

    @Synchronized fun removePlayer(Player: String) {
        if (PlayersDesconfiados.containsKey(Player)) {
            PlayersDesconfiados.replace(Player, PlayersDesconfiados[Player]!! - 1)
        } else {
            PlayersDesconfiados.put(Player, -1)
        }
    }


    @Synchronized fun removePlayer(Player: Collection<String>) {
        PlayersCondenaram.removeAll(Player)
    }


    @Synchronized fun addPlayerUsed(player: String) {
        this.PlayersCondenaram.add(player)
        object : BukkitRunnable {
            override fun run() {
                PlayersCondenaram.remove(player)
            }
        }.runTaskLater(Main.instance!!, 20 * TempoParaVotar)

    }

    @Synchronized fun addDesconfiou(Playe: String, Voto: String) {
        if (CondenouQuem.containsKey(Playe)) {
            val Condeou = CondenouQuem[Playe]!!
            Condeou.add(Voto)

            CondenouQuem.replace(Playe, Condeou)
        }else{
            val M = mutableListOf(Voto)
            CondenouQuem.put(Playe,M)
        }
    }

    @Synchronized fun addconfiou(Playe: String, Voto: String) {
        if (InocentouQuem.containsKey(Playe)) {
            val Condeou = InocentouQuem[Playe]!!
            Condeou.add(Voto)
            InocentouQuem.replace(Playe, Condeou)
        }else{
            val M = mutableListOf(Voto)
            InocentouQuem.put(Playe,M)
        }
    }

    @Synchronized fun desconfiou(Player: String, Desconfiado: String): Boolean {
        return CondenouQuem.containsKey(Player) && CondenouQuem[Player]!!.filter {P -> P.equals(Desconfiado,ignoreCase = true)}.isNotEmpty()
    }

    @Synchronized fun inocentou(Player: String, Inocente: String): Boolean {
        return InocentouQuem.containsKey(Player) && InocentouQuem[Player]!!.filter {P -> P.equals(Inocente,ignoreCase = true)}.isNotEmpty()
    }

    @Synchronized fun removeVote(Player: String, Voto: String) {
        if (inocentou(Player, Voto)) {
            val Remover = this.InocentouQuem[Player]!!
            Remover.remove(Voto)
            InocentouQuem.replace(Player, Remover)

            addPlayer(Voto)
        } else {
            val Remover = this.CondenouQuem[Player]!!
            Remover.remove(Voto)
            CondenouQuem.replace(Player, Remover)

            removePlayer(Voto)
        }

    }


    fun containsPlayerConde(player: String): Boolean {
        return this.PlayersCondenaram.contains(player)
    }

    fun containsPlayerInoce(player: String): Boolean {
        return this.PlayersInocentaram.contains(player)
    }

    fun containsVote(player: String, voto: String): Boolean {
        return desconfiou(player, voto) || inocentou(player, voto)
    }

    fun getPlayersDesconfiados() : Hashtable<String,Int> = PlayersDesconfiados




    companion object {

        val instance = Manager()
    }
}
