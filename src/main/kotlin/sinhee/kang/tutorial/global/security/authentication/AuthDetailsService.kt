package sinhee.kang.tutorial.global.security.authentication

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.exception.exceptions.notFound.UserNotFoundException

@Service
class AuthDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findByNickname(username)
            ?: throw UserNotFoundException()
        return AuthDetails(user)
    }
}
