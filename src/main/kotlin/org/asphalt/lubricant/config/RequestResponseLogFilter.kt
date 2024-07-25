package org.asphalt.lubricant.config

import jakarta.servlet.FilterChain
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.util.StreamUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset

class RequestResponseLogFilter : OncePerRequestFilter() {
    private val charset = Charset.forName("UTF-8")
    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val isAsync = isAsyncDispatch(request)
        val startTime = System.currentTimeMillis()

        if (isSkipUri(request) || isAsync) {
            filterChain.doFilter(request, response)
        } else {
            val multiReadRequest = request as? MultiReadHttpServletRequest ?: MultiReadHttpServletRequest(request)
            val wrappedResponse = response as? ContentCachingResponseWrapper ?: ContentCachingResponseWrapper(response)
            try {
                beforeRequest(multiReadRequest)
                filterChain.doFilter(multiReadRequest, wrappedResponse)
            } finally {
                afterRequest(multiReadRequest, wrappedResponse, startTime, readBody(multiReadRequest))
                wrappedResponse.copyBodyToResponse()
            }
        }
    }

    private fun beforeRequest(request: HttpServletRequest) {
        val message = loggingMessage(REQUEST_PREFIX, request)
        request.getSession(false)?.let { message["session"] = it.id }
        request.remoteUser?.let { message["remote-user"] = it }
        message["headers"] = ServletServerHttpRequest(request).headers
        log.info(message.toString())
    }

    private fun afterRequest(
        request: HttpServletRequest,
        wrappedResponse: ContentCachingResponseWrapper,
        startTime: Long,
        requestBody: String? = null,
    ) {
        val message = loggingMessage(RESPONSE_PREFIX, request)
        message["statue"] = wrappedResponse.status
        message["latency"] = "${System.currentTimeMillis() - startTime}ms"
        appendMaskingRequestBody(requestBody, message)

        if (HttpStatus.valueOf(wrappedResponse.status).is2xxSuccessful) {
            if (log.isDebugEnabled && requestBody != null) {
                appendMaskingResponseContent(wrappedResponse.contentAsByteArray.toString(charset), message)
            }
            log.info(message.toString())
        } else {
            log.error(message.toString())
        }
    }

    private fun appendMaskingRequestBody(
        requestBody: String?,
        message: MutableMap<String, Any>,
    ) {
        requestBody?.let { message["request-payload"] = PrivacyFilter.masking(it) }
    }

    private fun appendMaskingResponseContent(
        response: String?,
        message: MutableMap<String, Any>,
    ) {
        response?.let { message["response-content"] = PrivacyFilter.masking(it) }
    }

    private fun loggingMessage(
        prefix: String,
        request: HttpServletRequest,
    ): MutableMap<String, Any> {
        val msg = mutableMapOf<String, Any>()
        msg["type"] = prefix
        msg["method"] = request.method
        msg["url"] =
            if (request.queryString != null) "${request.requestURI}?${request.queryString}" else request.requestURI
        return msg
    }

    private fun isSkipUri(request: HttpServletRequest): Boolean =
        request.requestURI.startsWith("/health") ||
            request.requestURI.startsWith("/favicon.ico") ||
            request.requestURI.startsWith("/h2-console") ||
            request.requestURI.startsWith("/actuator/health") ||
            request.requestURI.startsWith("/webjars/springfox-swagger-ui") ||
            request.requestURI.startsWith("/swagger-ui") ||
            request.requestURI.startsWith("/swagger-ui.html") ||
            request.requestURI.startsWith("/swagger-ui/index.html") ||
            request.requestURI.startsWith("/v3/api-docs")

    private fun readBody(request: HttpServletRequest): String? =
        try {
            if (request.inputStream == null) {
                null
            } else {
                StreamUtils.copyToString(request.inputStream, charset)
            }
        } catch (e: IOException) {
            "Can't read body. cause => ${e.message}"
        }

    companion object {
        private const val REQUEST_PREFIX = "REQ"
        private const val RESPONSE_PREFIX = "RES"
    }
}

internal class MultiReadHttpServletRequest(
    request: HttpServletRequest,
) : HttpServletRequestWrapper(request) {
    private var cachedBytes: ByteArray? = null

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        if (cachedBytes == null) {
            cacheInputStream()
        }

        return CachedServletInputStream()
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader = BufferedReader(InputStreamReader(inputStream))

    @Throws(IOException::class)
    private fun cacheInputStream() {
        val out = ByteArrayOutputStream()

        StreamUtils.copy(super.getInputStream(), out)
        cachedBytes = out.toByteArray()
    }

    inner class CachedServletInputStream : ServletInputStream() {
        override fun isReady(): Boolean = true

        override fun isFinished(): Boolean = true

        override fun setReadListener(listener: ReadListener) {
        }

        private val input: ByteArrayInputStream = ByteArrayInputStream(cachedBytes)

        @Throws(IOException::class)
        override fun read(): Int = input.read()
    }
}

private object PrivacyFilter {
    private val logger = LoggerFactory.getLogger(PrivacyFilter::class.java)

    private const val STRING_PATTERN = "\"%s\"\\s*:\\s*\"([^\"]+)\",?"

    //    private const val intPattern = "\"%s\"\\s*:\\s*([0-9]+)"
    private val filterPatterns =
        listOf(
            STRING_PATTERN.format("phone").toRegex(),
            STRING_PATTERN.format("address").toRegex(),
            STRING_PATTERN.format("userName").toRegex(),
            STRING_PATTERN.format("email").toRegex(),
            STRING_PATTERN.format("birthday").toRegex(),
//        intPattern.format("userId").toRegex(),
//        intPattern.format("user-id").toRegex(),
        )

    fun masking(input: String): String =
        try {
            var result = input
            for (pattern in filterPatterns) {
                val matches = pattern.findAll(result)
                for (match in matches) {
                    val range = match.groups.last()!!.range
                    val size = (range.last - range.first) + 1
                    result = result.replaceRange(range, "*".repeat(size))
                }
            }
            result
        } catch (ex: Exception) {
            logger.error("masking fail!! ", ex)
            "Privacy Masking Error!!"
        }
}
