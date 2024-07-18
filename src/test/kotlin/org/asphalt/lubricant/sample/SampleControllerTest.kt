package org.asphalt.lubricant.sample

import org.asphalt.lubricant.common.RoleHeader
import org.asphalt.lubricant.domain.sample.SampleController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SampleControllerTest {
    @Autowired
    final lateinit var mockMvc: MockMvc

    private val userId = 1111L

    @Test
    @DisplayName("Sample Controller 테스트")
    fun hello() {
        val uri = linkTo<SampleController> { hello() }.toUri()
        val result =
            mockMvc
                .get(uri) {
                    contentType = MediaType.APPLICATION_JSON
                    header(RoleHeader.User.KEY, userId)
                }.andDo {
                    println()
                }.andExpect {
                    status { is2xxSuccessful() }
                }.andReturn()
                .response
                .contentAsString

        assertThat(result).isEqualTo("Hello, World!")
    }
}
