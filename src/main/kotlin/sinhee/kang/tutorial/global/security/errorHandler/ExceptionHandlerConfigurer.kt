package sinhee.kang.tutorial.global.security.errorHandler

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import sinhee.kang.tutorial.global.security.jwt.JwtTokenFilter
import sinhee.kang.tutorial.infra.api.slack.service.SlackReportService

class ExceptionHandlerConfigurer(
        private val slackReportService: SlackReportService
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(httpSecurity: HttpSecurity) {
        val handlerFilter = ExceptionHandlerFilter(slackReportService)
        httpSecurity.addFilterBefore(handlerFilter, JwtTokenFilter::class.java)
    }
}
