package Main.Votacao

import Main.teams.Assasino.AssasinMain
import Main.teams.Detetive.detetiveMain
import Main.teams.PacketControl
import Main.teams.PlayerType
import Main.teams.Vítima.Victimain
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandNeu : CommandExecutor {
    override fun onCommand(Sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (CoreD.isRunning()) {


            if (args.size != 1) {
                Sender.sendMessage("§cuse /neutro [player]")
                return false
            }

            if (!Manager.instance.containsVote(Sender.name, args[0])) {
                Sender.sendMessage("§cVocê não votou neste player.")
                return false
            }

            if (detetiveMain.insance.hasplayerinside(args[0])) {
                Sender.sendMessage("Você não pode neutralizar o detetive")
                return false
            }

            if (Sender.name.equals(args[0], ignoreCase = true)) {
                Sender.sendMessage("§cVocê não pode neutralizar a si mesmo!")
                return false
            }

            if (CoreDPlayer.isSpectator(Sender.name)) {
                Sender.sendMessage("§cVocê não pode neutralizar como espectador")
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


                AssasinMain.instance.bukkitPlayers!!.forEach { P ->

                    if (AssasinMain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {

                            P.sendMessage("§c" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        } else {
                            P.sendMessage("§c" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        }
                    }

                    if (Victimain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§a" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        } else {
                            P.sendMessage("§a" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        }
                    }

                    if (detetiveMain.insance.hasplayerinside(Sender.name)) {
                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§1" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        } else {
                            P.sendMessage("§1" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        }
                    }

                }

                Victimain.instance.bukkitPlayers!!.forEach { P ->

                    if (AssasinMain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§4" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        } else {
                            P.sendMessage("§4" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        }
                    }

                    if (Victimain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§a" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        } else {
                            P.sendMessage("§a" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        }
                    }

                    if (detetiveMain.insance.hasplayerinside(Sender.name)) {
                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§1" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        } else {
                            P.sendMessage("§1" + Sender.name + "§3 removeu o seu voto de §b" + player)
                        }
                    }

                }

                Manager.instance.addPlayerUsed(Sender.name)
                Manager.instance.removeVote(Sender.name, player)

                return true
            }
        }

        Sender.sendMessage("§cVocê não pode usar esse comando agora.")

        return false
    }
}
