package org.asphalt.lubricant.domain.sample

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class SampleService(
    private val stringRedisTemplate: StringRedisTemplate,
) {
    fun hello(): String {
        val key = SampleRedisKey("hello").getKey()
        val increment = stringRedisTemplate.opsForValue().increment(key)
        return "hello[$increment]"
    }
}
