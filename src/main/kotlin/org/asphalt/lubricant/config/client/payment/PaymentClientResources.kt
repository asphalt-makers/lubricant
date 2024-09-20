package org.asphalt.lubricant.config.client.payment

class PaymentClientResources {
    class Request {
        data class Payment(
            val type: String = "A",
            val name: String = "payment request",
            val amount: Long = 9999L,
            val description: String? = null,
        )
    }

    class Response {
        data class PaymentResult(
            val code: String,
            val message: String,
        )
    }
}
