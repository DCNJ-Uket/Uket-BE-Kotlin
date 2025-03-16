package uket.gateway.jwt.filter

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JWTTokenAuthentication(
    private val accessToken: String,
    private val authenticationUserInfo: AuthenticationUserInfo,
) : Authentication {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        val collection = mutableListOf<GrantedAuthority>()
        collection.add(GrantedAuthority(authenticationUserInfo::role))

        return collection
    }

    override fun getName(): String {
        return authenticationUserInfo.userId.toString()
    }

    override fun getCredentials(): Any {
        return accessToken
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        return ""
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        return
    }
}
