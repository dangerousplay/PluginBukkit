package Main.listeners

import Main.Util.PacketVersion
import Main.teams.PacketControl
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_12_R1.CraftServer
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent

/**
 * Created by GMDAV on 18/05/2017.
 */
open class PlayerJoin : Listener {

    @EventHandler
    open fun PlayerJoin(event: PlayerJoinEvent) {
        event.player.sendMessage("§a§oEste Minigame é semelhante ao detetive, os assasinos precisam matar as vítimas" + " e o detetive para ganhar, as vítimas precisam matar os assasinos.")

        event.player.sendMessage("§cUse /desconfiar [player] para marcar um player como suspeito.")
        event.player.sendMessage("§bUse /inocentar [player] para deixar um voto de confiança.")
        event.player.sendMessage("§3Use /neutralizar [player] para remove o seu voto.")



    }

}
