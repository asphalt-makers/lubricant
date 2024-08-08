package org.asphalt.lubricant.client.exrate

import org.asphalt.lubricant.config.AppEnv
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestClient

class ExRateStableClient(
    private val env: AppEnv.Client.Exrate,
    private val restClient: RestClient,
) : ExRateClient {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun getExRate(currency: String): ExRateClientResources.Response.ExRateData {
        val uri = "${env.host}/v6/latest/$currency"
        val body =
            restClient
                .get()
                .uri(uri)
                .retrieve()
                .body(ExRateClientResources.Response.ExRateData::class.java)!!
        if (env.logging) {
            log.info("Response body: $body")
        }
        return body
    }
}
