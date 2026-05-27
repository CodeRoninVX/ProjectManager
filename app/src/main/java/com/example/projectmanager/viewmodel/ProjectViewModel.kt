package com.example.projectmanager.viewmodel

import androidx.lifecycle.ViewModel
import com.example.projectmanager.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProjectViewModel : ViewModel() {

    private val _projects = MutableStateFlow(
        listOf(
            Project(1, "Mobile App Redesign",  "iOS/Android redesign project", 0.85f),
            Project(2, "Website Development",  "Corporate website",            0.30f)
        )
    )
    val projects: StateFlow<List<Project>> = _projects

    fun addProject(title: String, description: String) {
        val newId = (_projects.value.maxOfOrNull { it.id } ?: 0) + 1
        _projects.value = _projects.value + Project(newId, title, description, 0f)
    }

    fun deleteProject(id: Int) {
        _projects.value = _projects.value.filter { it.id != id }
    }

    fun updateProgress(id: Int, progress: Float) {
        _projects.value = _projects.value.map {
            if (it.id == id) it.copy(progress = progress) else it
        }
    }

    // ← НОВИЙ МЕТОД: оновлює назву і опис
    fun updateProjectInfo(id: Int, title: String, description: String) {
        _projects.value = _projects.value.map {
            if (it.id == id) it.copy(title = title, description = description) else it
        }
    }

    fun getProjectById(id: Int): Project? =
        _projects.value.find { it.id == id }
}