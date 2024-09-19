package org.asphalt.lubricant.config

import org.asphalt.lubricant.common.RoleHeader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.Optional

@Configuration
@EnableJpaAuditing
class AuditConfig {
    @Bean
    fun auditorAware(): AuditorAware<String> =
        AuditorAware {
            val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
            val request = requestAttributes?.request

            val auditor =
                request?.let {
                    val adminId = it.getHeader(RoleHeader.Admin.KEY)
                    val userId = it.getHeader(RoleHeader.User.KEY)

                    adminId?.let { "A:$adminId" } ?: userId?.let { "U:$userId" } ?: UNKNOWN_AUDITOR
                } ?: UNKNOWN_AUDITOR

            Optional.of(auditor)
        }

    companion object {
        const val UNKNOWN_AUDITOR = "UNKNOWN"
    }
}
