package com.example.lettucechat.feature.chat.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lettucechat.feature.chat.domain.model.Message
import com.example.lettucechat.feature.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    var uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadMessage()
    }

   private fun loadMessage() {
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

    fun onMessageLongClick(message: Message) {
        _uiState.value = _uiState.value.copy(
            selectedMessage = message,
            isSelectionMode = true
        )
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedMessage = null,
            isSelectionMode = false
        )
    }

    fun deleteSelectedMessage() {
        val message = _uiState.value.selectedMessage ?: return
        viewModelScope.launch {
            repository.deleteMessage(message.text)
            clearSelection()
        }
    }

    fun editSelectedMessage(newText: String) {
        val message = _uiState.value.selectedMessage ?: return
        viewModelScope.launch {
            repository.editMessage(message.text, newText)
            clearSelection()
        }
    }
}