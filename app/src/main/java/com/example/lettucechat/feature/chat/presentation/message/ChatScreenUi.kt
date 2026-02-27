package com.example.lettucechat.feature.chat.presentation.message

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lettucechat.feature.chat.domain.model.Message


@Composable
fun ChatScreenUi(chatViewModel: ChatViewModel) {

    Column(Modifier.fillMaxSize()) {
        val state by chatViewModel.uiState.collectAsState()

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        LazyColumn(Modifier.weight(1f), reverseLayout = true) {
            items(state.message) { message ->
                MessageBubble(message)
            }
        }

    }
    ChatInput(
        onSendClick = { text ->
            chatViewModel.sendMessage(text)
        }
    )

}

@Composable
fun MessageBubble(message: Message) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(8.dp)
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
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}