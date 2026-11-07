package com.example.komang_week6_vp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.komang_week6_vp.viewmodel.CampusViewModel

@Composable
fun CommunitiesScreen(viewModel: CampusViewModel, onNavigateToProjects: (String) -> Unit) {
    val context = LocalContext.current
    val communities by viewModel.communities.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Communities Directory", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Pilih komunitas untuk membuka Club Projects", fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.Search, contentDescription = null)
        }
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(communities) { community ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        if (community.isJoined) {
                            onNavigateToProjects(community.id)
                        } else {
                            Toast.makeText(context, "Harap bergabung ke komunitas terlebih dahulu!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(community.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("${community.memberCount} Mahasiswa", fontSize = 12.sp, color = Color.Gray)
                            }
                            if (community.isJoined) {
                                OutlinedButton(onClick = { viewModel.toggleCommunityJoin(community.id) }) {
                                    Text("Joined ✔", color = Color.Red)
                                }
                            } else {
                                Button(onClick = { viewModel.toggleCommunityJoin(community.id) }) {
                                    Text("+ Join")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(community.description, fontSize = 14.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (community.isJoined) {
                                Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Blue)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Unlocked • Buka Proyek →", fontSize = 12.sp, color = Color.Blue, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Terkunci (Gate Akses)", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}