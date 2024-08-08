package org.asphalt.lubricant.client.exrate

import org.asphalt.lubricant.config.AppEnv
import org.asphalt.lubricant.util.RestClientUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExRateClientConfig(
    private val appEnv: AppEnv,
) {
    @Bean
    fun exRateClient(): ExRateClient {
        val env = appEnv.client.exrate
        return if (env.useDummy) {
            ExRateDummyClient()
        } else {
            ExRateStableClient(env, RestClientUtil.new(env))
        }
    }
}
