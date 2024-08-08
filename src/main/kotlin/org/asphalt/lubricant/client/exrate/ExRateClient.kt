package org.asphalt.lubricant.client.exrate

interface ExRateClient {
    fun getExRate(currency: String): ExRateClientResources.Response.ExRateData
}
