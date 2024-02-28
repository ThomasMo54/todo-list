package com.motompro.todolist.api.controller

import com.motompro.todolist.api.entity.Task
import com.motompro.todolist.api.entity.User
import com.motompro.todolist.api.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/tasks")
class TaskController {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @PostMapping
    fun create(@RequestBody taskRequest: CreateTaskRequest): ResponseEntity<Any> {
        val username = getAuthenticatedUserName() ?: return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        val task = Task(
            UUID.randomUUID(),
            taskRequest.description,
            taskRequest.done,
            LocalDateTime.now(),
            LocalDateTime.ofInstant(Instant.ofEpochMilli(taskRequest.dueDate), ZoneOffset.UTC),
            username,
        )
        taskRepository.save(task)
        return ResponseEntity(null, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable(value = "id") id: UUID): ResponseEntity<Task> {
        val task = taskRepository.findById(id).getOrNull() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!isAuthorizedToAccessTask(task)) return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        return ResponseEntity(task, HttpStatus.OK)
    }

    @PatchMapping("/{id}/description")
    fun setDescription(@PathVariable(value = "id") id: UUID, @RequestBody updates: Map<String, Any>): ResponseEntity<Any> {
        val task = taskRepository.findById(id).getOrNull() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!isAuthorizedToAccessTask(task)) return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        val description = (updates["description"] as? String) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        task.description = description
        taskRepository.save(task)
        return ResponseEntity(null, HttpStatus.OK)
    }

    @PatchMapping("/{id}/done")
    fun setDone(@PathVariable(value = "id") id: UUID, @RequestBody updates: Map<String, Any>): ResponseEntity<Any> {
        val task = taskRepository.findById(id).getOrNull() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!isAuthorizedToAccessTask(task)) return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        val done = (updates["done"] as? Boolean) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        task.done = done
        taskRepository.save(task)
        return ResponseEntity(null, HttpStatus.OK)
    }

    @PatchMapping("/{id}/dueDate")
    fun setDueDate(@PathVariable(value = "id") id: UUID, @RequestBody updates: Map<String, Any>): ResponseEntity<Any> {
        val task = taskRepository.findById(id).getOrNull() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!isAuthorizedToAccessTask(task)) return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        val dueDateInMillis = (updates["dueDate"] as? Long) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        task.dueDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(dueDateInMillis), ZoneOffset.UTC)
        taskRepository.save(task)
        return ResponseEntity(null, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") id: UUID): ResponseEntity<Any> {
        val task = taskRepository.findById(id).getOrNull() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!isAuthorizedToAccessTask(task)) return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        taskRepository.deleteById(id)
        return ResponseEntity(null, HttpStatus.OK)
    }

    private fun getAuthenticatedUserName(): String? {
        val principal = SecurityContextHolder.getContext().authentication.principal ?: return null
        if (principal !is User) return null
        return principal.username
    }

    private fun isAuthorizedToAccessTask(task: Task): Boolean {
        return getAuthenticatedUserName() == task.ownerUsername
    }

    class CreateTaskRequest(
        val description: String,
        val done: Boolean,
        val dueDate: Long,
    )
}
