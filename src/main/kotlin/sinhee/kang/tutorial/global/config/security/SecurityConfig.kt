package sinhee.kang.tutorial.global.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import sinhee.kang.tutorial.global.config.security.exception.ExceptionConfigurer
import sinhee.kang.tutorial.global.config.security.jwt.JwtConfigurer
import sinhee.kang.tutorial.global.config.security.jwt.JwtTokenProvider
import sinhee.kang.tutorial.global.config.requestLog.RequestLogConfigurer
import sinhee.kang.tutorial.infra.api.slack.SlackSenderManager

@Configuration
@EnableWebSecurity
class SecurityConfig(
        private var jwtTokenProvider: JwtTokenProvider,
        private var slackSenderManager: SlackSenderManager
) : WebSecurityConfigurerAdapter() {

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .formLogin().disable()
                .headers()
                .frameOptions()
                .disable().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth").permitAll()
                .antMatchers("/post").permitAll()
                .antMatchers("/users").permitAll()
                .antMatchers("/users/password").permitAll()
                .antMatchers("/users/email/password/verify").permitAll()
                .antMatchers("/users/email/verify").permitAll().and()
                .apply(JwtConfigurer(jwtTokenProvider)).and()
                .apply(ExceptionConfigurer(slackSenderManager)).and()
                .apply(RequestLogConfigurer())

    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}