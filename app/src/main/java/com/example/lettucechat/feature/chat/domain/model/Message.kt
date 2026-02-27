package com.example.lettucechat.feature.chat.domain.model

data class Message(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: Long
)