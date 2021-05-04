package sinhee.kang.tutorial.global.security.exception

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import sinhee.kang.tutorial.global.security.jwt.JwtTokenFilter
import sinhee.kang.tutorial.infra.api.slack.service.SlackExceptionService

class ExceptionConfigurer(
        private val slackExceptionService: SlackExceptionService
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(httpSecurity: HttpSecurity) {
        val handlerFilter = ExceptionHandlerFilter(slackExceptionService)
        httpSecurity.addFilterBefore(handlerFilter, JwtTokenFilter::class.java)
    }
}
