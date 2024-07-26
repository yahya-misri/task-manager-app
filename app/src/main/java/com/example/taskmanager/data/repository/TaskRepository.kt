package com.example.taskmanager.data.repository

import com.example.taskmanager.domain.model.DependencyTask
import com.example.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun addDependent(dependencyTask: DependencyTask)
    fun getDependentTasks(parentId:Int): Flow<List<DependencyTask>>
    suspend fun check(ids:List<Int>) :List<Task>
    suspend fun deleteDependent(id:Int)


}