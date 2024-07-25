package org.asphalt.lubricant

import org.asphalt.lubricant.config.AppEnv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppEnv::class)
class LubricantApplication

fun main(args: Array<String>) {
    runApplication<LubricantApplication>(*args)
}
