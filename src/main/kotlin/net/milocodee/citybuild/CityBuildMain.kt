package net.milocodee.citybuild

import net.milocodee.citybuild.economy.EconomyMain
import net.milocodee.citybuild.economy.commands.BalanceCommand
import net.milocodee.citybuild.economy.commands.PayCommand
import net.milocodee.citybuild.economy.data.EconomyStorage
import net.milocodee.citybuild.plots.PlotManager
import net.milocodee.citybuild.listeners.JoinListener
import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class CityBuildMain : JavaPlugin(), Listener {

    lateinit var economy: EconomyMain
        private set

    lateinit var plotManager: PlotManager
        private set

    override fun onEnable() {
            val economyFolder = File(dataFolder, "economy")
            if (!economyFolder.exists()) economyFolder.mkdirs()

            val storage = EconomyStorage(economyFolder)
            economy = EconomyMain(storage)

            val plotFolder = File(dataFolder, "plots")
            if (!plotFolder.exists()) plotFolder.mkdirs()

            plotManager = PlotManager(this, plotFolder)

            if (Bukkit.getWorld("plots") == null) {
                Bukkit.createWorld(WorldCreator("plots"))
            }

            server.pluginManager.registerEvents(this, this)
            server.pluginManager.registerEvents(JoinListener(plotManager), this)

            getCommand("balance")?.setExecutor(BalanceCommand(this))
            getCommand("pay")?.setExecutor(PayCommand(this))

    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        economy.loadPlayer(e.player.uniqueId)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        economy.savePlayer(e.player.uniqueId)
    }

    override fun onDisable() {
        server.onlinePlayers.forEach {
            economy.savePlayer(it.uniqueId)
        }

        plotManager.saveAll()
    }
}