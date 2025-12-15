package net.milocodee.citybuild.commands

import net.milocodee.citybuild.CityBuildMain
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlotweltCommand(private val plugin: CityBuildMain) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        if (sender !is Player) return true

        val world = plugin.server.getWorld("plots") ?: return true

        val plot = plugin.plotManager.getPlot(sender.uniqueId)
        if (plot == null) {
            sender.sendMessage("§cDu besitzt kein Plot.")
            return true
        }

        val plotSize = 32
        val roadWidth = 5
        val groundHeight = 64

        val baseX = plot.gridX * (plotSize + roadWidth)
        val baseZ = plot.gridZ * (plotSize + roadWidth)

        val spawnLocation = Location(
            world,
            (baseX + plotSize / 2).toDouble(),
            (groundHeight + 2).toDouble(),
            (baseZ + plotSize / 2).toDouble()
        )

        sender.teleport(spawnLocation)
        sender.sendMessage("§aDu wurdest zu deinem Plot teleportiert.")

        return true
    }
}
