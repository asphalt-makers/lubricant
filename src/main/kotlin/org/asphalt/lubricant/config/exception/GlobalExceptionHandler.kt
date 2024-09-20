package org.asphalt.lubricant.config.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@ControllerAdvice(annotations = [RestController::class])
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(ExceptionHandler::class.java)

    @ExceptionHandler(value = [RuntimeException::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleRuntimeException(
        runtimeException: RuntimeException,
        request: HttpServletRequest,
    ): ErrorResponse =
        from(
            request,
            HttpStatus.INTERNAL_SERVER_ERROR,
            runtimeException,
        )

    private fun from(
        request: HttpServletRequest,
        status: HttpStatus,
        ex: Exception,
        causeBy: Map<String, Any>? = null,
    ): ErrorResponse {
        log.error("Error occurred - Type: ${ex.javaClass.simpleName}, Message: ${ex.message}, Path: ${request.servletPath}", ex)

        return ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message ?: "Unknown error",
            path = request.servletPath,
            causeBy = causeBy,
            trace = formatStackTrace(ex.stackTraceToString()),
        )
    }

    // 트레이스 요약
    private fun formatStackTrace(stackTrace: String): String {
        val lines = stackTrace.lines()
        return if (lines.size > 5) {
            lines.take(5).joinToString("\n") + "\n... (stack trace truncated)"
        } else {
            stackTrace
        }
    }
}
