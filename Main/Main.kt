package Main

import Main.Util.*
import Main.Votacao.CommandEx
import Main.Votacao.CommandIno
import Main.Votacao.CommandNeu
import Main.Votacao.Manager
import Main.espectador.*
import Main.listeners.*
import Main.teams.Assasino.AssasinMain
import Main.teams.Assasino.listenersA
import Main.teams.Detetive.detetiveMain
import Main.teams.Detetive.listenersD
import Main.teams.Vítima.Victimain
import Main.teams.Vítima.listenersV
import Main.chat.ChatManger
import Main.chat.AutoMessage
import br.com.tlcm.cc.API.CoreD
import br.com.tlcm.cc.API.CoreDPlayer
import br.com.tlcm.cc.API.MiniGame
import br.com.tlcm.cc.API.enumerators.TeleportToLobbyReason
import net.minecraft.server.v1_12_R1.EntitySlime
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by GMDAV on 18/04/2017.
 */
class Main : JavaPlugin() {

    private val DetetiveList = listenersD()
    private val AssasinoListener = listenersA()
    private var ScrapLocation: Material? = null
    private val ScrapsLocation = ArrayList<Location>()


    val ListenerDetetive
    get() = DetetiveList


    override fun onEnable() {
        instance = this

        config.set("Minimum_players", config.get("Minimum_players", 12))
        config.set("Assasino_Ward", config.get("Assasino_Ward", 15))
        config.set("Vitimas_Ward", config.get("Vitimas_Ward", 10))
        config.set("Detetive_Ward", config.get("Detetive_Ward", 12))

        CoreD.setCancelBlockBreakEvent(true)
        CoreD.setCancelBlockPlaceEvent(true)
        CoreD.setCancelPlayerInteractEvent(true)
        CoreD.setCancelPlayerInteractEntityEvent(true)
        CoreD.setDisableDamage(true)
        CoreD.setDisableDeathScreen(true)
        CoreD.setAllowSpectatorsNearPlayers(false)
        CoreD.setToSpectatorOnRespawn(true)
        CoreD.setAllowJoinSpectators(true)
        CoreD.setAllowTeamChat(false)
        CoreD.setStore(true)

        CoreD.setMiniGame(MiniGame("Murders", "Start", config.getInt("Minimum_players")))

        Bukkit.getServer().pluginManager.registerEvents(AssasinoListener, this)
        Bukkit.getServer().pluginManager.registerEvents(DetetiveList, this)
        Bukkit.getServer().pluginManager.registerEvents(listenersV(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerDeath.instance, this)
        Bukkit.getServer().pluginManager.registerEvents(disabledevents(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerUseItems.instance, this)
        Bukkit.getServer().pluginManager.registerEvents(SpawnAdd(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerRespawn(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerDamageHologram(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerJoin(), this)
        Bukkit.getServer().pluginManager.registerEvents(EspecInteract(), this)
        Bukkit.getServer().pluginManager.registerEvents(ChatManger(), this)
        Bukkit.getServer().pluginManager.registerEvents(PlayerGoldCount.instance,this)

        getCommand("desconfiar").executor = CommandEx()
        getCommand("inocentar").executor = CommandIno()
        getCommand("neutro").executor = CommandNeu()

        CoreD.getMiniGame().isFastStart = true

        killmobs()

        ItemsEdit.registerArcoSalvacao()
        ItemsEdit.registerFlechaRecipe()
        ItemsEdit.registerSword()

        AutoMessage.start()
    }


    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<String>?): Boolean {
        if (sender!!.isOp) {
            if (label!!.equals("start", ignoreCase = true)) {
                CoreD.setRunning()
                Start()
                return true
            }

            if (label.equals("npc", ignoreCase = true) && args!!.size == 2 && sender is Player) {
                NPCManager.instance.spawnCorpse(sender)
                return true
            }

            if (label.equals("npcs", ignoreCase = true) && args!!.size == 2 && sender is Player) {
                PlayerDeath.instance.addPlayerDeath(SpawnUtil.spawndeathplayer(sender, Integer.parseInt(args[0]), Integer.parseInt(args[1])))
                return true
            }

            if (label.equals("addscraploc", ignoreCase = true) && args!!.size == 3) {

                val X = Integer.parseInt(args[0])
                val Y = Integer.parseInt(args[1])
                val Z = Integer.parseInt(args[2])

                ScrapsLocation.add(Location(Bukkit.getWorlds()[0], X.toDouble(), Y.toDouble(), Z.toDouble()))
            }

            if (label.equals("scrapitem",ignoreCase = true) && args!!.size == 1) {
                val BlockType = ItemStack(Integer.parseInt(args[0]))
                ScrapLocation = BlockType.type
                Bukkit.getServer().logger.info("O bloco foi adicionado " + BlockType.type)
                return true
            }

            if(label.equals("sbt",ignoreCase = true)){



                val Hologram = Hologram1_7((sender as Player).location,2.0,"Test")
                Bukkit.getOnlinePlayers().forEach {Hologram.send(it)}

                val SlimeTest = EntitySlime((sender.world as CraftWorld).handle)
                SlimeTest.locX = sender.location.x
                SlimeTest.locY = sender.location.y
                SlimeTest.locZ = sender.location.z

                SlimeTest.customNameVisible = true
                SlimeTest.customName = "TEst ok"
                SlimeTest.isInvisible = false

                (sender as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutSpawnEntityLiving(SlimeTest))

                return true
            }

        }
        return false
    }

    private fun killmobs() {
        Bukkit.getWorlds().forEach { W -> W.entities.forEach{ it::remove }}
    }

    private fun Start() {

        setartimes()
        habilitareventos()
        Manager.instance.start()

        Bukkit.getServer().pluginManager.registerEvents(PlayerHideArmor(),this)

        SpawnUtil.SpawnBloodStart()

        ClearItems(this)

        DetetiveList.Delaytouse()
        AssasinoListener.Delaytouse()
        Finalizador()

        PlayerGoldCount.instance.Start()

        //Setar Lugar para o Spawn dos Scraps
        if (ScrapsLocation.isEmpty()) {
            SpawnUtil.SpawnScraps(ScrapSpawnPoint(ScrapLocation!!).scrapsLocation, ItemStack(Material.GOLD_INGOT, 1))
        } else {
            val ScrapLimit: Int

            if (Bukkit.getServer().onlinePlayers.size > 10) {
                ScrapLimit = Bukkit.getOnlinePlayers().size * 3
            } else {
                ScrapLimit = Bukkit.getOnlinePlayers().size * 2
            }

            if (ScrapsLocation.size < ScrapLimit) {

                while (ScrapsLocation.size < ScrapLimit) {
                    ScrapsLocation.add(ScrapsLocation[(Math.random() * (ScrapsLocation.size - 1)).toInt()])
                }

            } else {
                while (ScrapsLocation.size > ScrapLimit) {
                    ScrapsLocation.removeAt(0)
                }
            }

            SpawnUtil.SpawnScraps(ScrapsLocation, ItemStack(Material.GOLD_INGOT, 1))
        }
    }

    private fun habilitareventos() {
        CoreD.setCancelPlayerInteractEvent(false)
        CoreD.setCancelPlayerInteractEntityEvent(false)
        CoreD.setDisableDamage(false)
        //CoreD.setDisableDeathScreen(true);
    }

    private fun setartimes() {
        val random = Random()
        val players = Bukkit.getServer().onlinePlayers.map { it.name }.toMutableList()

        val Detetives : MutableList<String>

        if(Bukkit.getServer().onlinePlayers.size > 12) {
            Detetives = ArrayList()
            for (i in 1..3) {
                val current = players[random.nextInt(players.size)]
                Detetives.add(current)
                players.remove(current)
            }
        }else{
            Detetives = ArrayList()
            for (i in 1..2) {
                val current = players[random.nextInt(players.size)]
                Detetives.add(current)
                players.remove(current)
            }
        }

        detetiveMain.insance.setPlayer(Detetives)
        players.removeIf { detetiveMain.insance.getPlayer()!!.contains(it)}

        val Vitimas = Math.floor(Bukkit.getServer().onlinePlayers.size - 1 * 0.7).toInt()

        val VitimasList = ArrayList<String>()

        for (i in 1..Vitimas - 1) {
            VitimasList.add(players[random.nextInt(players.size)])
        }

        Victimain.instance.setTeams(VitimasList)

        players.removeAll(VitimasList)

        AssasinMain.instance.setPlayer(players)


    }

    private fun Finalizador() {
        object : BukkitRunnable() {
            override fun run() {
                if (CoreD.isRunning()) {
                    if (AssasinMain.instance.getPlayer().isEmpty()) {

                        Effects.PlaySoundAll(Sound.ENTITY_ENDERDRAGON_DEATH)

                        Bukkit.broadcastMessage("                                  ")
                        Bukkit.broadcastMessage("§aOs inocentes venceram!")
                        Bukkit.broadcastMessage("                                  ")
                        Bukkit.broadcastMessage("§cOs assasinos eram: " + AssasinMain.instance.toString())
                        Bukkit.broadcastMessage("                                  ")

                        object : BukkitRunnable() {
                            override fun run() {

                                Victimain.instance.teams.forEach { P ->
                                    CoreD.sendToLobby(Bukkit.getPlayer(P),
                                            TeleportToLobbyReason.WINNER)
                                    CoreD.award(Bukkit.getPlayer(P), config.getInt("Vitimas_Ward"))

                                }

                                if (detetiveMain.insance.getPlayer() != null && detetiveMain.insance.getPlayer()?.isEmpty()!!) {
                                    detetiveMain.insance.getPlayer()!!.forEach {
                                        CoreD.award(Bukkit.getPlayer(it), config.getInt("Detetive_Ward"))
                                        CoreD.sendToLobby(Bukkit.getPlayer(it), TeleportToLobbyReason.WINNER)
                                    }
                                }

                                if (!CoreDPlayer.getSpectators().isEmpty()) {
                                    CoreDPlayer.getSpectators().forEach { P -> CoreD.sendToLobby(Bukkit.getPlayer(P), TeleportToLobbyReason.SPECTATOR) }
                                }

                            }
                        }.runTaskLater(Main.instance, (20 * 10).toLong())

                        cancel()
                        return
                    }

                    if (Victimain.instance.teams.isEmpty() && detetiveMain.insance.getPlayer() == null) {

                        Effects.PlaySoundAll(Sound.ENTITY_ENDERDRAGON_DEATH)

                        Bukkit.broadcastMessage("                                  ")
                        Bukkit.broadcastMessage("§cOs assasinos venceram!")
                        Bukkit.broadcastMessage("                                  ")
                        Bukkit.broadcastMessage("§cOs assasinos eram: " + AssasinMain.instance.toString())
                        Bukkit.broadcastMessage("                                  ")

                        object : BukkitRunnable() {
                            override fun run() {

                                AssasinMain.instance.getPlayer().forEach { P ->
                                    CoreD.sendToLobby(Bukkit.getPlayer(P), TeleportToLobbyReason.WINNER)
                                    CoreD.award(Bukkit.getPlayer(P), config.getInt("Assasino_Ward"))
                                }

                                if (!CoreDPlayer.getSpectators().isEmpty()) {
                                    CoreDPlayer.getSpectators().forEach { P -> CoreD.sendToLobby(Bukkit.getPlayer(P), TeleportToLobbyReason.SPECTATOR) }
                                }

                            }
                        }.runTaskLater(Main.instance, (20 * 10).toLong())



                        cancel()
                    }
                }
            }
        }.runTaskTimer(this, 0, 20)


    }

    override fun onDisable() {
        AutoMessage.stop()
        HandlerList.unregisterAll(this)

        unregisterPacketListeners()
    }

    internal fun unregisterPacketListeners(){
        Bukkit.getServer().onlinePlayers.forEach {
            (it as CraftPlayer).handle.playerConnection.networkManager.channel.pipeline().remove("packet_in_listener")
        }
    }

    companion object {
        var instance: Main? = null
            private set
    }
}
