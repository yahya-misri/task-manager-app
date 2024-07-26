package com.example.taskmanager.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanager.domain.model.DependencyTask
import com.example.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface DependencyDao {
    @Query("SELECT * FROM dependency where parentTaskId=:parentId")
    fun getDependencies(parentId:Int): Flow<List<DependencyTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDependencyTask(dependencyTask: DependencyTask)

    @Update
    suspend fun updateDependencyTask(dependencyTask: DependencyTask)

    @Query("Delete from dependency where parentTaskId=:id")
    suspend fun deleteDependencyTask(id:Int)
}