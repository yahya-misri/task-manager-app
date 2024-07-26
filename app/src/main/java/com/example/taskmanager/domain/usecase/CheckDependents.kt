package com.example.taskmanager.domain.usecase

import com.example.taskmanager.data.repository.TaskRepository
import com.example.taskmanager.domain.model.Task
import javax.inject.Inject

class CheckDependents @Inject constructor(private val repository: TaskRepository) {
    suspend operator  fun invoke(ids: List<Int>): List<Task> = repository.check(ids)
}