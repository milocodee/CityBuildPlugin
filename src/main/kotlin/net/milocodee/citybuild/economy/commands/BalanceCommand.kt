package net.milocodee.citybuild.economy.commands

import net.milocodee.citybuild.CityBuildMain
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BalanceCommand(private val plugin: CityBuildMain) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        if (sender !is Player) {
            sender.sendMessage("Only players can execute this command") // idk why i coded this lmao
            return true
        }

        val bal = plugin.economy.getBalance(sender.uniqueId)
        sender.sendMessage("Your balance: $bal Coins")

        return true
    }
}
