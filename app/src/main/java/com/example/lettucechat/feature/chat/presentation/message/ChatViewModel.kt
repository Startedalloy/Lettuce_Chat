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
        _uiState.value = _uiState.value.copy(currentUserId = repository.getCurrentUserId())
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
        val currentUserId = _uiState.value.currentUserId
        if (currentUserId == null || message.senderId != currentUserId) {
            _uiState.value =
                _uiState.value.copy(errorMessage = "You can edit/delete only your own messages")
            return
        }
        _uiState.value = _uiState.value.copy(
            selectedMessage = message,
            isSelectionMode = true,
            errorMessage = null
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedMessage = null,
            isSelectionMode = false
        )
    }

    fun deleteSelectedMessage() {
        val message = _uiState.value.selectedMessage ?: return

        val currentUserId = _uiState.value.currentUserId
        if (currentUserId == null || message.senderId != currentUserId) {
            _uiState.value = _uiState.value.copy(errorMessage = "You can delete only your own messages")
            return
        }
        viewModelScope.launch {
            try {
                repository.deleteMessage(message.id)
                clearSelection()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Failed to delete: ${e.message}")
            }
        }
    }


    fun editSelectedMessage(newText: String) {
        val message = _uiState.value.selectedMessage ?: return

        val currentUserId = _uiState.value.currentUserId
        if (currentUserId == null || message.senderId != currentUserId) {
            _uiState.value = _uiState.value.copy(errorMessage = "You can edit only your own messages")
            return
        }

        viewModelScope.launch {
            try {
                repository.editMessage(message.id, newText)
                clearSelection()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Failed to edit: ${e.message}")
            }
        }
    }
}