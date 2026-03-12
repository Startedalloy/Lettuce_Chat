package com.example.lettucechat.feature.chat.domain.repository

import com.example.lettucechat.feature.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun observer(): Flow<List<Message>>

    suspend fun sendMessage(text: String)

    fun getCurrentUserId(): String?
    suspend fun editMessage(messageId: String, newText: String)
    suspend fun deleteMessage(messageId: String)
}