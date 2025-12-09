package net.milocodee.citybuild.commands

import net.milocodee.citybuild.CityBuildMain
import net.milocodee.citybuild.jobs.Job
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class JobCommand(private val plugin: CityBuildMain) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return true
        }
        if (args.isEmpty()) {
            sender.sendMessage("Verwendung: /job <lumberjack|miner|hunter|none>")
            return true
        }
        val arg = args[0]
        if (arg.equals("none", true)) {
            plugin.jobManager.removeJob(sender.uniqueId)
            sender.sendMessage("§aJob entfernt.")
            return true
        }
        val job = Job.fromString(arg)
        if (job == null) {
            sender.sendMessage("Unbekannter Job. Verfügbare Jobs: lumberjack, miner, hunter")
            return true
        }
        plugin.jobManager.setJob(sender.uniqueId, job)
        sender.sendMessage("§aDein Job wurde gesetzt: §e${job.name}")
        return true
    }
}
