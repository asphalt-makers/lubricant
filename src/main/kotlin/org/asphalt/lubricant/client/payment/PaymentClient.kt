package org.asphalt.lubricant.client.payment

interface PaymentClient {
    fun pay(request: PaymentClientResources.Request.Payment): PaymentClientResources.Response.PaymentResult
}
