package sinhee.kang.tutorial.global.security.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationFacade {

    fun getUserName(): String {
        val auth = getAuthentication()
        return if (auth.principal is AuthDetails) {
            (auth.principal as AuthDetails).username
        }
        else {
            getAuthentication().name
        }
    }

    private fun getAuthentication(): Authentication = SecurityContextHolder.getContext().authentication
}
