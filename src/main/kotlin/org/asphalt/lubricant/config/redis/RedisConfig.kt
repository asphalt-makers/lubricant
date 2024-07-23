package org.asphalt.lubricant.config.redis

import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import java.time.Duration

@Configuration
@EnableConfigurationProperties(RedisProperties::class)
@EnableRedisRepositories(basePackages = ["org.asphalt.lubricant"])
class RedisConfig(
    private val redisProperties: RedisProperties,
) {
    @Bean
    fun stringRedisTemplate(): StringRedisTemplate {
        val lettuceClientConfiguration =
            LettuceClientConfiguration
                .builder()
                .commandTimeout(Duration.ofSeconds(5))
                .clientOptions(
                    ClusterClientOptions
                        .builder()
                        .topologyRefreshOptions(
                            ClusterTopologyRefreshOptions
                                .builder()
                                .enablePeriodicRefresh(
                                    redisProperties.lettuce.cluster.refresh.period,
                                ).build(),
                        ).build(),
                ).build()

        val redisClusterConfiguration =
            RedisClusterConfiguration(
                redisProperties.cluster.nodes,
            ).apply {
                this.password = RedisPassword.of(redisProperties.password)
            }

        val lettuceConnectionFactory =
            LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration)
                .apply {
                    afterPropertiesSet()
                }
        return StringRedisTemplate(lettuceConnectionFactory)
    }
}
