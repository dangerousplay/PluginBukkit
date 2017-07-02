package Main.Util

import net.minecraft.server.v1_12_R1.EnumParticle
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.Arrays
import java.util.Random

/**
 * Created by GMDAV on 12/05/2017.
 */
object Effects {

    enum class ParticleEffects private constructor(internal val particleName: String, NMSName: EnumParticle) {



        HUGE_EXPLOSION("hugeexplosion",EnumParticle.EXPLOSION_HUGE),
        LARGE_EXPLODE("largeexplode",EnumParticle.EXPLOSION_LARGE),
        FIREWORKS_SPARK("fireworksSpark",EnumParticle.FIREWORKS_SPARK),
        BUBBLE("bubble",EnumParticle.WATER_BUBBLE),
        SUSPEND("suspend",EnumParticle.SUSPENDED),
        DEPTH_SUSPEND("depthSuspend",EnumParticle.SUSPENDED_DEPTH),
        TOWN_AURA("townaura",EnumParticle.TOWN_AURA),
        CRIT("crit",EnumParticle.CRIT),
        MAGIC_CRIT("magicCrit",EnumParticle.CRIT_MAGIC),
        MOB_SPELL("mobSpell",EnumParticle.SPELL_MOB),
        MOB_SPELL_AMBIENT("mobSpellAmbient",EnumParticle.SPELL_MOB_AMBIENT),
        SPELL("spell",EnumParticle.SPELL),
        INSTANT_SPELL("instantSpell",EnumParticle.SPELL_INSTANT),
        WITCH_MAGIC("witchMagic",EnumParticle.SPELL_WITCH),
        NOTE("note",EnumParticle.NOTE),
        PORTAL("portal",EnumParticle.PORTAL),
        ENCHANTMENT_TABLE("enchantmenttable",EnumParticle.ENCHANTMENT_TABLE),
        EXPLODE("explode",EnumParticle.EXPLOSION_NORMAL),
        FLAME("flame",EnumParticle.FLAME),
        LAVA("lava",EnumParticle.LAVA),
        FOOTSTEP("footstep",EnumParticle.FOOTSTEP),
        SPLASH("splash",EnumParticle.WATER_SPLASH),
        LARGE_SMOKE("largesmoke",EnumParticle.SMOKE_LARGE),
        CLOUD("cloud",EnumParticle.CLOUD),
        RED_DUST("reddust",EnumParticle.BLOCK_DUST),
        SNOWBALL_POOF("snowballpoof",EnumParticle.SNOWBALL),
        DRIP_WATER("dripWater",EnumParticle.DRIP_WATER),
        DRIP_LAVA("dripLava",EnumParticle.DRIP_LAVA),
        SNOW_SHOVEL("snowshovel",EnumParticle.SNOW_SHOVEL),
        SLIME("slime",EnumParticle.SLIME),
        HEART("heart",EnumParticle.HEART),
        ANGRY_VILLAGER("angryVillager",EnumParticle.VILLAGER_ANGRY),
        HAPPY_VILLAGER("happerVillager",EnumParticle.VILLAGER_HAPPY),
        ICONCRACK("iconcrack_",EnumParticle.ITEM_CRACK),
        TILECRACK("tilecrack_",EnumParticle.BLOCK_CRACK);


    }

    fun SendParticle(effects: EnumParticle, loc: Location, Particles: Int, random: Boolean) {

        if (random) {
            val R = Random()
            var Base = loc

            var X: Double
            var Y: Double
            var Z: Double

            for (i in 0..Particles) {

                if (R.nextBoolean()) {
                    X = R.nextDouble() * -1
                } else {
                    X = R.nextDouble()
                }

                if (R.nextBoolean()) {
                    Y = R.nextDouble() * -1
                } else {
                    Y = R.nextDouble()
                }

                if (R.nextBoolean()) {
                    Z = R.nextDouble() * -1
                } else {
                    Z = R.nextDouble()
                }

                Base.add(X, Y, Z)

                val LocFinal = Base

                Bukkit.getServer().onlinePlayers.forEach { P -> (P as CraftPlayer).handle.playerConnection.sendPacket(toPacket(effects, LocFinal, 2)) }
                Base = loc
            }

        } else {
            Bukkit.getServer().onlinePlayers.forEach { P -> (P as CraftPlayer).handle.playerConnection.sendPacket(toPacket(effects, loc, Particles)) }
        }


    }

    fun PlaySoundAll(Sound: Sound) {
       Bukkit.getServer().onlinePlayers.forEach { P -> P.playSound(P.location, Sound, 100f, 100f) }
    }

    fun toPacket(effects: EnumParticle, Location: Location, Ammount: Int): PacketPlayOutWorldParticles {

        return PacketPlayOutWorldParticles(effects,true, Location.x.toFloat(), Location.y.toFloat(), Location.z.toFloat(), 0.toFloat(), 0.toFloat(), 0.toFloat(), 0.toFloat(),0, Ammount)

    }


}
