package net.milocodee.citybuild.plots.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

object PlotGeneration {

    private const val PLOT_SIZE = 32
    private const val ROAD_WIDTH = 5
    private const val GROUND_HEIGHT = 64
    private const val DIRT_LAYERS = 10
    private const val PLOT_COUNT = 10


    // i hate this type of code
    fun generateAllPlots() {
        for (i in 0 until PLOT_COUNT) {
            val plot = PlotData(i, 0)
            generatePlot(plot)

            if (i < PLOT_COUNT - 1) {
                generateRoadAfterPlot(plot)
            }
        }
    }

    fun generatePlot(plot: PlotData) {
        val world = Bukkit.getWorld("plots") ?: return

        val baseX = plot.gridX * (PLOT_SIZE + ROAD_WIDTH)
        val baseZ = plot.gridZ * (PLOT_SIZE + ROAD_WIDTH)

        for (x in 0 until PLOT_SIZE) {
            for (z in 0 until PLOT_SIZE) {

                // grass
                world.getBlockAt(baseX + x, GROUND_HEIGHT, baseZ + z)
                    .setType(Material.GRASS_BLOCK)

                // dirt layer
                for (y in 1..DIRT_LAYERS) {
                    world.getBlockAt(
                        baseX + x,
                        GROUND_HEIGHT - y,
                        baseZ + z
                    ).setType(Material.DIRT)
                }

                // bedrock
                world.getBlockAt(
                    baseX + x,
                    GROUND_HEIGHT - DIRT_LAYERS - 1,
                    baseZ + z
                ).setType(Material.BEDROCK)
            }
        }

        for (x in 0 until PLOT_SIZE) {
            world.getBlockAt(baseX + x, GROUND_HEIGHT + 1, baseZ)
                .setType(Material.STONE_BRICKS)
            world.getBlockAt(baseX + x, GROUND_HEIGHT + 1, baseZ + PLOT_SIZE - 1)
                .setType(Material.STONE_BRICKS)
        }

        for (z in 0 until PLOT_SIZE) {
            world.getBlockAt(baseX, GROUND_HEIGHT + 1, baseZ + z)
                .setType(Material.STONE_BRICKS)
            world.getBlockAt(baseX + PLOT_SIZE - 1, GROUND_HEIGHT + 1, baseZ + z)
                .setType(Material.STONE_BRICKS)
        }
    }

    private fun generateRoadAfterPlot(plot: PlotData) {
        val world = Bukkit.getWorld("plots") ?: return

        val startX =
            plot.gridX * (PLOT_SIZE + ROAD_WIDTH) + PLOT_SIZE
        val baseZ =
            plot.gridZ * (PLOT_SIZE + ROAD_WIDTH)

        for (x in 0 until ROAD_WIDTH) {
            for (z in 0 until PLOT_SIZE) {

                world.getBlockAt(
                    startX + x,
                    GROUND_HEIGHT,
                    baseZ + z
                ).setType(Material.STONE)

                for (y in 1..DIRT_LAYERS) {
                    world.getBlockAt(
                        startX + x,
                        GROUND_HEIGHT - y,
                        baseZ + z
                    ).setType(Material.DIRT)
                }

                world.getBlockAt(
                    startX + x,
                    GROUND_HEIGHT - DIRT_LAYERS - 1,
                    baseZ + z
                ).setType(Material.BEDROCK)
            }
        }
    }

    fun getPlotSpawn(plot: PlotData): Location {
        val world = Bukkit.getWorld("plots") ?: return Location(null, 0.0, 0.0, 0.0)

        val baseX = plot.gridX * (PLOT_SIZE + ROAD_WIDTH)
        val baseZ = plot.gridZ * (PLOT_SIZE + ROAD_WIDTH)

        return Location(
            world,
            (baseX + PLOT_SIZE / 2).toDouble(),
            (GROUND_HEIGHT + 2).toDouble(),
            (baseZ + PLOT_SIZE / 2).toDouble()
        )
    }
}
