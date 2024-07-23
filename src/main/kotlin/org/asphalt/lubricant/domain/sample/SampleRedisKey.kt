package org.asphalt.lubricant.domain.sample

import org.asphalt.lubricant.config.redis.RedisKey

class SampleRedisKey(
    private val keyName: String,
) : RedisKey {
    override fun getKey(): String = "$KEY:$keyName"

    companion object {
        const val KEY = "${RedisKey.PREFIX}:sample"
    }
}
