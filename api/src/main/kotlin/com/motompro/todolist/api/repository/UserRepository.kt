package com.motompro.todolist.api.repository

import com.motompro.todolist.api.entity.User
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface UserRepository : CrudRepository<User, String> {

    fun findByUsername(username: String): Optional<User>
}
