package com.example.lettucechat.feature.chat.domain.repository

import com.example.lettucechat.feature.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun observer(): Flow<List<Message>>

    suspend fun sendMessage(text: String)
    suspend fun editMessage(text: String, newText: String)
    suspend fun deleteMessage(text: String, newText: String)
}