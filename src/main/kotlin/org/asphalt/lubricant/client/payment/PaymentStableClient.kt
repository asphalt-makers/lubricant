package org.asphalt.lubricant.client.payment

import org.asphalt.lubricant.config.AppEnv
import org.springframework.web.client.RestClient

class PaymentStableClient(
    private val env: AppEnv.Client.Payment,
    private val restClient: RestClient,
) : PaymentClient {
    override fun pay(request: PaymentClientResources.Request.Payment): PaymentClientResources.Response.PaymentResult {
        TODO("Not yet implemented")
    }
}
