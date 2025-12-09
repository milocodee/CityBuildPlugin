package net.milocodee.citybuild

import net.milocodee.citybuild.economy.EconomyMain
import net.milocodee.citybuild.economy.EconomyStorage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class CityBuildMain : JavaPlugin(), Listener {

    lateinit var economy: EconomyMain
        private set

    override fun onEnable() {
        logger.info("CityBuild Plugin gestartet.")

        val economyFolder = File(dataFolder, "economy")
        val storage = EconomyStorage(economyFolder)
        economy = EconomyMain(storage)

        server.pluginManager.registerEvents(this, this)

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
        logger.info("CityBuild Plugin beendet.")
    }
}
