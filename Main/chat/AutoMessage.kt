package Main.chat

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*

/**
 * Created by GMDAV on 14/06/2017.
 */
object AutoMessage {
    private val TimeToSend : Long = 60


        private val Messages = arrayListOf(
                "§6[!] Use /desconfiar [d] <player> para desconfiar de um player suspeito.",
                "§6[!] Use /inocentar [i] <player> para dar um voto de confiança a um suspeito.",
                "§6[!] Use /neutralizar [n] <player> para remover os seus votos de um player.",
                "§6[!] Use /desconfiar [d] <player> para desconfiar de um player suspeito.",
                "§6[!] Você pode explodir os outros player usando a TNT.",
                "§6[!] O Detetive pode descobrir quem é o assasino testando ele com a tesoura.",
                "§6[!] O Detetive pode analisar um corpo morto e descobrir quem o matou.",
                "§6[!] Inocentes podem craftar o Arco da Salvação que pode matar apenas com uma flecha!",
                "§6[!] Usando o ouro, você pode craftar armaduras e ferramentas."
        )

        private var RestMessages = Messages.toMutableList()

        private var Runnable: BukkitTask? = null

        fun start(){
            Runnable = object : BukkitRunnable(){
                override fun run(){
                    sendNext()
                }
            }.runTaskTimer(Main.Main.instance,0,20*TimeToSend)
        }

        fun stop(){
            Runnable?.cancel()
            Runnable = null
        }

        private fun sendNext(){
            if(RestMessages.isEmpty() || RestMessages.size <= 1){
                RestMessages = Messages.toMutableList()
            }

            val Random = Random().nextInt(RestMessages.size-1)
            Bukkit.getServer().broadcastMessage(RestMessages[Random])
            RestMessages.removeAt(Random)
        }
}