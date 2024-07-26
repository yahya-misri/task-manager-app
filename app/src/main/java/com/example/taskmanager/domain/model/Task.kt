package com.example.taskmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var isComplete: Boolean = false,
    val dependencies: List<Int> = emptyList()
)
