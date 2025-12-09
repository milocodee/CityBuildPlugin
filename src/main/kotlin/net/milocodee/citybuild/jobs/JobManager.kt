package net.milocodee.citybuild.jobs

import java.util.*
import java.util.concurrent.ConcurrentHashMap

class JobManager {
    private val jobs = ConcurrentHashMap<UUID, Job>()

    fun setJob(uuid: UUID, job: Job) {
        jobs[uuid] = job
    }

    fun getJob(uuid: UUID): Job? = jobs[uuid]

    fun removeJob(uuid: UUID) {
        jobs.remove(uuid)
    }
}
