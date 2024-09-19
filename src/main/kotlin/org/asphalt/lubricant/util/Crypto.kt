package org.asphalt.lubricant.util

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Crypto(
    key: String,
    iv: String,
) {
    private val ivParameterSpec = IvParameterSpec(iv.toByteArray())
    private val algorithm = "AES/CBC/PKCS5Padding"
    private val keySpec = SecretKeySpec(key.toByteArray(), "AES")
    private val jackson = Jackson.mapper()

    fun enc(value: Any): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)
        val json = jackson.writeValueAsString(value)
        val byteArrayOfEncrypt = cipher.doFinal(json.toByteArray(Charsets.UTF_8))

        return Base64.getUrlEncoder().withoutPadding().encodeToString(byteArrayOfEncrypt)
    }

    fun <T> dec(
        text: String,
        valueType: Class<T>,
    ): T {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)
        val cipherText = Base64.getUrlDecoder().decode(text)
        val decryptedText = String(cipher.doFinal(cipherText), Charsets.UTF_8)

        return jackson.readValue(decryptedText, valueType)
    }
}
