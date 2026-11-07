package com.example.komang_week6_vp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.komang_week6_vp.viewmodel.CampusViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProjectDetailScreen(
    projectId: String,
    viewModel: CampusViewModel,
    onNavigateBack: () -> Unit
) {
    val communities by viewModel.communities.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val project = projects.find { it.id == projectId }
    val community = communities.find { it.id == project?.communityId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Project Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (project != null && community != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D47A1))) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Surface(color = Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp)) {
                            Text(community.name, color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(project.title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Mark as Done", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            val statusText = if (project.isCompleted) "Completed" else "In Progress"
                            val statusColor = if (project.isCompleted) Color(0xFF4CAF50) else Color(0xFFFFC107)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Status: ", fontSize = 12.sp, color = Color.Gray)
                                Surface(color = if (project.isCompleted) Color(0xFFE8F5E9) else Color(0xFFFFF8E1), shape = RoundedCornerShape(4.dp)) {
                                    Text("★ $statusText", color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }
                        Switch(checked = project.isCompleted, onCheckedChange = { viewModel.toggleProjectStatus(project.id) })
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Deskripsi Lengkap Proyek", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(project.description, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tech Stack Terpilih", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            project.techStacks.forEach { stack ->
                                SuggestionChip(onClick = {}, label = { Text(stack) })
                            }
                        }
                    }
                }
            }
        }
    }
}