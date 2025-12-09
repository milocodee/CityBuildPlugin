package net.milocodee.citybuild.economy

import net.milocodee.citybuild.economy.data.EconomyPlayerData
import net.milocodee.citybuild.economy.data.EconomyStorage
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class EconomyMain(
    private val storage: EconomyStorage
) {

    private val cache = ConcurrentHashMap<String, EconomyPlayerData>()

    fun loadPlayer(uuid: UUID) {
        val data = storage.load(uuid.toString())
        cache[uuid.toString()] = data
    }

    fun savePlayer(uuid: UUID) {
        val data = cache[uuid.toString()] ?: return
        storage.save(data)
    }

    fun getBalance(uuid: UUID): Double {
        return cache[uuid.toString()]?.balance ?: 0.0
    }

    fun deposit(uuid: UUID, amount: Double) {
        val data = cache[uuid.toString()] ?: return
        data.balance += amount
    }

    fun withdraw(uuid: UUID, amount: Double): Boolean {
        val data = cache[uuid.toString()] ?: return false
        if (data.balance < amount) return false
        data.balance -= amount
        return true
    }

    fun transfer(from: UUID, to: UUID, amount: Double): Boolean {
        if (!withdraw(from, amount)) return false
        deposit(to, amount)
        return true
    }
}