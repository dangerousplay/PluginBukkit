package Main.Runnable

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

/** Criada uma interface com a função de delegar para o Sheduler um novo task, com o mesmo resultado de
 * new BukkitRunnable(){
 * public void run(){

 * }
 * }

 * porém feito de uma forma mais sutil usando esta interface funcional.

 * ((BukkitRunnable) () -> {

 * })

 */

@FunctionalInterface
interface BukkitRunnable : Runnable {

    @Throws(IllegalStateException::class)
    fun cancel() {
        Bukkit.getScheduler().cancelTask(this.getTaskId())
    }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun runTask(plugin: Plugin): BukkitTask {
        this.checkState()
        return this.setupId(Bukkit.getScheduler().runTask(plugin, this))
    }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun runTaskAsynchronously(plugin: Plugin): BukkitTask {
        this.checkState()
        return this.setupId(Bukkit.getScheduler().runTaskAsynchronously(plugin, this))
    }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun runTaskLater(plugin: Plugin, delay: Int): BukkitTask {
        this.checkState()
        return this.setupId(Bukkit.getScheduler().runTaskLater(plugin, this, delay.toLong()))
    }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun runTaskLaterAsynchronously(plugin: Plugin, delay: Long): BukkitTask {
        this.checkState()
        return this.setupId(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, delay))
    }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun runTaskTimer(plugin: Plugin, delay: Long, period: Long): BukkitTask {
        this.checkState()
        return this.setupId(Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period))
    }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun runTaskTimerAsynchronously(plugin: Plugin, delay: Long, period: Long): BukkitTask {
        this.checkState()
        return this.setupId(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay, period))
    }

    @Throws(IllegalStateException::class)
    fun getTaskId(): Int {
        val id = -1
        if (id == -1) {
            throw IllegalStateException("Not scheduled yet")
        } else {
            return id
        }
    }

    fun checkState() {

    }

    fun setupId(task: BukkitTask): BukkitTask {
        return task
    }

    companion object {
        val taskId = -1
    }
}
