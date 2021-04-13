package sinhee.kang.tutorial.domain.auth.service.refreshtoken

import sinhee.kang.tutorial.domain.auth.domain.refreshToken.RefreshToken

interface RefreshTokenService {
    fun save(refreshToken: RefreshToken)
}
