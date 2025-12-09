package net.milocodee.citybuild.economy

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class EconomyStorage(private val folder: File) {

    init {
        if (!folder.exists()) folder.mkdirs()
    }

    fun load(uuid: String): EconomyPlayerData {
        val file = File(folder, "$uuid.yml")
        if (!file.exists()) {
            return EconomyPlayerData(uuid, 0.0)
        }

        val config = YamlConfiguration.loadConfiguration(file)
        val balance = config.getDouble("balance", 0.0)
        return EconomyPlayerData(uuid, balance)
    }

    fun save(data: EconomyPlayerData) {
        val file = File(folder, "${data.uuid}.yml")
        val config = YamlConfiguration()

        config.set("balance", data.balance)
        config.save(file)
    }
}
