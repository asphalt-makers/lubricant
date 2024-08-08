package org.asphalt.lubricant.sample.service

import org.asphalt.lubricant.client.exrate.ExRateClient
import org.asphalt.lubricant.sample.Sample
import org.asphalt.lubricant.sample.SampleDocument
import org.asphalt.lubricant.sample.persistence.redis.SampleRedisKey
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class SampleService(
    private val stringRedisTemplate: StringRedisTemplate,
    private val sampleDocumentRepository: SampleDocumentRepository,
    private val sampleJpaRepository: SampleJpaRepository,
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
        log.info("Created sample document - $sampleDocument")
        return sampleDocument.id.toString()
    }

    fun helloJpa(): Long {
        val sample = Sample("Hello, jdbc!")
        sampleJpaRepository.save(sample)
        log.info("Created sample - $sample")
        return sample.id!!
    }

    fun helloExRate(): BigDecimal {
        val currency = "USD"
        val exRate = exRateClient.getExRate(currency)
        return exRate.rates.get("KRW")!!
    }
}
