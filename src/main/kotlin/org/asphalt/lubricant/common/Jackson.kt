package org.asphalt.lubricant.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

object Jackson {
    // Lazy initialization to create ObjectMapper when needed
    private val mapper: ObjectMapper by lazy {
        jacksonObjectMapper()
            .registerKotlinModule()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(JavaTimeModule())
            .registerModule(customDateModule()) // Custom Date serializer registration
    }

    // Function to register the custom Date serializer
    private fun customDateModule(): SimpleModule = SimpleModule().addSerializer(Date::class.java, CustomDateSerializer())

    fun mapper(): ObjectMapper = mapper
}

private class CustomDateSerializer : StdSerializer<Date>(Date::class.java) {
    override fun serialize(
        value: Date?,
        gen: JsonGenerator?,
        provider: SerializerProvider?,
    ) {
        value?.let {
            gen?.writeString(
                LocalDateTime
                    .ofInstant(it.toInstant(), ZoneId.systemDefault())
                    .format(DateTimeFormatter.ISO_DATE_TIME),
            )
        }
    }
}

internal fun <T> T.toJson(): String = Jackson.mapper().writeValueAsString(this)

internal inline fun <reified T> String.fromJson(): T = Jackson.mapper().readValue(this)
