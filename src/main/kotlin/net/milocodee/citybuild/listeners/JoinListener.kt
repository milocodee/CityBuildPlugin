package net.milocodee.citybuild.listeners

import net.milocodee.citybuild.plots.PlotManager
import net.milocodee.citybuild.plots.data.PlotGeneration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent


class JoinListener(private val manager: PlotManager) : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.getPlayer()

        var plot = manager.getPlot(player.getUniqueId())
        if (plot == null) {
            plot = manager.createPlotForPlayer(player.getUniqueId(), player.getName())
            PlotGeneration.generatePlot(plot)
        }

        player.teleport(PlotGeneration.getPlotSpawn(plot))
    }
}
