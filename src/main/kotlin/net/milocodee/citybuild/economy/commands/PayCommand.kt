package net.milocodee.citybuild.economy.commands

import net.milocodee.citybuild.CityBuildMain
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PayCommand(private val plugin: CityBuildMain) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        if (sender !is Player) {
            sender.sendMessage("only players can give money.") // again... why??
            return true
        }

        if (args.size != 2) {
            sender.sendMessage("Using: /pay <player> <value>")
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == null) {
            sender.sendMessage("Can't find this player.")
            return true
        }

        val amount = args[1].toDoubleOrNull()
        if (amount == null || amount <= 0) {
            sender.sendMessage("Invalid value.")
            return true
        }

        if (!plugin.economy.transfer(sender.uniqueId, target.uniqueId, amount)) {
            sender.sendMessage("You don't have enough money.")
            return true
        }

        sender.sendMessage("You've sent $amount money to ${target.name}")
        target.sendMessage("You've got $amount money from ${sender.name}")

        return true
    }
}
