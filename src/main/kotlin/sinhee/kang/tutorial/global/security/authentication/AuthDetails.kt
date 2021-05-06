package sinhee.kang.tutorial.global.security.authentication

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import sinhee.kang.tutorial.domain.user.domain.user.User
import java.util.*

class AuthDetails(
        private val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = ArrayList()

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.nickname

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}