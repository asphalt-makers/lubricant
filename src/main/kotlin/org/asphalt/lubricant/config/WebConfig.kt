package org.asphalt.lubricant.config

import org.asphalt.lubricant.util.Jackson
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.format.support.FormattingConversionService
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration
import java.time.format.DateTimeFormatter

@Configuration
@ControllerAdvice
class WebConfig : DelegatingWebMvcConfiguration() {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val objectMapper = Jackson.getMapper()
        converters.add(StringHttpMessageConverter())
        converters.add(MappingJackson2HttpMessageConverter(objectMapper))
        super.configureMessageConverters(converters)
    }

    @Bean
    fun requestResponseLogFilter(): FilterRegistrationBean<RequestResponseLogFilter> =
        FilterRegistrationBean<RequestResponseLogFilter>().apply {
            filter = RequestResponseLogFilter()
        }

    @Bean
    override fun mvcConversionService(): FormattingConversionService {
        val mvcConversionService = super.mvcConversionService()
        val dateTimeRegister = DateTimeFormatterRegistrar()
        dateTimeRegister.setDateFormatter(DateTimeFormatter.ISO_DATE)
        dateTimeRegister.setTimeFormatter(DateTimeFormatter.ISO_TIME)
        dateTimeRegister.setDateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME)
        dateTimeRegister.registerFormatters(mvcConversionService)
        return mvcConversionService
    }
}
