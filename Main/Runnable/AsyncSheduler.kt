package Main.Runnable

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

/**
 * Created by GMDAV on 29/06/2017.
 */
object AsyncSheduler {

    inline fun runTaskTimer(plguin: Plugin,Delay: Long,Timer: Long,crossinline body: () -> Boolean) : BukkitTask{
        return object : BukkitRunnable{
            override fun run() {
                if(!body())
                    cancel()
            }
        }.runTaskTimer(plguin,Delay,Timer)
    }
}