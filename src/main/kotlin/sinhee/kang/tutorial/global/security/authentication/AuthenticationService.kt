package sinhee.kang.tutorial.global.security.authentication

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationService {

    fun getUserName(): String {
        val authentication: Authentication = getAuthentication()

        return authentication.run {
            if (principal is AuthDetails)
                (principal as AuthDetails).username
            else name
        }
    }

    private fun getAuthentication(): Authentication =
        SecurityContextHolder.getContext().authentication
}
