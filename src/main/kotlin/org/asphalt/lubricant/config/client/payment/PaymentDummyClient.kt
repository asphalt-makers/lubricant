package org.asphalt.lubricant.config.client.payment

import org.slf4j.LoggerFactory

class PaymentDummyClient : PaymentClient {
    private val log = LoggerFactory.getLogger(PaymentDummyClient::class.java)

    override fun pay(request: PaymentClientResources.Request.Payment): PaymentClientResources.Response.PaymentResult {
        log.info("Call payment client!!")
        return PaymentClientResources.Response.PaymentResult(
            code = "00",
            message = "success",
        )
    }
}
