package sinhee.kang.tutorial.global.security.requestLog

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import sinhee.kang.tutorial.global.security.errorHandler.ExceptionHandlerFilter

class RequestLogConfigurer: SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(httpSecurity: HttpSecurity) {
        val requestLogFilter = RequestLogFilter()
        httpSecurity.addFilterBefore(requestLogFilter, ExceptionHandlerFilter::class.java)
    }
}
