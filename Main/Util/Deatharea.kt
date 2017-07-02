package Main.Util

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.SkullType
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.material.Skull

import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * Created by GMDAV on 21/04/2017.
 */
class Deatharea(locations: Collection<Location>, var player: String?, private val Damagestaken: Map<String, Double>, private val EntityID : Int) : Iterable<Location> {

    private val CrimeLocations = ArrayList<Location>()

    val EntityIDs: Int
    get() = EntityID

    init {
        this.CrimeLocations.addAll(locations)
    }

    override fun iterator(): Iterator<Location> {
        return CrimeLocations.iterator()
    }

    override fun forEach(action: Consumer<in Location>) {
        Objects.requireNonNull(action)
        for (loc in CrimeLocations) {
            action.accept(loc)
        }
    }

    fun hasblockinside(b: Block): Boolean {
        return this.CrimeLocations.contains(b.location)
    }

    fun hasplayerinside(p: Player): Boolean {
        return this.CrimeLocations.contains(p.location)
    }


    val crimeLocations: List<Location>
        get() = CrimeLocations

    val damagesTakenByPlayer: List<String>
        get() {

            val Return = ArrayList<String>()

            val Sumof = ArrayList<Double>()
            Damagestaken.forEach { P, K -> Sumof.add(K) }

            val somatudo = Sumof.stream().mapToDouble { P -> P }.sum()

            if (somatudo <= 0) {
                Return.add("§1§lInvestigação do player " + "§b" + player)
                Return.add("§eMorreu por causas desconhecidas!")
            } else {

                Return.add("§c§lInvestigação do player " + "§b" + player)

                Damagestaken.forEach { K, V -> Return.add("§b" + K + " causou " + getpercentageint(somatudo, V) + "% de dano ao player") }

                Return.add("                 ")
            }
            return Return
        }

    val damagesTakenInventory: Inventory
        get() {
            val inventory = Bukkit.createInventory(null, if (Damagestaken.size % 9 == 0) Damagestaken.size else (Math.ceil((Damagestaken.size / 9).toDouble()) * 9).toInt(), "§lAnalise: " + player!!)

            val Sumof = ArrayList<Double>()
            Damagestaken.forEach { _, K -> Sumof.add(K) }

            val somatudo = Sumof.stream().mapToDouble { P -> P }.sum()


            Damagestaken.forEach { K, V ->

                val Skull = ItemStack(Material.SKULL, 1, SkullType.PLAYER.ordinal.toShort())

                val MetaSkull = Skull.itemMeta

                MetaSkull.displayName = K

                val Lore = ArrayList<String>()

                Lore.add("§b" + K + " causou §4" + getpercentageint(somatudo, V) + "§c% de dano ao player " + "")
                Lore.add(getcoracao(getpercentageint(somatudo, V)))
                MetaSkull.lore = Lore

                Skull.itemMeta = MetaSkull

                inventory.addItem(Skull)

            }

            return inventory
        }

    private fun getcoracao(Percentage: Int): String {
        val SB = StringBuilder()

        var i = 5
        while (i <= Percentage) {
            SB.append("♥")
            i *= 5
        }

        return SB.toString()
    }

    private fun getpercentage(max: Double, value: Double): Double {
        return 100 * value / max
    }

    private fun getpercentageint(max: Double, value: Double): Int {
        return Math.ceil(100 * value / max).toInt()
    }
}
