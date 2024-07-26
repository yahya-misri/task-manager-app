package com.example.taskmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dependency")
data class DependencyTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var parentTaskId: Int = 0,
    var dependentId: Int = 0
)
