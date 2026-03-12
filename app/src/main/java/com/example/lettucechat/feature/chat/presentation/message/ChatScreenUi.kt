package com.example.lettucechat.feature.chat.presentation.message

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lettucechat.DarkBg
import com.example.lettucechat.LettuceGreen
import com.example.lettucechat.SurfaceVariant
import com.example.lettucechat.feature.chat.domain.model.Message


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreenUi(chatViewModel: ChatViewModel) {
    val state by chatViewModel.uiState.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // Keep the TopBar persistent or only show during selection
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (state.isSelectionMode) "Selected" else "LettuceChat",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBg),
                navigationIcon = {
                    if (state.isSelectionMode) {
                        IconButton(onClick = { chatViewModel.clearSelection() }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Cancel",
                                tint = Color.White
                            )
                        }
                    }
                },
                actions = {
                    if (state.isSelectionMode) {
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = LettuceGreen
                            )
                        }
                        IconButton(onClick = { chatViewModel.deleteSelectedMessage() }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            )
        },
        containerColor = DarkBg
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                reverseLayout = true
            ) {
                items(state.message) { message ->
                    MessageBubble(
                        message = message,
                        isMine = true, // Toggle this based on your logic
                        isSelected = state.selectedMessage == message,
                        onLongClick = { chatViewModel.onMessageLongClick(message) }
                    )
                }
            }

            ChatInput(onSendClick = { chatViewModel.sendMessage(it) })
        }
    }

    if (showEditDialog && state.selectedMessage != null) {
        EditMessageDialog(
            initialText = state.selectedMessage?.text ?: "",
            onDismiss = { showEditDialog = false },
            onConfirm = { newText: String ->
                chatViewModel.editSelectedMessage(newText)
                showEditDialog = false
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(
    message: Message,
    isMine: Boolean,
    isSelected: Boolean,
    onLongClick: () -> Unit
) {
    val alignment = if (isMine) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (isMine) LettuceGreen else SurfaceVariant
    val textColor = if (isMine) Color.Black else Color.White

    // Different corner radii for sent vs received
    val shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isMine) 16.dp else 0.dp,
        bottomEnd = if (isMine) 0.dp else 16.dp
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .combinedClickable(
                    onLongClick = onLongClick,
                    onClick = { /* Handle click */ }
                ),
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) bubbleColor.copy(alpha = 0.5f) else bubbleColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ChatInput(onSendClick: (String) -> Unit) {
    var textState by remember { mutableStateOf("") }

    Surface(
        color = DarkBg,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding(), // Ensures it stays above nav bar
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier
                    .weight(1f),
                placeholder = { Text("Type a message...", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceVariant,
                    unfocusedContainerColor = SurfaceVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            FloatingActionButton(
                onClick = {
                    if (textState.isNotBlank()) {
                        onSendClick(textState)
                        textState = ""
                    }
                },
                containerColor = LettuceGreen,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
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