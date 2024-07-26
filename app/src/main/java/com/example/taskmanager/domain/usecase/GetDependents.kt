package com.example.taskmanager.domain.usecase

import com.example.taskmanager.data.repository.TaskRepository
import com.example.taskmanager.domain.model.DependencyTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDependents @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(parentId: Int): Flow<List<DependencyTask>> =
        repository.getDependentTasks(parentId)

}