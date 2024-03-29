package com.motompro.todolist.api.service

import com.motompro.todolist.api.entity.User
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.stereotype.Component
import java.util.function.Supplier

@Component
class UserSecurity : AuthorizationManager<RequestAuthorizationContext> {

    override fun check(authenticationSupplier: Supplier<Authentication>, ctx: RequestAuthorizationContext): AuthorizationDecision? {
        val username = ctx.variables["username"] ?: return AuthorizationDecision(false)
        val authentication = authenticationSupplier.get()
        return AuthorizationDecision(hasUsername(authentication, username))
    }

    fun hasUsername(authentication: Authentication, username: String): Boolean {
        val principal = authentication.principal
        if (principal !is User) return false
        return principal.username == username
    }
}
