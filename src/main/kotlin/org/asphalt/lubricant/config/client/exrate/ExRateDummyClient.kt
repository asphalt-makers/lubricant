package org.asphalt.lubricant.config.client.exrate

import org.slf4j.LoggerFactory
import java.math.BigDecimal

class ExRateDummyClient : ExRateClient {
    private val log = LoggerFactory.getLogger(ExRateDummyClient::class.java)

    override fun getExRate(currency: String): ExRateClientResources.Response.ExRateData {
        log.info("Call ExRate!! => $currency")
        return ExRateClientResources.Response.ExRateData(
            result = "success",
            mutableMapOf("USD" to BigDecimal.TEN),
        )
    }
}
