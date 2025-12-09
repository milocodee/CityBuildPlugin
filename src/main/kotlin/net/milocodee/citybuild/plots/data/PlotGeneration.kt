package net.milocodee.citybuild.plots.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

object PlotGeneration {
    private const val PLOT_SIZE = 32
    private const val GROUND_HEIGHT = 64

    // i hate this type of work
    fun generatePlot(plot: PlotData) {
        val world = Bukkit.getWorld("plots")
        if (world == null) return

        val baseX = plot.gridX * (PLOT_SIZE + 5)
        val baseZ = plot.gridZ * (PLOT_SIZE + 5)

        for (x in 0..<PLOT_SIZE) {
            for (z in 0..<PLOT_SIZE) {
                val block = world.getBlockAt(baseX + x, GROUND_HEIGHT, baseZ + z)
                block.setType(Material.GRASS_BLOCK)
            }
        }

        for (x in 0..<PLOT_SIZE) {
            world.getBlockAt(baseX + x, GROUND_HEIGHT + 1, baseZ).setType(Material.STONE_BRICKS)
            world.getBlockAt(baseX + x, GROUND_HEIGHT + 1, baseZ + PLOT_SIZE - 1).setType(Material.STONE_BRICKS)
        }
        for (z in 0..<PLOT_SIZE) {
            world.getBlockAt(baseX, GROUND_HEIGHT + 1, baseZ + z).setType(Material.STONE_BRICKS)
            world.getBlockAt(baseX + PLOT_SIZE - 1, GROUND_HEIGHT + 1, baseZ + z).setType(Material.STONE_BRICKS)
        }
    }

    fun getPlotSpawn(plot: PlotData): Location {
        val world = Bukkit.getWorld("plots")
        val baseX = plot.gridX * (PLOT_SIZE + 5)
        val baseZ = plot.gridZ * (PLOT_SIZE + 5)
        return Location(
            world,
            (baseX + PLOT_SIZE / 2).toDouble(),
            (GROUND_HEIGHT + 2).toDouble(),
            (baseZ + PLOT_SIZE / 2).toDouble()
        )
    }
}
