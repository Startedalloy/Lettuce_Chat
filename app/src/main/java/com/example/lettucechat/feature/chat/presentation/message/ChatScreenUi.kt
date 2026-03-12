package com.example.lettucechat.feature.chat.presentation.message

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lettucechat.Primary
import com.example.lettucechat.Secondary
import com.example.lettucechat.feature.chat.domain.model.Message


@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenUi(chatViewModel: ChatViewModel) {
    val state by chatViewModel.uiState.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (state.isSelectionMode) {
                TopAppBar(
                    title = { Text("Message Options") },
                    navigationIcon = {
                        IconButton(onClick = {
                            chatViewModel.clearSelection()
                            showEditDialog = false
                        }) {

                            Icon(Icons.Default.Close, contentDescription = "Cancel")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { chatViewModel.deleteSelectedMessage() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                )
            }
        },
        containerColor = Primary
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            // ... (Your CircularProgressIndicator and Error Text)

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                TextButton(onClick = { chatViewModel.clearError() }) {
                    Text("Dismiss")
                }
            }


            LazyColumn(Modifier.weight(1f), reverseLayout = true) {
                items(state.message) { message ->
                    MessageBubble(
                        message = message,
                        isSelected = state.selectedMessage == message,
                        onLongClick = { chatViewModel.onMessageLongClick(message) }
                    )
                }
            }

            ChatInput(onSendClick = { chatViewModel.sendMessage(it) })
            Spacer(Modifier.height(16.dp))
        }
    }

    // Basic Edit Dialog
    if (showEditDialog && state.selectedMessage != null) {
        EditMessageDialog(
            initialText = state.selectedMessage?.text ?: "",
            onDismiss = { showEditDialog = false },
            onConfirm = { newText ->
                chatViewModel.editSelectedMessage(newText)
                showEditDialog = false
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(
    message: Message, isSelected: Boolean,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = { /* Handle normal click if needed */ }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Secondary.copy(alpha = 0.7f) else Secondary
        )
    ) {
        Text(
            text = message.text,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun ChatInput(onSendClick: (String) -> Unit) {

    var textState by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = textState,
            onValueChange = { textState = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") }
        )

        Spacer(modifier = Modifier.width(8.dp))


        IconButton(
            onClick = {
                if (textState.isNotBlank()) {
                    onSendClick(textState)
                    textState = ""
                }
            }
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun EditMessageDialog(
    initialText: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialText) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Message") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onConfirm(text)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}