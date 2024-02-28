package com.motompro.todolist.api.controller

import com.motompro.todolist.api.entity.Task
import com.motompro.todolist.api.repository.TaskRepository
import com.motompro.todolist.api.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var taskRepository: TaskRepository

    @GetMapping("/{user}/tasks")
    fun tasks(@PathVariable(value = "user") username: String): ResponseEntity<List<Task>> {
        val tasks = taskRepository.findAllByOwnerUsername(username)
        return ResponseEntity(tasks, HttpStatus.OK)
    }
}
