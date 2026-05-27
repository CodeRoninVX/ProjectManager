package com.example.projectmanager.model

data class Project(
    val id: Int,
    val title: String,
    val description: String,
    val progress: Float = 0f  // від 0.0 до 1.0 (0% – 100%)
)