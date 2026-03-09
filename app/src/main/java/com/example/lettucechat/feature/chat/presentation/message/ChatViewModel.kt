package com.example.lettucechat.feature.chat.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lettucechat.feature.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    var uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        LoadMessage()
    }

   private fun LoadMessage() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.observer().collect { message ->
                _uiState.value = _uiState.value.copy(message = message, isLoading = false)
            }
        }

    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            try {
                repository.sendMessage(text)
            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(errorMessage = "Failed to send: ${e.message}")
            }
        }
    }

}