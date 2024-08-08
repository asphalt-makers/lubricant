package org.asphalt.lubricant.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class AppEnv {
    var cryptoKey = CryptoKey()
    var client = Client()

    class CryptoKey {
        var attributeEncryptKey: String? = null
        var iv: String? = null
    }

    class Client {
        var payment = Payment()
        var exrate = Exrate()

        class Payment : ConnectionInfo()

        class Exrate : ConnectionInfo()
    }

    open class ConnectionInfo {
        var host: String = "localhost"
        var timeout: Long = 2
        var useDummy: Boolean = true
        var logging: Boolean = true
    }
}
