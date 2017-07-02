package Main

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Arrow
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

import java.util.ArrayList

/**
 * Created by GMDAV on 21/04/2017.
 */
object ItemsEdit {

    enum class ItemsToConsume private constructor(val item: ItemStack) {

        Tesoura(tesouraDetetive),
        ArcoSalvacao(arcoSalvacao),
        LaminaDoran(laminadoran),
        PoçãoVelocidade(PocaoVelocidade()),
        PoçãoInvisibilidade(PocaoInvisibilidade())


    }


    val tesouraDetetive: ItemStack
        get() {
            val TesouraDetetive = ItemStack(Material.SHEARS)
            val TesouraMeta = TesouraDetetive.itemMeta

            TesouraMeta.displayName = "§bTesoura inspetora"

            val lore = ArrayList<String>()
            lore.add("§aUse esta tesoura em um player que morreu e você")
            lore.add("Verá quem matou ele")

            TesouraMeta.lore = lore

            TesouraDetetive.itemMeta = TesouraMeta

            return TesouraDetetive
        }

    val laminadoran: ItemStack
        get() {
            val LaminaAss = ItemStack(Material.DIAMOND_SWORD)
            val LaminaAssItemMeta = LaminaAss.itemMeta

            LaminaAssItemMeta.displayName = "§4Lâmina de Doran!"

            val lore = ArrayList<String>()
            lore.add("§aUse essa lâmina para matar um player")
            lore.add("Rapidamente")

            LaminaAssItemMeta.lore = lore

            LaminaAss.itemMeta = LaminaAssItemMeta

            return LaminaAss
        }

    val arcoSalvacao: ItemStack
        get() {
            val ArcoSalvacao = ItemStack(Material.BOW)
            val ArcosalvaItemMeta = ArcoSalvacao.itemMeta

            ArcosalvaItemMeta.displayName = "§1Arco da Salvação"

            val lore = ArrayList<String>()
            lore.add("§aUse esse arco para matar o Sabotador")
            lore.add("§aRapidamente")

            ArcosalvaItemMeta.lore = lore

            ArcoSalvacao.itemMeta = ArcosalvaItemMeta

            return ArcoSalvacao
        }

    fun registerArcoSalvacao() {

        val ArcoReceita = ShapedRecipe(arcoSalvacao)
        ArcoReceita.shape(
                "@##",
                "#@#",
                "@##")

        ArcoReceita.setIngredient('#', Material.GOLD_INGOT)
        Bukkit.getServer().addRecipe(ArcoReceita)

    }

    fun registerFlechaRecipe() {


        val Flecha = ShapedRecipe(ItemStack(Material.ARROW, 1))
        Flecha.shape("@", "@")
        Flecha.setIngredient('@', Material.GOLD_INGOT)

        Bukkit.getServer().addRecipe(Flecha)

    }

    fun registerSword() {

        val Espada = ShapedRecipe(ItemStack(Material.GOLD_SWORD, 1))
        Espada.shape("@", "@", "@")
        Espada.setIngredient('@', Material.GOLD_INGOT)

        Bukkit.getServer().addRecipe(Espada)

    }

    fun PocaoInvisibilidade(): ItemStack {
        val pocao = Potion(PotionType.INVISIBILITY)
        pocao.level = 1
        pocao.setHasExtendedDuration(false)
        pocao.isSplash = false
        return pocao.toItemStack(1)
    }


    fun PocaoVelocidade(): ItemStack {
        val pocao = Potion(PotionType.SPEED)
        pocao.level = 1
        return pocao.toItemStack(1)
    }


}
