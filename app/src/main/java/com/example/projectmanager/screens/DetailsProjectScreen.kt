package com.example.projectmanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.Canvas
import com.example.projectmanager.viewmodel.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsProjectScreen(
    projectId: Int,
    viewModel: ProjectViewModel,
    onNavigateBack: () -> Unit,
    onDeleted: () -> Unit
) {
    val projects by viewModel.projects.collectAsState()
    val project  = projects.find { it.id == projectId }

    if (project == null) { onDeleted(); return }

    // Локальний стан слайдера
    var sliderValue by remember(project.id) { mutableFloatStateOf(project.progress) }
    // Режим редагування
    var editMode    by remember { mutableStateOf(false) }
    var editTitle   by remember(project.id) { mutableStateOf(project.title) }
    var editDesc    by remember(project.id) { mutableStateOf(project.description) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Топ-бар ─────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 52.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(CardWhite)
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Project Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Кругловий прогрес ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .shadow(6.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardWhite)
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressCard(progress = sliderValue)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Назва / опис (або поля редагування) ───────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(CardWhite)
                    .padding(18.dp)
            ) {
                Column {
                    if (editMode) {
                        OutlinedTextField(
                            value = editTitle,
                            onValueChange = { editTitle = it },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BlueAccent,
                                focusedLabelColor  = BlueAccent
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = editDesc,
                            onValueChange = { editDesc = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BlueAccent,
                                focusedLabelColor  = BlueAccent
                            )
                        )
                    } else {
                        Text(
                            text = "Current Sprint",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = project.title,
                            fontSize = 14.sp,
                            color = TextGray
                        )
                        if (project.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = project.description,
                                fontSize = 13.sp,
                                color = TextGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Слайдер прогресу ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(CardWhite)
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Next Steps", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Text(
                            "${(sliderValue * 100).toInt()}%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlueAccent
                        )
                    }
                    Slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        onValueChangeFinished = {
                            viewModel.updateProgress(project.id, sliderValue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor        = BlueAccent,
                            activeTrackColor  = BlueAccent,
                            inactiveTrackColor = Color(0xFFDDE1EC)
                        )
                    )
                    // Чекбокс-підказки
                    CheckRow(label = "QA Testing",          checked = sliderValue >= 0.4f)
                    CheckRow(label = "User Feedback Round",  checked = sliderValue >= 0.7f)
                    CheckRow(label = "Deployment Prep",      checked = sliderValue >= 0.9f)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Кнопки Edit / Delete ──────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Edit / Save
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(6.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(BlueAccent)
                        .clickable {
                            if (editMode) {
                                // Зберегти зміни
                                viewModel.updateProjectInfo(project.id, editTitle, editDesc)
                                viewModel.updateProgress(project.id, sliderValue)
                            }
                            editMode = !editMode
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (editMode) "Save" else "Edit",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                // Delete
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEAEDF2))
                        .clickable {
                            viewModel.deleteProject(project.id)
                            onDeleted()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Delete",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

// ─── Кругловий прогрес ──────────────────────────────────────────────────────
@Composable
fun CircularProgressCard(progress: Float) {
    val percent = (progress * 100).toInt()
    val sweepAngle = progress * 360f

    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(160.dp)) {
            // Фон кола
            drawArc(
                color       = Color(0xFFDDE1EC),
                startAngle  = -90f,
                sweepAngle  = 360f,
                useCenter   = false,
                style       = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            )
            // Прогрес
            drawArc(
                brush       = Brush.sweepGradient(
                    listOf(Color(0xFF2979FF), Color(0xFF5EC8F2))
                ),
                startAngle  = -90f,
                sweepAngle  = sweepAngle,
                useCenter   = false,
                style       = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$percent%",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(listOf(Color(0xFF2979FF), Color(0xFF5C9FFF)))
                    )
                    .size(100.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text("Completed", fontSize = 13.sp, color = TextGray, fontWeight = FontWeight.Medium)
        }
    }
}

// ─── Рядок з чекбоксом ──────────────────────────────────────────────────────
@Composable
fun CheckRow(label: String, checked: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(if (checked) BlueAccent else Color(0xFFDDE1EC)),
            contentAlignment = Alignment.Center
        ) {
            if (checked) Text("✓", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(label, fontSize = 13.sp, color = if (checked) TextDark else TextGray)
    }
}