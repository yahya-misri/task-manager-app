package com.example.taskmanager.di

import android.content.Context
import androidx.room.Room
import com.example.taskmanager.dao.DependencyDao
import com.example.taskmanager.dao.TaskDao
import com.example.taskmanager.data.local.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TaskDbModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        return Room.databaseBuilder(
            appContext,
            TaskDatabase::class.java,
            "task_database"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideDependentTaskDao(database: TaskDatabase): DependencyDao {
        return database.taskDependentDao()
    }
}