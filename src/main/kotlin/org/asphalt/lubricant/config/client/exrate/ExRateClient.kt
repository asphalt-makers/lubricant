package org.asphalt.lubricant.config.client.exrate

interface ExRateClient {
    fun getExRate(currency: String): ExRateClientResources.Response.ExRateData
}
