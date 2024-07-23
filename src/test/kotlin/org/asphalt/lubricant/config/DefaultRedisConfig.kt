package org.asphalt.lubricant.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@TestConfiguration
class DefaultRedisConfig {
    val port = EmbeddedRedisConfiguration.port

    @Bean
    fun stringRedisTemplate(): StringRedisTemplate {
        val redisStandaloneConfiguration =
            RedisStandaloneConfiguration("localhost", port)
        val lettuceConnectionFactory =
            LettuceConnectionFactory(redisStandaloneConfiguration).apply { afterPropertiesSet() }
        return StringRedisTemplate(lettuceConnectionFactory)
    }
}
