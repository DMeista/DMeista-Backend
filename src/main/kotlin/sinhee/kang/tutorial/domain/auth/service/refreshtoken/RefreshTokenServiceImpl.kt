package sinhee.kang.tutorial.domain.auth.service.refreshtoken

import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.domain.refreshToken.RefreshToken
import sinhee.kang.tutorial.domain.auth.domain.refreshToken.repository.RefreshTokenRepository

@Service
class RefreshTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository
) : RefreshTokenService {
    override fun save(refreshToken: RefreshToken) {
        refreshTokenRepository.save(refreshToken)
    }
}
