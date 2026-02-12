package com.example.lettucechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lettucechat.navigation.Navigation
import com.example.lettucechat.ui.theme.LettuceChatTheme
import com.example.lettucechat.viewModel.ChatViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ChatViewModel = viewModel()
            LettuceChatTheme {
                Navigation(viewModel)
            }
        }
    }
}

