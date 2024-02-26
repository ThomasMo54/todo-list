package com.motompro.todolist.api.service

import com.motompro.todolist.api.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class DatabaseUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) throw UsernameNotFoundException("User not present")
        val user = userRepository.findByUsername(username).getOrNull()
            ?: throw UsernameNotFoundException("User not present")
        return user
    }
}
