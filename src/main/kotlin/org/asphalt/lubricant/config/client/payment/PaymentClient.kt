package org.asphalt.lubricant.config.client.payment

interface PaymentClient {
    fun pay(request: PaymentClientResources.Request.Payment): PaymentClientResources.Response.PaymentResult
}
