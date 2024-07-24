package org.asphalt.lubricant.config

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
class ExceptionHandler {
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
        log.error("classification|error|${ex.javaClass.simpleName}|${ex.message}", ex)
        return ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.servletPath,
            causeBy = causeBy,
            trace = ex.stackTraceToString(), // TODO : 이걸 좀 예쁘게 찍어보자
        )
    }
}
