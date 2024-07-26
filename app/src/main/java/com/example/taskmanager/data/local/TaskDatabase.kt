package com.example.taskmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.taskmanager.dao.DependencyDao
import com.example.taskmanager.dao.TaskDao
import com.example.taskmanager.domain.model.DependencyTask
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.utils.convertJsonToModel
import com.example.taskmanager.utils.convertObjectToString

@Database(entities = [Task::class,DependencyTask::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun taskDependentDao():DependencyDao
}

class Converters {
    @TypeConverter
    fun fromList(value: List<Int>): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun toList(value: String): List<Int> {
        if (value.isEmpty()) {
            return emptyList()
        }

        return value.split(",").map { it.toInt() }
    }
}