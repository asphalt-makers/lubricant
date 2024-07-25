package org.asphalt.lubricant.config.scope

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.env.Environment
import org.springframework.expression.AccessException
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class BlockProfileInterceptor(
    private val env: Environment,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if ((handler is HandlerMethod).not()) {
            return true
        }

        val handlerMethod = handler as HandlerMethod
        val hasClass = handlerMethod.method.declaringClass.isAnnotationPresent(BlockProfile::class.java)

        val blockProfile =
            if (hasClass) {
                handlerMethod.method.declaringClass.getAnnotation(BlockProfile::class.java)
            } else {
                handlerMethod.getMethodAnnotation(BlockProfile::class.java)
            }

        if (blockProfile == null || env.activeProfiles.isEmpty()) {
            return true
        }

        val profile = env.activeProfiles.first()

        if (blockProfile.profiles.contains(profile)) {
            throw AccessException("Can not access this environment!!")
        }

        return true
    }
}
