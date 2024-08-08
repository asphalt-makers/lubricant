package org.asphalt.lubricant.sample.web

import org.asphalt.lubricant.sample.service.SampleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/v1")
class SampleController(
    private val sampleService: SampleService,
) {
    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> = ResponseEntity.ok(sampleService.hello())

    @GetMapping("/hello-redis")
    fun helloRedis(): ResponseEntity<String> = ResponseEntity.ok(sampleService.helloRedis())

    @GetMapping("/hello-mongo")
    fun helloMongo(): ResponseEntity<String> = ResponseEntity.ok(sampleService.helloMongo())

    @GetMapping("/hello-jpa")
    fun helloJpa(): ResponseEntity<Long> = ResponseEntity.ok(sampleService.helloJpa())

    @GetMapping("/hello-error")
    fun helloError(): ResponseEntity<String> = throw RuntimeException("runtime exception")

    @GetMapping("/hello-ex-rate")
    fun helloExRate(): ResponseEntity<BigDecimal> = ResponseEntity.ok(sampleService.helloExRate())
}
