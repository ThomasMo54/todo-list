package com.motompro.todolist.api.repository

import com.motompro.todolist.api.entity.Task
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface TaskRepository : CrudRepository<Task, UUID> {

    override fun findById(id: UUID): Optional<Task>

    fun findAllByOwnerUsername(ownerUsername: String): List<Task>
}
