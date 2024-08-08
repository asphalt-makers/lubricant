package org.asphalt.lubricant.util

import org.asphalt.lubricant.config.AppEnv
import org.springframework.boot.web.client.ClientHttpRequestFactories
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import java.time.Duration

object RestClientUtil {
    fun new(connectionInfo: AppEnv.ConnectionInfo): RestClient {
        val clientHttpRequestFactory =
            ClientHttpRequestFactories.get(
                ClientHttpRequestFactorySettings.DEFAULTS
                    .withReadTimeout(Duration.ofSeconds(connectionInfo.timeout))
                    .withConnectTimeout(Duration.ofSeconds(connectionInfo.timeout)),
            )
        return RestClient
            .builder()
            .baseUrl(connectionInfo.host)
            .defaultHeaders { headers ->
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }.requestFactory(clientHttpRequestFactory)
            .build()
    }
}
