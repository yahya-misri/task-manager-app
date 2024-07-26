package com.example.taskmanager.di

import com.example.taskmanager.dao.DependencyDao
import com.example.taskmanager.dao.TaskDao
import com.example.taskmanager.data.repository.TaskRepository
import com.example.taskmanager.data.repository.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao,taskDependencyDao: DependencyDao): TaskRepository {
        return TaskRepositoryImpl(taskDao,taskDependencyDao)
    }


}
