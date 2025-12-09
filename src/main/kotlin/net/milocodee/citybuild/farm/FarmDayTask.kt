package net.milocodee.citybuild.farm

import net.milocodee.citybuild.CityBuildMain
import org.bukkit.Bukkit
import org.bukkit.entity.Item
import org.bukkit.entity.Monster
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.atomic.AtomicLong

class FarmDayTask(private val plugin: CityBuildMain) : BukkitRunnable() {

    private val lastTriggeredDay = AtomicLong(-1L)

    override fun run() {
        val world = Bukkit.getWorld("farmwelt") ?: return
        val time = world.time
        val currentDay = world.fullTime / 24000L

        if (time in 0..5 && lastTriggeredDay.get() < currentDay) {
            lastTriggeredDay.set(currentDay)
            announceAndScheduleCleanup(world.name)
        }
    }

    private fun announceAndScheduleCleanup(worldName: String) {
        val world = Bukkit.getWorld(worldName) ?: return
        world.players.forEach { it.sendMessage("§cAlle auf dem Boden liegenden Items und feindliche Mobs werden in 10 Sekunden gelöscht.") }

        runTaskLater(plugin, 20L * 5) {
            val w = Bukkit.getWorld(worldName) ?: return@runTaskLater
            w.players.forEach { it.sendMessage("§cAchtung: Noch 5 Sekunden bis zur Löschung.") }
        }

        runTaskLater(plugin, 20L * 10) {
            val w = Bukkit.getWorld(worldName) ?: return@runTaskLater
            var removedItems = 0
            var removedMobs = 0
            val entities = w.entities.toList()
            for (entity in entities) {
                when (entity) {
                    is Item -> {
                        entity.remove()
                        removedItems++
                    }
                    is Monster -> {
                        entity.remove()
                        removedMobs++
                    }
                }
            }
            w.players.forEach { it.sendMessage("§aCleanup abgeschlossen: §e$removedItems §aItems und §e$removedMobs §afeindliche Mobs entfernt.") }
        }
    }

    private fun runTaskLater(plugin: CityBuildMain, delay: Long, function: () -> Unit) {}
}
