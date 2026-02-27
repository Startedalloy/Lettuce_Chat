package com.example.lettucechat.feature.chat.presentation.message

import com.example.lettucechat.feature.chat.domain.model.Message

data class ChatUiState(
    var message: List<Message> = emptyList(),

    var isLoading : Boolean = false,

    var errorMessage: String? = null
)