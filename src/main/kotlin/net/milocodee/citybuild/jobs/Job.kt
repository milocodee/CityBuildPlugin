package net.milocodee.citybuild.jobs

enum class Job {
    LUMBERJACK,
    MINER,
    HUNTER;

    companion object {
        fun fromString(s: String): Job? {
            return when (s.lowercase()) {
                "lumberjack", "wood", "holz", "holzfaeller", "holzfäller" -> LUMBERJACK
                "miner", "mine" -> MINER
                "hunter", "mob", "mobkill", "jäger", "jaeger" -> HUNTER
                else -> null
            }
        }
    }
}
