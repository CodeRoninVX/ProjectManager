package com.example.projectmanager.data.repository

import com.example.projectmanager.data.local.ProjectDao
import com.example.projectmanager.data.local.ProjectEntity
import com.example.projectmanager.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProjectRepository(private val dao: ProjectDao) {

    // ── Маппінг Entity ↔ Model ──────────────────────────────────────────────

    private fun ProjectEntity.toModel() = Project(
        id          = id,
        title       = title,
        description = description,
        progress    = progress
    )

    private fun Project.toEntity() = ProjectEntity(
        id          = id,
        title       = title,
        description = description,
        progress    = progress
    )

    // ── Публічні методи для ViewModel ──────────────────────────────────────

    // Flow з усіма проектами (UI підпишеться через collectAsState)
    val allProjects: Flow<List<Project>> = dao
        .getAllProjects()
        .map { list -> list.map { it.toModel() } }

    suspend fun addProject(title: String, description: String) {
        dao.insertProject(
            ProjectEntity(title = title, description = description, progress = 0f)
        )
    }

    suspend fun deleteProject(id: Int) {
        dao.deleteProjectById(id)
    }

    suspend fun updateProgress(id: Int, progress: Float) {
        val entity = dao.getProjectById(id) ?: return
        dao.updateProject(entity.copy(progress = progress))
    }

    suspend fun updateProjectInfo(id: Int, title: String, description: String) {
        val entity = dao.getProjectById(id) ?: return
        dao.updateProject(entity.copy(title = title, description = description))
    }

    suspend fun getProjectById(id: Int): Project? {
        return dao.getProjectById(id)?.toModel()
    }
}