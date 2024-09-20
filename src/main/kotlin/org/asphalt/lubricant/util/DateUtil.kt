package org.asphalt.lubricant.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 날짜를 포멧함
 */
fun LocalDateTime.formatyyyyMMddHHmmss(): String = this.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))

fun LocalDateTime.formatyyyyMMddHHmmssWithDash(): String = this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

fun LocalDate.formatyyyyMMdd(): String = this.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

fun LocalDate.formatyyyyMMddWithDash(): String = this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
