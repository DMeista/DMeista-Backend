package sinhee.kang.tutorial.domain.user.domain.user.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.user.domain.user.User

@Repository
interface UserRepository : CrudRepository<User, Int?> {
    fun findByEmail(email: String): User?
    fun findByNickname(nickname: String): User?
}