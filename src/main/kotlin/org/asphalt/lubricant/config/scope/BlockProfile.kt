package org.asphalt.lubricant.config.scope

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BlockProfile(
    val profiles: Array<String> = ["prod"],
)
