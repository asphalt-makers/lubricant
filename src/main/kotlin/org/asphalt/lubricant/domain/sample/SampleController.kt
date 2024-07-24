package org.asphalt.lubricant.domain.sample

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}
