package sinhee.kang.tutorial.global.security.errorHandler

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import sinhee.kang.tutorial.global.security.jwt.JwtTokenFilter
import sinhee.kang.tutorial.infra.api.slack.service.SlackMessageService

class ExceptionHandlerConfigurer(
        private val slackMessageService: SlackMessageService
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(httpSecurity: HttpSecurity) {
        val handlerFilter = ExceptionHandlerFilter(slackMessageService)
        httpSecurity.addFilterBefore(handlerFilter, JwtTokenFilter::class.java)
    }
}
