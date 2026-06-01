package com.example.projectmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.repository.ProjectRepository
import com.example.projectmanager.model.Project
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val repository: ProjectRepository
) : ViewModel() {

    // StateFlow з бази даних — автоматично оновлює UI
    val projects: StateFlow<List<Project>> = repository.allProjects
        .stateIn(
            scope         = viewModelScope,
            started       = SharingStarted.WhileSubscribed(5000),
            initialValue  = emptyList()
        )

    fun addProject(title: String, description: String) {
        viewModelScope.launch {
            repository.addProject(title, description)
        }
    }

    fun deleteProject(id: Int) {
        viewModelScope.launch {
            repository.deleteProject(id)
        }
    }

    fun updateProgress(id: Int, progress: Float) {
        viewModelScope.launch {
            repository.updateProgress(id, progress)
        }
    }

    fun updateProjectInfo(id: Int, title: String, description: String) {
        viewModelScope.launch {
            repository.updateProjectInfo(id, title, description)
        }
    }

    // ── ViewModelFactory (потрібен, бо ViewModel тепер має параметр) ───────
    class Factory(private val repository: ProjectRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProjectViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}