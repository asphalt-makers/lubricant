package org.asphalt.lubricant.util

import java.util.Locale

internal fun String.camelToSnakeHibernateVer(): String {
    val builder = StringBuilder(this.replace('.', '_'))
    var i = 1
    while (i < builder.length - 1) {
        if (isUnderscoreRequired(builder[i - 1], builder[i], builder[i + 1])) {
            builder.insert(i++, '_')
        }
        i++
    }
    return builder.toString().lowercase(Locale.getDefault())
}

private fun isUnderscoreRequired(
    before: Char,
    current: Char,
    after: Char,
): Boolean = Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after)

internal fun String.camelToSnakeCase(): String =
    this
        .fold(StringBuilder()) { acc, c ->
            acc.let {
                val lowerC = c.lowercase()
                acc.append(if (acc.isNotEmpty() && c.isUpperCase()) "_$lowerC" else lowerC)
            }
        }.toString()

internal fun String.toMaskAll(): String =
    if (this.isEmpty()) {
        this
    } else {
        "*".repeat(this.length)
    }

internal fun String.toMaskUserName(): String =
    if (this.isEmpty()) {
        this
    } else {
        when (this.length) {
            1 -> {
                "*"
            }

            2 -> {
                "${this.first()}*"
            }

            else -> {
                "${this.first()}${"*".repeat(this.length - 2)}${this.last()}"
            }
        }
    }

internal fun String.toMaskEmail(): String {
    return if (this.isEmpty() || this.contains("@").not()) {
        this
    } else {
        val at = this.indexOf("@")
        if (at < 0) {
            return this
        }
        val emailID = this.substring(0, at)
        val domain = this.substring(at)
        when (emailID.length) {
            1 -> {
                "*$domain"
            }

            2 -> {
                "${emailID.first()}*$domain"
            }

            else -> {
                "${emailID.substring(0, emailID.length - 2)}**$domain"
            }
        }
    }
}

internal fun String.toMaskPhoneNumber(): String {
    return if (this.isEmpty()) {
        this
    } else {
        if (this.length < 3) {
            return this
        }
        val front = this.substring(0, 3)
        val middle = "*".repeat(this.length - 7)
        val last = this.substring(this.length - 4, this.length)
        "$front-$middle-$last"
    }
}

internal fun String.toMaskBirthDay(): String {
    return if (this.isEmpty()) {
        this
    } else {
        if (this.length < 4) {
            return this
        }
        "${this.substring(0, 4)}-**-**"
    }
}
