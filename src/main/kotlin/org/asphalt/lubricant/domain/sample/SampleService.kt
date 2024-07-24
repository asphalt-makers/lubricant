package org.asphalt.lubricant.domain.sample

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class SampleService(
    private val stringRedisTemplate: StringRedisTemplate,
    private val sampleDocumentRepository: SampleDocumentRepository,
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
}
