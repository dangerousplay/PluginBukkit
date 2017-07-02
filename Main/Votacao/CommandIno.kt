package Main.Votacao

import Main.teams.Assasino.AssasinMain
import Main.teams.Detetive.detetiveMain
import Main.teams.PacketControl
import Main.teams.PlayerType
import Main.teams.Vítima.Victimain
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandIno : CommandExecutor {


    override fun onCommand(Sender: CommandSender, command: Command, s: String, args: Array<String>): Boolean {


        if (CoreD.isRunning()) {
            if (Manager.instance.containsPlayerInoce(Sender.name)) {
                Sender.sendMessage("§cAguarde para inocentar novamente!")
                return false
            }

            if (args.size != 1) {
                Sender.sendMessage("§cuse /inocentar [player]")
                return false
            }

            if (detetiveMain.insance.hasplayerinside(args[0])) {
                Sender.sendMessage("Você não pode inocentar o detetive")
                return false
            }

            if (Sender.name.equals(args[0], ignoreCase = true)) {
                Sender.sendMessage("§cVocê não pode inocentar a si mesmo!")
                return false
            }

            if (CoreDPlayer.isSpectator(Sender.name)) {
                Sender.sendMessage("§cVocê não pode inocentar como espectador")
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

                if (Manager.instance.inocentou(Sender.name, player)) {
                    Sender.sendMessage("§cVocê já inocentou o(a) " + player)
                    return false
                }





                AssasinMain.instance.bukkitPlayers!!.forEach { P ->

                    if (AssasinMain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {

                            P.sendMessage("§c" + Sender.name + "§a acha que §c" + player + " §aé um inocente")
                        } else {
                            P.sendMessage("§c" + Sender.name + "§a acha que §a" + player + " §aé um inocente")
                        }
                    }

                    if (Victimain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§a" + Sender.name + "§a acha que §c" + player + " §aé um inocente")
                        } else {
                            P.sendMessage("§a" + Sender.name + "§a acha que §a" + player + " §aé um inocente")
                        }
                    }

                    if (detetiveMain.insance.hasplayerinside(Sender.name)) {
                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§1" + Sender.name + "§a acha que §c" + player + " §aé um inocente")
                        } else {
                            P.sendMessage("§1" + Sender.name + "§a acha que §a" + player + " §aé um inocente")
                        }
                    }

                }

                Victimain.instance.bukkitPlayers!!.forEach { P ->

                    if (!detetiveMain.insance.isRevelado(player)) {
                        P.sendMessage("§4" + Sender.name + "§a acha que §e" + player + " §aé um inocente")
                        if(detetiveMain.insance.getPlayer()!= null)
                            detetiveMain.insance.getBukkitPlayers().forEach { it.sendMessage("§4" + Sender.name + "§a acha que §e" + player + " §aé um inocente")}
                        return@forEach
                    }


                    if (AssasinMain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {

                            P.sendMessage("§4" + Sender.name + "§a acha que §4" + player + " §aé um inocente")
                        } else {
                            P.sendMessage("§4" + Sender.name + "§a acha que §1" + player + " §aé um inocente")
                        }
                    }

                    if (Victimain.instance.hasplayerinside(Sender.name)) {

                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§a" + Sender.name + "§a acha que §4" + player + " §aé um inocente")

                            if(detetiveMain.insance.getPlayer()!= null)
                                detetiveMain.insance.getBukkitPlayers().forEach { it.sendMessage("§a" + Sender.name + "§a acha que §4" + player + " §aé um inocente") }
                        } else {
                            P.sendMessage("§a" + Sender.name + "§a acha que §1" + player + " §aé um inocente")

                            if(detetiveMain.insance.getPlayer()!= null)
                                detetiveMain.insance.getBukkitPlayers().forEach { it.sendMessage("§a" + Sender.name + "§a acha que §1" + player + " §aé um inocente") }
                        }
                    }

                    if (detetiveMain.insance.hasplayerinside(Sender.name)) {
                        if (Type == PlayerType.Assasino) {
                            P.sendMessage("§1" + Sender.name + "§a acha que §4" + player + " §aé um inocente")
                        } else {
                            P.sendMessage("§1" + Sender.name + "§a acha que §1" + player + " §aé um inocente")
                        }
                    }

                }

                Manager.instance.removePlayer(player)
                Manager.instance.addPlayerUsed(Sender.name)
                Manager.instance.addconfiou(Sender.name, player)
                return true
            }
        }

        Sender.sendMessage("§cVocê não pode usar esse comando agora.")

        return false
    }
}
