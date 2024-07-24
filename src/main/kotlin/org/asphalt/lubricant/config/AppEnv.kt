package org.asphalt.lubricant.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class AppEnv {
    var cryptoKey = CryptoKey()

    class CryptoKey {
        var attributeEncryptKey: String? = null
        var iv: String? = null
    }

    class Client {
        var payment = Payment()

        class Payment : ConnectionInfo()
    }

    open class ConnectionInfo {
        var host: String = "localhost"
        var timeout: Long = 2
        var useDummy: Boolean = true
    }
}
