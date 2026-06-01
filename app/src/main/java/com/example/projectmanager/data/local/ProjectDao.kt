package com.example.projectmanager.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    // Отримати всі проекти як Flow (автооновлення UI)
    @Query("SELECT * FROM projects ORDER BY id ASC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    // Отримати один проект за ID
    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: Int): ProjectEntity?

    // Додати новий проект
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    // Оновити існуючий проект
    @Update
    suspend fun updateProject(project: ProjectEntity)

    // Видалити проект
    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    // Видалити проект за ID (зручніший варіант)
    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Int)
}