package sinhee.kang.tutorial.global.security.error

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import sinhee.kang.tutorial.global.security.jwt.JwtTokenFilter
import sinhee.kang.tutorial.infra.api.slack.service.SlackReportService

class ErrorHandlerConfigurer(
        private val slackReportService: SlackReportService
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(httpSecurity: HttpSecurity) {
        val handlerFilter = ErrorHandlerFilter(slackReportService)
        httpSecurity.addFilterBefore(handlerFilter, JwtTokenFilter::class.java)
    }
}
