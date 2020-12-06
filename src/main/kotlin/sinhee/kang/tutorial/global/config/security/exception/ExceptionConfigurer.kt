package sinhee.kang.tutorial.global.config.security.exception

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import sinhee.kang.tutorial.global.config.security.jwt.JwtTokenFilter

class ExceptionConfigurer : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(httpSecurity: HttpSecurity) {
        val handlerFilter = ExceptionHandlerFilter()
        httpSecurity.addFilterBefore(handlerFilter, JwtTokenFilter::class.java)
    }

}