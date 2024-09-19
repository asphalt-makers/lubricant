package org.asphalt.lubricant.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.IOException
import java.nio.charset.StandardCharsets

class ApiLogFilter : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(ApiLogFilter::class.java)
    private val objectMapper = ObjectMapper() // Jackson ObjectMapper for JSON conversion

    // 로깅에 필요한 헤더만 포함
    private val allowedHeaders = listOf("Authorization", "Content-Type", "Accept", "User-Agent")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)
        val startTime = System.currentTimeMillis()

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            val duration = System.currentTimeMillis() - startTime
            if (!isMultipartRequest(request)) {
                logRequest(wrappedRequest)
                logResponse(wrappedResponse, duration)
            } else {
                log.info("Multipart request - Skipping body logging")
            }
            wrappedResponse.copyBodyToResponse()
        }
    }

    private fun isMultipartRequest(request: HttpServletRequest): Boolean {
        val contentType = request.contentType
        return contentType != null && contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)
    }

    private fun logRequest(request: ContentCachingRequestWrapper) {
        val requestBody = readRequestBody(request)
        val maskedRequestBody = PrivacyMasking.mask(requestBody)
        val filteredHeaders = filterHeaders(request)
        val logMap =
            mapOf(
                "type" to "REQ",
                "method" to request.method,
                "uri" to request.requestURI,
                "query" to (request.queryString ?: ""),
                "headers" to filteredHeaders,
                "body" to maskedRequestBody,
            )
        val logJson = objectMapper.writeValueAsString(logMap)
        log.info(logJson)
    }

    private fun logResponse(
        response: ContentCachingResponseWrapper,
        duration: Long,
    ) {
        val responseBody = readResponseBody(response)
        val maskedResponseBody = PrivacyMasking.mask(responseBody)
        val logMap =
            mapOf(
                "type" to "RES",
                "status" to response.status,
                "duration" to duration,
                "headers" to response.headerNames.associateWith { response.getHeader(it) },
                "body" to maskedResponseBody,
            )
        val logJson = objectMapper.writeValueAsString(logMap)
        log.info(logJson)
    }

    private fun filterHeaders(request: HttpServletRequest): Map<String, String> =
        request.headerNames
            .toList()
            .filter { allowedHeaders.contains(it) }
            .associateWith { request.getHeader(it) }

    private fun readRequestBody(request: ContentCachingRequestWrapper): String =
        try {
            val content = request.contentAsByteArray
            if (content.isNotEmpty()) {
                String(content, StandardCharsets.UTF_8)
            } else {
                "[empty body]"
            }
        } catch (e: IOException) {
            "[error reading request body]"
        }

    private fun readResponseBody(response: ContentCachingResponseWrapper): String =
        try {
            val content = response.contentAsByteArray
            if (content.isNotEmpty()) {
                String(content, StandardCharsets.UTF_8)
            } else {
                "[empty body]"
            }
        } catch (e: IOException) {
            "[error reading response body]"
        }
}

object PrivacyMasking {
    private val sensitiveFields =
        listOf(
            "\"phone\"",
            "\"email\"",
            "\"address\"",
            "\"password\"",
        )

    fun mask(input: String): String {
        var result = input
        for (field in sensitiveFields) {
            val regex = Regex("$field\\s*:\\s*\"([^\"]+)\"")
            result =
                regex.replace(result) {
                    val range = it.groups[1]?.range
                    if (range != null) {
                        val size = range.last - range.first + 1
                        it.value.replaceRange(range, "*".repeat(size))
                    } else {
                        it.value
                    }
                }
        }
        return result
    }
}
