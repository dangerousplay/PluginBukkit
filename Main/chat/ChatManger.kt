package Main.chat

import Main.Util.ChatUtil
import Main.Votacao.Manager
import Main.teams.Assasino.AssasinMain
import Main.teams.Detetive.detetiveMain
import Main.teams.PlayerType
import Main.teams.Vítima.Victimain
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import net.minecraft.server.v1_12_R1.IChatBaseComponent
import net.minecraft.server.v1_12_R1.PacketPlayOutChat
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * Created by GMDAV on 14/06/2017.
 */
open class ChatManger : Listener {


    @EventHandler(ignoreCancelled = true)
    open fun PlayerChat(Chat : AsyncPlayerChatEvent){
        if(CoreD.isRunning()){

            if(CoreDPlayer.isSpectator(Chat.player)){
                return
            }

            Chat.isCancelled = true

            var TypeOF: PlayerType
            val isRevelado = detetiveMain.insance.isRevelado(Chat.player.name)
            var ChatPacketAssin: IChatBaseComponent? = null
            val ChatSabotador = Chat.message.contains("@sab",ignoreCase = true)

            var ChatPacketInocente: IChatBaseComponent? = null



            if(ChatSabotador && AssasinMain.instance.hasplayerinside(Chat.player.name)){
                ChatPacketAssin = ChatUtil.chatHover("§4§o[A] ${Chat.player.name}: ", "§4${Chat.player.name} é um assasino") + "§4${Chat.message}"

                val PacketFinalizeChat = PacketPlayOutChat(ChatPacketAssin)

                AssasinMain.instance.bukkitPlayers!!.forEach {
                    (it as CraftPlayer).handle.playerConnection.sendPacket(PacketFinalizeChat)
                }
                return
            } else {

                    if(Victimain.instance.teams.contains(Chat.player.name)) {
                        TypeOF = PlayerType.Vitima
                        ChatPacketAssin = ChatUtil.chatHover("§2[V]§a ${Chat.player.name}: ", "§a${Chat.player.name} é um inocente!") + Chat.message
                    }

                    if(AssasinMain.instance.getPlayer().contains(Chat.player.name)) {
                        TypeOF = PlayerType.Assasino
                        ChatPacketAssin = ChatUtil.chatHover("§4[A]§c ${Chat.player.name}: ", "§4${Chat.player.name} é um assasino!") + Chat.message
                    }

                    if(detetiveMain.insance.getPlayer() != null && detetiveMain.insance.getPlayer()!!.contains(Chat.player.name)) {
                        TypeOF = PlayerType.Detetive
                        ChatPacketAssin = ChatUtil.chatHover("§1[D]§b ${Chat.player.name}: ", "§1${Chat.player.name} é um detetive!") + Chat.message
                    }


                val PacketFinalizeChat = PacketPlayOutChat(ChatPacketAssin)

                AssasinMain.instance.bukkitPlayers!!.forEach {
                    (it as CraftPlayer).handle.playerConnection.sendPacket(PacketFinalizeChat)
                }
            }

            if(isRevelado) {
                val PacketFinalizeChat = PacketPlayOutChat(ChatPacketAssin)

                val TodosInocentes = if (detetiveMain.insance.getPlayer() != null) Victimain.instance.bukkitPlayers!! + detetiveMain.insance.getBukkitPlayers() else Victimain.instance.bukkitPlayers

                TodosInocentes!!.forEach { (it as CraftPlayer).handle.playerConnection.sendPacket(PacketFinalizeChat) }
            }else{
                if(Manager.instance.getPlayersDesconfiados().contains(Chat.player.name)){
                    if(Manager.instance.getPlayersDesconfiados()[Chat.player.name]!! > 0){
                        ChatPacketInocente = ChatUtil.chatHover("§5[S] §d${Chat.player.name}§r: ", "§d${Chat.player.name} §5é um Suspeito Desconfiado!") + Chat.message
                    }
                    if(Manager.instance.getPlayersDesconfiados()[Chat.player.name]!! == 0){
                        ChatPacketInocente = ChatUtil.chatHover("§6[S] §e${Chat.player.name}§r: ", "§e${Chat.player.name} §6é um Suspeito!") + Chat.message
                    }
                    if(Manager.instance.getPlayersDesconfiados()[Chat.player.name]!! < 0){
                        ChatPacketInocente = ChatUtil.chatHover("§3[S] §b${Chat.player.name}§r: ", "§b${Chat.player.name} §bé um Suspeito Confiável!") + Chat.message
                    }

                    val PacketFinalizeChat = PacketPlayOutChat(ChatPacketInocente)

                    val TodosInocentes = if (detetiveMain.insance.getPlayer() != null) Victimain.instance.bukkitPlayers!! + detetiveMain.insance.getBukkitPlayers() else Victimain.instance.bukkitPlayers

                    TodosInocentes!!.forEach { (it as CraftPlayer).handle.playerConnection.sendPacket(PacketFinalizeChat) }

                }else{
                    ChatPacketInocente = ChatUtil.chatHover("§6[S] §e${Chat.player.name}§r: ", "§e${Chat.player.name} §6é um Suspeito!") + Chat.message

                    val PacketFinalizeChat = PacketPlayOutChat(ChatPacketInocente)

                    val TodosInocentes = if (detetiveMain.insance.getPlayer() != null) Victimain.instance.bukkitPlayers!! + detetiveMain.insance.getBukkitPlayers() else Victimain.instance.bukkitPlayers

                    TodosInocentes!!.forEach { (it as CraftPlayer).handle.playerConnection.sendPacket(PacketFinalizeChat) }
                }
            }
        }
    }

    operator infix fun IChatBaseComponent.plus(Other: IChatBaseComponent) : IChatBaseComponent{
        this.a(Other.text)
        return this
    }

    operator infix fun IChatBaseComponent.plus(Other: String) : IChatBaseComponent{
        this.a(Other)
        return this
    }


}