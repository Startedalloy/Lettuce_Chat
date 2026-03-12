package com.example.lettucechat.feature.chat.presentation.message

import com.example.lettucechat.feature.chat.domain.model.Message

data class ChatUiState(
    val message: List<Message> = emptyList(),

    val isLoading: Boolean = false,

    val errorMessage: String? = null,

    val selectedMessage: Message? = null,

    val isSelectionMode: Boolean = false,

    val currentUserId: String? = null
)