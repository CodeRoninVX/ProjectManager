package com.example.projectmanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectmanager.model.Project
import com.example.projectmanager.viewmodel.ProjectViewModel

// ─── Кольори ───────────────────────────────────────────────────────────────
val AppBg      = Color(0xFFEAEDF2)
val CardWhite  = Color(0xFFFFFFFF)
val BlueAccent = Color(0xFF2979FF)
val TextDark   = Color(0xFF1C2340)
val TextGray   = Color(0xFF9098B1)
val YellowDot  = Color(0xFFFFC107)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsListScreen(
    viewModel: ProjectViewModel,
    onProjectClick: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    val projects by viewModel.projects.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Заголовок ─────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppBg)
                    .padding(top = 52.dp, bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Projects",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            // ── Список карток ─────────────────────────────────────────────
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(projects) { project ->
                    val isFirst = projects.indexOf(project) == 0
                    ProjectCard(
                        project   = project,
                        isHighlight = isFirst,
                        onClick   = { onProjectClick(project.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(90.dp)) }
            }
        }

        // ── FAB "+" ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .shadow(12.dp, CircleShape)
                    .clip(CircleShape)
                    .background(BlueAccent)
                    .clickable { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project, isHighlight: Boolean, onClick: () -> Unit) {
    val percent = (project.progress * 100).toInt()

    if (isHighlight) {
        // Синя карточка (перший проект)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF2979FF), Color(0xFF5C9FFF))
                    )
                )
                .clickable { onClick() }
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Іконка-плашка
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📱", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(project.title, fontSize = 16.sp, fontWeight = FontWeight.Bold,  color = Color.White)
                    Text(
                        project.description.ifBlank { "No description" },
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Прогрес-бар
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White.copy(alpha = 0.3f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(project.progress.coerceIn(0f, 1f))
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color.White)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Жовтий чекмарк
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(YellowDot),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    } else {
        // Біла карточка (інші проекти)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(CardWhite)
                .clickable { onClick() }
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark,
                        modifier = Modifier.weight(1f)
                    )
                    // Жовтий глобус / три крапки
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(YellowDot.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🌐", fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                // Прогрес
                Text("$percent%", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextDark)
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFDDE1EC))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(project.progress.coerceIn(0f, 1f))
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(BlueAccent)
                    )
                }
            }
        }
    }
}