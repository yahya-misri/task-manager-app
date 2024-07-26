package com.example.taskmanager.data.repository

import com.example.taskmanager.dao.DependencyDao
import com.example.taskmanager.dao.TaskDao
import com.example.taskmanager.data.repository.TaskRepository
import com.example.taskmanager.domain.model.DependencyTask
import com.example.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskDependencyDao: DependencyDao
) : TaskRepository {
    override fun getTasks(): Flow<List<Task>> = taskDao.getTasks()

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    override suspend fun addDependent(dependencyTask: DependencyTask) {
        taskDependencyDao.insertDependencyTask(dependencyTask)
    }

    override fun getDependentTasks(parentId: Int): Flow<List<DependencyTask>> =taskDependencyDao.getDependencies(parentId)
    override suspend fun check(ids: List<Int>): List<Task> =taskDao.check(ids)
    override suspend fun deleteDependent(id: Int) {
       taskDependencyDao.deleteDependencyTask(id)
    }
}








