package sinhee.kang.tutorial.domain.auth.domain.refreshToken.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.auth.domain.refreshToken.RefreshToken

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Int>
