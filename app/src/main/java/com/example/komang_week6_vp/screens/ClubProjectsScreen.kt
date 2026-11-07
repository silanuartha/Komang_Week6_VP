package com.example.komang_week6_vp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.komang_week6_vp.viewmodel.CampusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubProjectsScreen(
    communityId: String,
    viewModel: CampusViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val communities by viewModel.communities.collectAsState()
    val allProjects by viewModel.projects.collectAsState()
    val community = communities.find { it.id == communityId }
    val projects = allProjects.filter { it.communityId == communityId }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${community?.name} Projects") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Project") }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(community?.name ?: "", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Blue)
                            Text("${community?.memberCount} Anggota • ${projects.size} Proyek Aktif", fontSize = 12.sp, color = Color.Gray)
                        }
                        Text("Joined ✔", color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Text("Daftar Proyek Mahasiswa (${projects.size})", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(projects) { project ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToDetail(project.id) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(project.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(project.description.take(50) + "...", fontSize = 12.sp, color = Color.Gray)
                                }
                                IconButton(
                                    onClick = { viewModel.deleteProject(project.id) },
                                    modifier = Modifier.size(32.dp),
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFFFEBEE))
                                ) {
                                    Text("-", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(project.techStacks.joinToString(" • "), fontSize = 10.sp, color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                val statusText = if (project.isCompleted) "Completed" else "In Progress"
                                val statusColor = if (project.isCompleted) Color(0xFF4CAF50) else Color(0xFFFFC107)
                                val bgColor = if (project.isCompleted) Color(0xFFE8F5E9) else Color(0xFFFFF8E1)
                                Surface(color = bgColor, shape = RoundedCornerShape(4.dp)) {
                                    Text("★ $statusText", fontSize = 10.sp, color = statusColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showDialog && community != null) {
            AddProjectDialog(
                communityName = community.name,
                onDismiss = { showDialog = false },
                onSave = { title, desc, stacks ->
                    viewModel.addProject(communityId, title, desc, stacks)
                    showDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddProjectDialog(communityName: String, onDismiss: () -> Unit, onSave: (String, String, List<String>) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val availableStacks = listOf("Kotlin", "Jetpack Compose", "Firebase", "Ktor", "Room", "Python", "Google Maps SDK")
    var selectedStacks by remember { mutableStateOf(setOf<String>()) }
    val isSaveEnabled = title.isNotBlank() && description.isNotBlank() && selectedStacks.isNotEmpty()

    Dialog(onDismissRequest = {}) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                Text("Add Project", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Komunitas: $communityName", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Proyek *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi Proyek (Minimal 3 Baris) *") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Tech Stack (Multi-Selection) *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("${selectedStacks.size} Dipilih", fontSize = 12.sp, color = Color.Blue)
                }
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    availableStacks.forEach { stack ->
                        val isSelected = selectedStacks.contains(stack)
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedStacks = if (isSelected) selectedStacks - stack else selectedStacks + stack },
                            label = { Text(stack) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onSave(title, description, selectedStacks.toList()) }, enabled = isSaveEnabled) { Text("Save") }
                }
            }
        }
    }
}