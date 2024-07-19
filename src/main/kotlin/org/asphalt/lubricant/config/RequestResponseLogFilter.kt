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
        request.session?.let { message.append(";session=$it") }
        request.remoteUser?.let { message.append(";remote-user=$it") }
        message.append(";headers=${ServletServerHttpRequest(request).headers}")
        log.info(message.toString())
    }

    private fun afterRequest(
        request: HttpServletRequest,
        wrappedResponse: ContentCachingResponseWrapper,
        startTime: Long,
        requestBody: String? = null,
    ) {
        val message = loggingMessage(RESPONSE_PREFIX, request)
        message.append(";statue=${wrappedResponse.status}")
        message.append(";latency=${System.currentTimeMillis() - startTime}ms")

        if (HttpStatus.valueOf(wrappedResponse.status).is2xxSuccessful) {
            if (log.isDebugEnabled && requestBody != null) {
                message.append(";request-body=$requestBody")
            }
            log.info(message.toString())
        } else {
            message.append(";status=${wrappedResponse.status}")
            message.append(";request-body=$requestBody")
            wrappedResponse.contentAsByteArray.let { message.append(";response-content=${it.toString(charset)}") }
            log.error(message.toString())
        }
    }

    private fun loggingMessage(
        prefix: String,
        request: HttpServletRequest,
    ): StringBuilder {
        val msg =
            StringBuilder()
                .append("$prefix;method=[${request.method}];uri=${request.requestURI}")
        request.queryString?.let { msg.append("?$it") }
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
        private const val REQUEST_PREFIX = "[REQ]"
        private const val RESPONSE_PREFIX = "[RES]"
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
