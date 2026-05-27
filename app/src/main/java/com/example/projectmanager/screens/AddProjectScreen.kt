package com.example.projectmanager.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectmanager.viewmodel.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectScreen(
    viewModel: ProjectViewModel,
    onNavigateBack: () -> Unit
) {
    var title       by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate     by remember { mutableStateOf("") }
    var titleError  by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Топ-бар ───────────────────────────────────────────────────
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
                    text = "New Project",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Форма ─────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Project Title
                NeuTextField(
                    value          = title,
                    onValueChange  = { title = it; titleError = false },
                    placeholder    = "Project Title",
                    isError        = titleError,
                    trailingIcon   = {
                        Text("⋮", fontSize = 20.sp, color = TextGray)
                    }
                )
                if (titleError) {
                    Text(
                        "Title is required",
                        fontSize = 12.sp,
                        color = Color(0xFFE53935),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                // Description
                NeuTextField(
                    value         = description,
                    onValueChange = { description = it },
                    placeholder   = "Description"
                )

                // Due Date
                NeuTextField(
                    value         = dueDate,
                    onValueChange = { dueDate = it },
                    placeholder   = "Due Date"
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Кнопки ────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Create
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(BlueAccent)
                        .clickable {
                            if (title.isBlank()) {
                                titleError = true
                            } else {
                                viewModel.addProject(title.trim(), description.trim())
                                onNavigateBack()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Create", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }

                // Cancel
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(AppBg)
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cancel", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextGray)
                }
            }
        }
    }
}

// ─── Кастомне поле вводу у стилі прикладу ──────────────────────────────────
@Composable
fun NeuTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = if (isError) 0.dp else 4.dp,
                shape        = RoundedCornerShape(16.dp),
                ambientColor = Color(0x22000000)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(if (isError) Color(0xFFFFF0F0) else CardWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value         = value,
                onValueChange = onValueChange,
                placeholder   = placeholder,
                modifier      = Modifier.weight(1f),
                singleLine    = singleLine
            )
            trailingIcon?.invoke()
        }
    }
}

// ─── Простий TextField без зайвих рамок ─────────────────────────────────────
@Composable
fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    androidx.compose.foundation.text.BasicTextField(
        value       = value,
        onValueChange = onValueChange,
        singleLine  = singleLine,
        modifier    = modifier.padding(vertical = 16.dp),
        textStyle   = androidx.compose.ui.text.TextStyle(
            fontSize = 15.sp,
            color    = TextDark,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Default
        ),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(placeholder, fontSize = 15.sp, color = TextGray)
            }
            innerTextField()
        }
    )
}