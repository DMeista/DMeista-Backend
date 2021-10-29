 
package sinhee.kang.tutorial.domain.user.repository.user

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sinhee.kang.tutorial.domain.user.entity.user.User

@Repository
interface UserRepository : CrudRepository<User, Int> {
    fun findByEmail(email: String): User?

    fun findByNickname(nickname: String): User?

    fun existsByEmail(email: String): Boolean

    fun existsByNickname(nickname: String): Boolean
}
