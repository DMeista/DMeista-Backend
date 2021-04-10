package sinhee.kang.tutorial.global.security.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import sinhee.kang.tutorial.domain.user.domain.user.User
import java.util.*

class AuthDetails(
        private var user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return ArrayList()
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.nickname
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}