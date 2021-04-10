package sinhee.kang.tutorial.global.security.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationFacade {

    fun getAuthentication(): Authentication {
        return SecurityContextHolder.getContext().authentication
    }

    fun getUserName(): String {
        val auth: Authentication = this.getAuthentication()
        return if (auth.principal is AuthDetails) {
            (auth.principal as AuthDetails).username
        }
        else {
            getAuthentication().name
        }
    }
}