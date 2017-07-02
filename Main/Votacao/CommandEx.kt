package Main.Votacao

import Main.teams.Assasino.AssasinMain
import Main.teams.Detetive.detetiveMain
import Main.teams.PacketControl
import Main.teams.PlayerType
import Main.teams.Vítima.Victimain
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by GMDAV on 18/05/2017.
 */
class CommandEx : CommandExecutor {
    /** Será executado o comando desconfiar para depois ser condenado no Manager
     * @param Sender é quem enviou o comando.
     * *
     * @param cmd é o comando enviado.
     * *
     * @param label é o nome do comando.
     * *
     * @param args são os argumentos enviados.
     * *
     * @return é o tipo de retorno, false ou true.
     */

    override fun onCommand(Sender: CommandSender, cmd: org.bukkit.command.Command, label: String, args: Array<String>): Boolean {
        if (CoreD.isRunning()) {

            if (Manager.instance.containsPlayerConde(Sender.name)) {
                Sender.sendMessage("§cAguarde para desconfiar novamente!")
                return false
            }

            if (args.size != 1) {
                Sender.sendMessage("§cuse /desconfiar [player]")
                return false
            }

            if (detetiveMain.insance.hasplayerinside(args[0])) {
                Sender.sendMessage("Você não pode desconfiar do detetive")
                return false
            }

            if (Sender.name.equals(args[0], ignoreCase = true)) {
                Sender.sendMessage("§cVocê não pode desconfiar de si mesmo!")
                return false
            }

            if (CoreDPlayer.isSpectator(Sender.name)) {
                Sender.sendMessage("§cVocê não pode desconfiar como espectador")
                return false
            }

            if (detetiveMain.insance.isRevelado(args[0])) {
                Sender.sendMessage("§cEste player já foi revelado!")
                return false
            }

            if (AssasinMain.instance.containsignorecase(args[0]).isPresent || Victimain.instance.containsignorecase(args[0]).isPresent) {

                val player: String
                val Type: PlayerType


                if (AssasinMain.instance.containsignorecase(args[0]).isPresent) {
                    player = AssasinMain.instance.containsignorecase(args[0]).get()
                    Type = PlayerType.Assasino
                } else {
                    player = Victimain.instance.containsignorecase(args[0]).get()
                    Type = PlayerType.Vitima
                }

                if (Manager.instance.desconfiou(Sender.name, player)) {
                    Sender.sendMessage("§cVocê já desconfiou do(a) " + player)
                    return false
                }



                AssasinMain.instance.bukkitPlayers!!.forEach { P ->

                    if (AssasinMain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {

                            P.sendMessage("§c" + Sender.name + "§a acha que §c" + player + " §cé um assasino")
                        } else {
                            P.sendMessage("§c" + Sender.name + "§a acha que §a" + player + " §cé um assasino")
                        }
                    }

                    if (Victimain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§a" + Sender.name + "§a acha que §c" + player + " §cé um assasino")
                        } else {
                            P.sendMessage("§a" + Sender.name + "§a acha que §a" + player + " §cé um assasino")
                        }
                    }

                    if (detetiveMain.insance.hasplayerinside(Sender.name)) {
                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§9" + Sender.name + "§a acha que §c" + player + " §cé um assasino")
                        } else {
                            P.sendMessage("§9" + Sender.name + "§a acha que §a" + player + " §cé um assasino")
                        }
                    }

                }

                Victimain.instance.bukkitPlayers!!.forEach { P ->

                    if (!detetiveMain.insance.isRevelado(player)) {
                        P.sendMessage("§e" + Sender.name + "§a acha que §4" + player + " §cé um assasino")

                        if(detetiveMain.insance.getPlayer()!= null)
                            detetiveMain.insance.getBukkitPlayers().forEach { it.sendMessage("§e" + Sender.name + "§a acha que §4" + player + " §cé um assasino")  }

                        return@forEach
                    }


                    if (AssasinMain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§c" + Sender.name + "§a acha que §c" + player + " §cé um assasino")

                            if(detetiveMain.insance.getPlayer()!= null)
                                detetiveMain.insance.getBukkitPlayers().forEach { it.sendMessage("§c" + Sender.name + "§a acha que §c" + player + " §cé um assasino") }
                        } else {
                            P.sendMessage("§c" + Sender.name + "§a acha que §a" + player + " §cé um assasino")

                            if(detetiveMain.insance.getPlayer()!= null)
                                detetiveMain.insance.getBukkitPlayers().forEach { it.sendMessage("§c" + Sender.name + "§a acha que §c" + player + " §cé um assasino")}
                        }
                    }

                    if (Victimain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§a" + Sender.name + "§a acha que §c" + player + " §cé um assasino")
                        } else {
                            P.sendMessage("§a" + Sender.name + "§a acha que §a" + player + " §cé um assasino")
                        }
                    }

                    if (detetiveMain.insance.hasplayerinside(Sender.name)) {
                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§1" + Sender.name + "§a acha que §c" + player + " §cé um assasino")
                        } else {
                            P.sendMessage("§1" + Sender.name + "§a acha que §a" + player + " §cé um assasino")
                        }
                    }

                }


                Manager.instance.addDesconfiou(Sender.name, player)

                Manager.instance.addPlayer(player)
                Manager.instance.addPlayerUsed(Sender.name)
                return true
            }
        }

        Sender.sendMessage("§cVocê não pode usar esse comando agora.")

        return false
    }
}
