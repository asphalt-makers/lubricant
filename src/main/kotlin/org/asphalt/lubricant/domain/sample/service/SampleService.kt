package org.asphalt.lubricant.domain.sample.service

import org.asphalt.lubricant.config.client.exrate.ExRateClient
import org.asphalt.lubricant.domain.sample.Sample
import org.asphalt.lubricant.domain.sample.SampleDocument
import org.asphalt.lubricant.domain.sample.persistence.redis.SampleRedisKey
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class SampleService(
    private val stringRedisTemplate: StringRedisTemplate,
    private val sampleDocumentRepository: SampleDocumentRepository,
    private val sampleJdbcRepository: SampleJdbcRepository,
    private val exRateClient: ExRateClient,
) {
    private val log = LoggerFactory.getLogger(SampleService::class.java)

    fun hello(): String = "Hello, world!"

    fun helloRedis(): String {
        val key = SampleRedisKey("hello").getKey()
        val increment = stringRedisTemplate.opsForValue().increment(key)
        return "hello[$increment]"
    }

    fun helloMongo(): String {
        val sampleDocument = SampleDocument("Hello, mongo!")
        sampleDocumentRepository.save(sampleDocument)
        log.info("Created sample document - ${sampleDocument.id}")
        return sampleDocument.id.toString()
    }

    fun helloJpa(): Long {
        val sample = Sample("Hello, jdbc!")
        sampleJdbcRepository.save(sample)
        log.info("Created sample - ${sample.id}")
        return sample.id!!
    }

    fun helloExRate(): BigDecimal {
        val currency = "USD"
        val exRate = exRateClient.getExRate(currency)
        return exRate.rates.get("KRW")!!
    }
}
