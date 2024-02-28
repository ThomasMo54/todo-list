package com.motompro.todolist.api.controller

import com.motompro.todolist.api.entity.User
import com.motompro.todolist.api.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest, request: HttpServletRequest, response: HttpServletResponse) {
        val token = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        val authentication = authenticationManager.authenticate(token)
        val context = SecurityContextHolder.getContext()
        context.authentication = authentication
        val session = request.getSession(true)
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context)
    }

    @PostMapping("/register")
    fun register(name: String, pass: String): ResponseEntity<Any> {
        val existingUser = userRepository.findByUsername(name).getOrNull()
        if (existingUser != null) {
            return ResponseEntity(null, HttpStatus.CONFLICT)
        }
        userRepository.save(User(name, passwordEncoder.encode(pass)))
        return ResponseEntity(null, HttpStatus.OK)
    }

    class LoginRequest(
        val username: String,
        val password: String,
    )
}
