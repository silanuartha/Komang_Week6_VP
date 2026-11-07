package com.example.komang_week6_vp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.komang_week6_vp.viewmodel.CampusViewModel

@Composable
fun DashboardScreen(viewModel: CampusViewModel) {
    val communities by viewModel.communities.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val joinedCommunities = communities.filter { it.isJoined }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text("CampusTech Hub", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Student Profile & Portfolio Dashboard", fontSize = 14.sp, color = Color.Gray)
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E88E5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("AP", color = Color(0xFF1E88E5), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("MAHASISWA AKTIF", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("Aditya Pratama", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Teknik Informatika • Angkatan 2022", color = Color.White, fontSize = 12.sp)
                        Text("NIM: 22001045", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.Group, contentDescription = null, tint = Color.Blue)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${joinedCommunities.size}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Joined Clubs", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Komunitas yang Diikuti", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.Build, contentDescription = null, tint = Color.Magenta)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${projects.size}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Total Projects", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Seluruh Proyek Dibuat", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("⭐ Semua Komunitas yang Diikuti (${joinedCommunities.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Status: Joined", fontSize = 12.sp, color = Color.Blue)
            }
        }
        if (joinedCommunities.isEmpty()) {
            item {
                Text("Belum bergabung dengan komunitas", color = Color.Gray, modifier = Modifier.padding(vertical = 16.dp))
            }
        } else {
            items(joinedCommunities) { community ->
                val projectCount = projects.count { it.communityId == community.id }
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(community.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("${community.description.take(40)}... • $projectCount Proyek", fontSize = 12.sp, color = Color.Gray)
                        }
                        Text("Joined ✔", color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Semua Proyek Mahasiswa (${projects.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Total: ${projects.size} Proyek", fontSize = 12.sp, color = Color.Blue)
            }
        }
        if (projects.isEmpty()) {
            item {
                Text("Belum ada proyek yang dicatat", color = Color.Gray, modifier = Modifier.padding(vertical = 16.dp))
            }
        } else {
            items(projects) { project ->
                val communityName = communities.find { it.id == project.communityId }?.name ?: ""
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(communityName, fontSize = 10.sp, color = Color.Blue, fontWeight = FontWeight.Bold)
                            Text(project.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(project.techStacks.joinToString(" • "), fontSize = 12.sp, color = Color.Gray)
                        }
                        if (project.isCompleted) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Green)
                        }
                    }
                }
            }
        }
    }
}