package net.milocodee.citybuild.plots.data

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class PlotData(
    val gridX: Int,
    val gridZ: Int,
    var name: String?
) {

    fun saveToFile(file: File) {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        val config = YamlConfiguration()
        config.set("name", name)
        config.set("gridX", gridX)
        config.set("gridZ", gridZ)

        config.save(file)
    }

    companion object {
        fun loadFromFile(file: File): PlotData? {
            if (!file.exists()) return null

            val config = YamlConfiguration.loadConfiguration(file)
            val name = config.getString("name")
            val gridX = config.getInt("gridX")
            val gridZ = config.getInt("gridZ")

            return PlotData(gridX, gridZ, name)
        }
    }
}
