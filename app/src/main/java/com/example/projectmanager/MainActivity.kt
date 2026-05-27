package com.example.projectmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.projectmanager.navigation.AppNavigation
import com.example.projectmanager.ui.theme.ProjectManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectManagerTheme {
                AppNavigation()
            }
        }
    }
}