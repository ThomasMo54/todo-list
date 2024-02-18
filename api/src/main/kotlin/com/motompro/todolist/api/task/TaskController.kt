package com.motompro.todolist.api.task

import com.motompro.todolist.api.entity.Task
import com.motompro.todolist.api.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/task")
class TaskController {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @GetMapping
    fun findById(@RequestParam(value = "id") id: UUID): ResponseEntity<Task> {
        val task = taskRepository.findById(id).getOrNull() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        return ResponseEntity(task, HttpStatus.OK)
    }
}
