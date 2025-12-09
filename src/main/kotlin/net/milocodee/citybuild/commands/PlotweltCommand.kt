package net.milocodee.citybuild.commands

import net.milocodee.citybuild.CityBuildMain
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlotweltCommand(private val plugin: CityBuildMain) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return true
        }
        val world = plugin.server.getWorld("plots") ?: run {
            return true
        }
        sender.teleport(world.spawnLocation)
        sender.sendMessage("§aDu wurdest zur Plotwelt teleportiert.")
        return true
    }
}
