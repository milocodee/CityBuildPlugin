package net.milocodee.citybuild.plots

import net.milocodee.citybuild.CityBuildMain
import net.milocodee.citybuild.plots.data.PlotData
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

class PlotManager(private val main: CityBuildMain, private val plotFolder: File) {
    private val playerPlots = HashMap<UUID, PlotData>()
    private var nextPlotIndex = 0

    fun createPlotForPlayer(uuid: UUID, name: String): PlotData {
        val index = nextPlotIndex++

        val gridX = index % 100
        val gridZ = index / 100

        val data = PlotData(gridX, gridZ, name)
        playerPlots[uuid] = data
        return data
    }

    fun getPlot(uuid: UUID): PlotData? {
        return playerPlots[uuid]
    }

    fun savePlot(uuid: UUID, plot: PlotData) {
        val file = File(plotFolder, "$uuid.yml")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        val config = YamlConfiguration()
        config.set("owner", uuid.toString())
        config.set("name", plot.name)
        config.set("gridX", plot.gridX)
        config.set("gridZ", plot.gridZ)

        config.save(file)
    }

    fun saveAll() {
        for ((uuid, plot) in playerPlots) {
            savePlot(uuid, plot)
        }
    }

    companion object {
        const val PLOT_SIZE = 32 // 32x32
    }
}
