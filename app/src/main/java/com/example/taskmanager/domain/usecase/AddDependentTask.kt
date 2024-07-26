package com.example.taskmanager.domain.usecase

import com.example.taskmanager.data.repository.TaskRepository
import com.example.taskmanager.domain.model.DependencyTask
import com.example.taskmanager.domain.model.Task
import javax.inject.Inject

class AddDependentTask  @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(dependencyTask: DependencyTask) {
        repository.addDependent(dependencyTask)
    }
}