package org.asphalt.lubricant.config.exception

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "Lubricant.ErrorResponse")
data class ErrorResponse(
    @Schema(description = "error time stamp")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    @Schema(description = "http status code")
    val status: Int,
    @Schema(description = "error name")
    val error: String,
    @Schema(description = "error message")
    val message: String? = null,
    @Schema(description = "api path")
    val path: String,
    @Schema(description = "caused by")
    val causeBy: Map<String, Any?>? = null,
    @Schema(description = "exception trace")
    val trace: String? = null,
)
