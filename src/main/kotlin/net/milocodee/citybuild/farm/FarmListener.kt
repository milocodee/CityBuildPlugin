package net.milocodee.citybuild.farm

import net.milocodee.citybuild.CityBuildMain
import net.milocodee.citybuild.jobs.Job
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
import java.util.concurrent.ThreadLocalRandom

class FarmListener(private val plugin: CityBuildMain) : Listener {

    companion object {
        private val LOG_MATERIALS = setOf(
            Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG,
            Material.ACACIA_LOG, Material.DARK_OAK_LOG, Material.MANGROVE_LOG,
            Material.STRIPPED_OAK_LOG, Material.STRIPPED_SPRUCE_LOG, Material.STRIPPED_BIRCH_LOG,
            Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_DARK_OAK_LOG,
            Material.STRIPPED_MANGROVE_LOG
        )

        private val ORE_MATERIALS = setOf(
            Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.DIAMOND_ORE,
            Material.EMERALD_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE,
            Material.NETHER_GOLD_ORE, Material.NETHER_QUARTZ_ORE
        )
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player = e.player
        if (player.world.name != "farmwelt") return
        val job = plugin.jobManager.getJob(player.uniqueId) ?: return
        val block: Block = e.block
        when (job) {
            Job.LUMBERJACK -> if (LOG_MATERIALS.contains(block.type)) payPlayer(player, 1.0, 5.0)
            Job.MINER -> if (ORE_MATERIALS.contains(block.type)) payPlayer(player, 2.0, 8.0)
            else -> {}
        }
    }

    @EventHandler
    fun onEntityDeath(e: EntityDeathEvent) {
        val killer = e.entity.killer ?: return
        if (killer.world.name != "farmwelt") return
        val job = plugin.jobManager.getJob(killer.uniqueId) ?: return
        if (job != Job.HUNTER) return
        val hostile = when (e.entity.type) {
            EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER,
            EntityType.ENDERMAN, EntityType.WITCH, EntityType.BLAZE, EntityType.HOGLIN,
            EntityType.ZOMBIFIED_PIGLIN, EntityType.SILVERFISH, EntityType.SLIME,
            EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.EVOKER, EntityType.HUSK,
            EntityType.DROWNED, EntityType.STRAY, EntityType.WITHER_SKELETON,
            EntityType.SHULKER, EntityType.MAGMA_CUBE, EntityType.GUARDIAN -> true
            else -> false
        }
        if (hostile) payPlayer(killer, 3.0, 12.0)
    }

    private fun payPlayer(player: Player, min: Double, max: Double) {
        val amount = ((ThreadLocalRandom.current().nextDouble(min, max) * 100.0).toInt()) / 100.0
        plugin.economy.deposit(player.uniqueId, amount)
        val msg = if (amount % 1.0 == 0.0) amount.toInt().toString() else String.format("%.2f", amount)
        player.sendMessage("§aJob: Du hast §e$msg §averdient.")
    }
}
