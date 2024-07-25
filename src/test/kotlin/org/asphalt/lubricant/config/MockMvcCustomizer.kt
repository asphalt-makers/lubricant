package org.asphalt.lubricant.config

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer
import org.springframework.boot.test.context.TestComponent
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder
import org.springframework.web.filter.CharacterEncodingFilter

@TestComponent
class MockMvcCustomizer<T : Nothing?> : MockMvcBuilderCustomizer {
    @Bean
    fun characterEncodingFilter(): CharacterEncodingFilter? = CharacterEncodingFilter("UTF-8", true)

    override fun customize(builder: ConfigurableMockMvcBuilder<*>) {
        builder.alwaysDo<T> { result -> result.response.characterEncoding = Charsets.UTF_8.name() }
    }
}
