package com.sebinesh.kabaddihero.core.ratelimit

import java.util.concurrent.ConcurrentHashMap

class RateLimiter(
    private val maxRequests: Int = 15,
    private val windowMillis: Long = 60_000L
) {
    private val timestampsByKey = ConcurrentHashMap<String, MutableList<Long>>()

    @Synchronized
    fun tryAcquire(key: String): Boolean {
        val now = System.currentTimeMillis()
        val list = timestampsByKey.getOrPut(key) { mutableListOf() }
        list.removeAll { now - it > windowMillis }

        return if (list.size < maxRequests) {
            list.add(now)
            true
        } else {
            false
        }
    }

    @Synchronized
    fun reset(key: String) {
        timestampsByKey.remove(key)
    }
}
