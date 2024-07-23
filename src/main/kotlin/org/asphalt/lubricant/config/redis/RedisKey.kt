package org.asphalt.lubricant.config.redis

interface RedisKey {
    fun getKey(): String

    companion object {
        const val PREFIX = "lubricant"
    }
}
