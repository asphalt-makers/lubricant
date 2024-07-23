package org.asphalt.lubricant.config

import com.asarkar.spring.test.redis.EmbeddedRedisConfigurer
import org.springframework.test.util.TestSocketUtils
import redis.embedded.RedisServerBuilder

class EmbeddedRedisConfiguration : EmbeddedRedisConfigurer {
    override fun configure(builder: RedisServerBuilder) {
        builder.port(getRandomPort())
    }

    companion object {
        var port = TestSocketUtils.findAvailableTcpPort()
        private var configured = false

        fun getRandomPort(): Int {
            if (configured.not()) {
                configured = true
                return port
            }

            port = TestSocketUtils.findAvailableTcpPort()
            return port
        }
    }
}
