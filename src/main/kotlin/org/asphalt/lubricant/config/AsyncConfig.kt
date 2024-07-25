package org.asphalt.lubricant.config

import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.lang.reflect.Method
import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor

@Configuration
@EnableAsync
class AsyncConfig(
    private val taskExecutionProperties: TaskExecutionProperties,
) : AsyncConfigurer {
    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? = CustomAsyncExceptionHandler()

    override fun getAsyncExecutor(): Executor {
        val executor =
            ThreadPoolTaskExecutor().apply {
                setThreadNamePrefix(taskExecutionProperties.threadNamePrefix)

                corePoolSize = taskExecutionProperties.pool.coreSize
                maxPoolSize = taskExecutionProperties.pool.maxSize
                queueCapacity = taskExecutionProperties.pool.queueCapacity
                keepAliveSeconds =
                    taskExecutionProperties.pool.keepAlive.seconds
                        .toInt()
                setRejectedExecutionHandler(CustomAsyncRejectedExecutionHandler())
            }
        executor.initialize()
        return executor
    }
}

class CustomAsyncExceptionHandler : AsyncUncaughtExceptionHandler {
    private val log = LoggerFactory.getLogger(CustomAsyncExceptionHandler::class.java)

    override fun handleUncaughtException(
        ex: Throwable,
        method: Method,
        vararg params: Any?,
    ) {
        log.error("Method: ${method.name}, Params: $params, Message: ${ex.message}")
    }
}

class CustomAsyncRejectedExecutionHandler : RejectedExecutionHandler {
    private val log = LoggerFactory.getLogger(CustomAsyncRejectedExecutionHandler::class.java)

    override fun rejectedExecution(
        r: Runnable?,
        executor: ThreadPoolExecutor?,
    ) {
        log.error("No more requests can be processed!! [$executor]")
    }
}
