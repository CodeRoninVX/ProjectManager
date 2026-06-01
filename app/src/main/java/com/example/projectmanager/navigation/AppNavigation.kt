package com.example.projectmanager.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectmanager.data.local.AppDatabase
import com.example.projectmanager.data.repository.ProjectRepository
import com.example.projectmanager.screens.AddProjectScreen
import com.example.projectmanager.screens.DetailsProjectScreen
import com.example.projectmanager.screens.ProjectsListScreen
import com.example.projectmanager.viewmodel.ProjectViewModel

sealed class Screen(val route: String) {
    object ProjectsList : Screen("projects_list")
    object AddProject   : Screen("add_project")
    object Details      : Screen("details/{projectId}") {
        fun createRoute(projectId: Int) = "details/$projectId"
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current

    // Створюємо залежності: DB → DAO → Repository → ViewModel
    val db         = AppDatabase.getInstance(context)
    val repository = ProjectRepository(db.projectDao())
    val viewModel: ProjectViewModel = viewModel(
        factory = ProjectViewModel.Factory(repository)
    )

    val navController = rememberNavController()

    NavHost(
        navController  = navController,
        startDestination = Screen.ProjectsList.route
    ) {
        composable(Screen.ProjectsList.route) {
            ProjectsListScreen(
                viewModel     = viewModel,
                onProjectClick = { projectId ->
                    navController.navigate(Screen.Details.createRoute(projectId))
                },
                onAddClick    = {
                    navController.navigate(Screen.AddProject.route)
                }
            )
        }

        composable(Screen.AddProject.route) {
            AddProjectScreen(
                viewModel      = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route     = Screen.Details.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getInt("projectId") ?: 0
            DetailsProjectScreen(
                projectId      = projectId,
                viewModel      = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onDeleted      = { navController.popBackStack() }
            )
        }
    }
}