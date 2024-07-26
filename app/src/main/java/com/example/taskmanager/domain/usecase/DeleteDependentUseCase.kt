package com.example.taskmanager.domain.usecase

import com.example.taskmanager.data.repository.TaskRepository
import javax.inject.Inject

class DeleteDependentUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Int) {
        repository.deleteDependent(id)
    }
}