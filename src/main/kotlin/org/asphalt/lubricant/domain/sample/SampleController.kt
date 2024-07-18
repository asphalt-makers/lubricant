package org.asphalt.lubricant.domain.sample

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class SampleController {
    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> = ResponseEntity.ok("Hello, World!")
}
