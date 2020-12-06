package sinhee.kang.tutorial.global.config.security.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.config.security.exception.UserNotFoundException

@Service
class AuthDetailsService(
        private var userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findByNickname(username)
                ?: { throw UserNotFoundException() }()
        return AuthDetails(user)
    }
}