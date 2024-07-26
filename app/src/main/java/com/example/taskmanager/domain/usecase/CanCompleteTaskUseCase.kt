package com.example.taskmanager.domain.usecase

import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.data.repository.TaskRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CanCompleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task): Boolean {
        val tasks = repository.getTasks().first()
        return task.dependencies.all { depId ->
            tasks.find { it.id == depId }?.isComplete == true
        }
    }
}