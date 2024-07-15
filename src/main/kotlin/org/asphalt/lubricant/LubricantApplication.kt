package org.asphalt.lubricant

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LubricantApplication

fun main(args: Array<String>) {
    runApplication<LubricantApplication>(*args)
}
