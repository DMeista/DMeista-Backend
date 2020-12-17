package sinhee.kang.tutorial.global.config.requestLog

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import sinhee.kang.tutorial.global.config.security.exception.ExceptionHandlerFilter

class RequestLogConfigurer: SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
    override fun configure(builder: HttpSecurity?) {
        val filter = RequestLogFilter()
        builder?.addFilterBefore(filter, ExceptionHandlerFilter::class.java)
    }
}