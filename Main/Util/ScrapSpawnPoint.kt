package Main.Util

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block

import java.util.ArrayList
import java.util.HashSet
import java.util.stream.Collectors

/**
 * Created by GMDAV on 27/04/2017.
 */
class ScrapSpawnPoint {

    var scrapsLocation: List<Location> = ArrayList()
        private set


    constructor(Material: Material) {
        val Radius = Bukkit.getSpawnRadius()
        val Spawn = Bukkit.getWorlds()[0].spawnLocation

        val PointMax = Location(Spawn.world, Spawn.x + Radius, Spawn.y + Radius, Spawn.z + Radius)
        val PointMin = Location(Spawn.world, Spawn.x - Radius, Spawn.y - Radius, Spawn.z - Radius)

        val AllLocations = ArrayList<Location>()

        for (x in PointMin.blockX..PointMax.blockX) {
            for (y in PointMin.blockY..PointMax.blockY) {
                for (z in PointMin.blockZ..PointMax.blockZ) {
                    AllLocations.add(Location(Spawn.world, x.toDouble(), y.toDouble(), z.toDouble()))
                }
            }
        }

        scrapsLocation = AllLocations.stream().filter { P -> P.block.type == Material }.collect(Collectors.toList())
    }

    constructor(Material: Material, Radius: Int) {
        val Spawn = Bukkit.getWorlds()[0].spawnLocation

        val PointMax = Location(Spawn.world, Spawn.x + Radius, Spawn.y + Radius, Spawn.z + Radius)
        val PointMin = Location(Spawn.world, Spawn.x - Radius, Spawn.y - Radius, Spawn.z - Radius)

        val AllLocations = ArrayList<Location>()

        for (x in PointMin.blockX..PointMax.blockX) {
            for (y in PointMin.blockY..PointMax.blockY) {
                for (z in PointMin.blockZ..PointMax.blockZ) {
                    AllLocations.add(Location(Spawn.world, x.toDouble(), y.toDouble(), z.toDouble()))
                }
            }
        }

        scrapsLocation = AllLocations.stream().filter { P -> P.block.type == Material }.collect(Collectors.toList())
    }
}
