package sinhee.kang.tutorial.global.config.security.exception

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import sinhee.kang.tutorial.global.config.security.jwt.JwtTokenFilter
import sinhee.kang.tutorial.infra.api.slack.SlackSenderManager

class ExceptionConfigurer(
        private val slackSenderManager: SlackSenderManager
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(httpSecurity: HttpSecurity) {
        val handlerFilter = ExceptionHandlerFilter(slackSenderManager)
        httpSecurity.addFilterBefore(handlerFilter, JwtTokenFilter::class.java)
    }

}