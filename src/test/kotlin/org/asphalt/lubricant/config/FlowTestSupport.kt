package org.asphalt.lubricant.config

import com.asarkar.spring.test.redis.AutoConfigureEmbeddedRedis
import com.asarkar.spring.test.redis.EmbeddedRedisAutoConfiguration
import org.asphalt.lubricant.LubricantApplication
import org.asphalt.lubricant.common.toJson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.net.URI

@ActiveProfiles("test")
@SpringBootTest(
    classes = [LubricantApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@AutoConfigureEmbeddedRedis(serverConfigurerClass = "org.asphalt.lubricant.config.EmbeddedRedisConfiguration")
@ContextConfiguration(classes = [EmbeddedRedisAutoConfiguration::class, DefaultRedisConfig::class])
@AutoConfigureMockMvc
@Import(MockMvcCustomizer::class)
class FlowTestSupport {
    @Autowired
    final lateinit var mockMvc: MockMvc

    // Flow test
    fun mockMvcFlow(
        method: HttpMethod,
        uri: URI,
        headers: List<Pair<String, Any>>? = null,
        request: Any? = null,
        httpStatus: HttpStatus = HttpStatus.OK,
    ): String =
        requestMockMvc(mockMvc, method, uri) {
            contentType = MediaType.APPLICATION_JSON
            if (request != null) {
                content = request.toJson()
            }
            headers?.forEach {
                header(it.first, it.second)
            }
        }.andExpect {
            status { isEqualTo(httpStatus.value()) }
        }.andDo {
            print()
        }.andReturn()
            .response
            .contentAsString

    @Throws(Exception::class)
    fun requestMockMvc(
        mockMvc: MockMvc,
        method: HttpMethod,
        uri: URI,
        dsl: MockHttpServletRequestDsl.() -> Unit = {},
    ): ResultActionsDsl =
        when (method) {
            HttpMethod.GET -> mockMvc.get(uri, dsl)
            HttpMethod.POST -> mockMvc.post(uri, dsl)
            HttpMethod.PUT -> mockMvc.put(uri, dsl)
            HttpMethod.PATCH -> mockMvc.patch(uri, dsl)
            HttpMethod.DELETE -> mockMvc.delete(uri, dsl)
            else -> {
                throw Exception("지원하지 않는 HTTP 요청입니다.")
            }
        }
}
