package com.example.komang_week6_vp.model

data class Community(
    val id: String,
    val name: String,
    val description: String,
    val memberCount: Int,
    val isJoined: Boolean = false
)

data class Project(
    val id: String,
    val communityId: String,
    val title: String,
    val description: String,
    val techStacks: List<String>,
    val isCompleted: Boolean = false
)