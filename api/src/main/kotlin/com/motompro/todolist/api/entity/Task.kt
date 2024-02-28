package com.motompro.todolist.api.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import org.hibernate.annotations.JdbcTypeCode
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Types
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "tasks")
class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    val id: UUID = UUID.randomUUID(),
    var description: String,
    var done: Boolean = false,
    @Temporal(TemporalType.TIMESTAMP)
    val creationDate: LocalDateTime = LocalDateTime.now(),
    @Temporal(TemporalType.TIMESTAMP)
    var dueDate: LocalDateTime,
    val ownerUsername: String,
) {

    override fun toString(): String {
        return "Task[id=$id, description=$description, done=$done, creationDate=$creationDate, dueDate=$dueDate, ownerUsername=$ownerUsername]"
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
