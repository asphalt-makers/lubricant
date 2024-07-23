package org.asphalt.lubricant.sample

import org.asphalt.lubricant.common.RoleHeader
import org.asphalt.lubricant.config.FlowTestSupport
import org.asphalt.lubricant.domain.sample.SampleController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType

class SampleControllerTest : FlowTestSupport() {
    private val userId = 1111L

    @Test
    @DisplayName("Sample Controller 테스트")
    fun hello() {
        val uri = linkTo<SampleController> { hello() }.toUri()
        val result =
            mockMvcFlow(
                HttpMethod.GET,
                uri,
                listOf(
                    Pair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON),
                    Pair(RoleHeader.User.KEY, userId),
                ),
            )
        assertThat(result).isEqualTo("hello[1]")
    }
}
