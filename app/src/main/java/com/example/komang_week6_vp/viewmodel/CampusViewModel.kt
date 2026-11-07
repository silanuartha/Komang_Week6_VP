package com.example.komang_week6_vp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.komang_week6_vp.model.Community
import com.example.komang_week6_vp.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class CampusViewModel : ViewModel() {
    private val _communities = MutableStateFlow(
        listOf(
            Community("1", "GDSC", "Fokus pada ekosistem Google: Android Compose, Flutter, Firebase, dan Google Cloud Platform.", 142),
            Community("2", "CyberSecurity Guild", "Eksplorasi ethical hacking, CTF challenges, reverse engineering, dan pertahanan jaringan kampus.", 86),
            Community("3", "AI Society", "Riset terapan deep learning, computer vision, agen cerdas, dan fine-tuning large language models.", 115),
            Community("4", "Mobile Dev Community", "Native Jetpack Compose & KMP.", 95),
            Community("5", "Web Creators", "Pengembangan web modern dengan React, Vue, dan arsitektur micro-frontend.", 120),
            Community("6", "Data Science Club", "Analisis data, visualisasi, dan big data processing menggunakan Python dan R.", 105)
        )
    )
    val communities: StateFlow<List<Community>> = _communities.asStateFlow()

    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    fun toggleCommunityJoin(communityId: String) {
        _communities.update { list ->
            list.map { if (it.id == communityId) it.copy(isJoined = !it.isJoined) else it }
        }
    }

    fun addProject(communityId: String, title: String, description: String, techStacks: List<String>) {
        val newProject = Project(
            id = UUID.randomUUID().toString(),
            communityId = communityId,
            title = title,
            description = description,
            techStacks = techStacks
        )
        _projects.update { it + newProject }
    }

    fun deleteProject(projectId: String) {
        _projects.update { list -> list.filter { it.id != projectId } }
    }

    fun toggleProjectStatus(projectId: String) {
        _projects.update { list ->
            list.map { if (it.id == projectId) it.copy(isCompleted = !it.isCompleted) else it }
        }
    }
}