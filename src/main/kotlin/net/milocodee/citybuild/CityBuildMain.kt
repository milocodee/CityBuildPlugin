package net.milocodee.citybuild

import net.milocodee.citybuild.commands.FarmweltCommand
import net.milocodee.citybuild.commands.PlotweltCommand
import net.milocodee.citybuild.commands.JobCommand
import net.milocodee.citybuild.commands.LobbyCommand
import net.milocodee.citybuild.jobs.JobManager
import net.milocodee.citybuild.plots.PlotManager
import net.milocodee.citybuild.farm.FarmDayTask
import net.milocodee.citybuild.farm.FarmListener
import net.milocodee.citybuild.economy.EconomyMain
import net.milocodee.citybuild.economy.commands.BalanceCommand
import net.milocodee.citybuild.economy.commands.PayCommand
import net.milocodee.citybuild.economy.data.EconomyStorage
import net.milocodee.citybuild.listeners.JoinListener
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
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

    lateinit var jobManager: JobManager
        private set

    lateinit var farmWorld: World
        private set

    lateinit var plotWorld: World
        private set

    lateinit var lobby: World
        private set

    override fun onEnable() {
        // Economy setup
        val economyFolder = File(dataFolder, "economy")
        if (!economyFolder.exists()) economyFolder.mkdirs()
        val storage = EconomyStorage(economyFolder)
        economy = EconomyMain(storage)

        // Plot setup
        val plotsFolder = File(dataFolder, "plots")
        if (!plotsFolder.exists()) plotsFolder.mkdirs()
        plotManager = PlotManager(this, plotsFolder)

        // Plot world
        plotWorld = Bukkit.getWorld("plots") ?: WorldCreator("plots")
            .environment(org.bukkit.World.Environment.NORMAL)
            .type(WorldType.FLAT)
            .createWorld()!!

        // Farm world
        farmWorld = Bukkit.getWorld("farmwelt") ?: run {
            logger.info("Erstelle Farmwelt (normal)...")
            WorldCreator("farmwelt")
                .environment(org.bukkit.World.Environment.NORMAL)
                .type(WorldType.NORMAL)
                .createWorld()!!
        }

        // Lobby
        lobby = Bukkit.getWorld("lobby") ?: run {
            WorldCreator("lobby")
                .environment(org.bukkit.World.Environment.NORMAL)
                .type(WorldType.FLAT)
                .createWorld()!!
        }

        // Job manager
        jobManager = JobManager()

        // Event listeners
        server.pluginManager.registerEvents(this, this)
        server.pluginManager.registerEvents(JoinListener(plotManager), this) // <-- FIXED
        server.pluginManager.registerEvents(FarmListener(this), this)

        // Farm day task
        FarmDayTask(this).runTaskTimer(this, 0L, 20L) // adjust timing if needed

        // Commands
        getCommand("balance")?.setExecutor(BalanceCommand(this))
        getCommand("pay")?.setExecutor(PayCommand(this))
        getCommand("farmwelt")?.setExecutor(FarmweltCommand(this))
        getCommand("plotwelt")?.setExecutor(PlotweltCommand(this))
        getCommand("job")?.setExecutor(JobCommand(this))
        getCommand("lobby")?.setExecutor(LobbyCommand(this))
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