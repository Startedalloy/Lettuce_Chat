package com.example.lettucechat.feature.chat.data.model

data class MessageDto(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)
