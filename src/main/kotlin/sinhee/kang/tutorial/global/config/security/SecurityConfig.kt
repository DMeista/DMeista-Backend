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
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import sinhee.kang.tutorial.global.config.security.exception.ExceptionConfigurer
import sinhee.kang.tutorial.global.config.security.jwt.JwtConfigurer
import sinhee.kang.tutorial.global.config.security.jwt.JwtTokenProvider
import sinhee.kang.tutorial.global.config.requestLog.RequestLogConfigurer
import sinhee.kang.tutorial.infra.api.slack.SlackSenderManager
import javax.servlet.http.HttpServletRequest

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
            .cors().and()
            .formLogin()
                .disable()
            .headers()
            .frameOptions()
                .disable().and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
                .requestMatchers(RequestMatcher {
                        request: HttpServletRequest -> CorsUtils.isPreFlightRequest(request)
                }).permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
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
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration().apply {
            addAllowedOrigin("*")
            addAllowedHeader("*")
            addAllowedMethod("GET, POST, PUT, PATCH")
            allowCredentials = true
            maxAge = 3600L
        }

        UrlBasedCorsConfigurationSource().registerCorsConfiguration("/**", configuration)
        return UrlBasedCorsConfigurationSource()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
