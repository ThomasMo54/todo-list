package com.motompro.todolist.api.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import java.util.Date
import java.util.UUID

@Entity
class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,
    var description: String,
    var done: Boolean,
    @Temporal(TemporalType.DATE)
    val creationDate: Date,
    @Temporal(TemporalType.DATE)
    var dueDate: Date,
) {

    override fun toString(): String {
        return "Task[id=$id, description=$description, done=$done, creationDate=$creationDate, dueDate=$dueDate]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
