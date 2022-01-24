package sinhee.kang.tutorial.application.service.auth

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import sinhee.kang.tutorial.infra.util.authentication.bean.RequestAuthScope
import sinhee.kang.tutorial.application.service.token.JwtTokenProvider
import sinhee.kang.tutorial.domain.user.repository.user.UserRepository

@Aspect
@Component
class AuthenticationAspect(
    private val userRepository: UserRepository,
    private val tokenProvider: JwtTokenProvider,
    private val requestAuthScope: RequestAuthScope
) {
    @Before("@annotation(sinhee.kang.tutorial.infra.util.authentication.annotation.Authentication)")
    private fun before() {
        val user = tokenProvider.getAccessToken()
            ?.run { tokenProvider.verifyToken(this) }
            ?.let { userRepository.findByNickname(it) }

        requestAuthScope.user = user
    }
}
