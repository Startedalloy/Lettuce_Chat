package com.example.lettucechat.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lettucechat.R
import com.example.lettucechat.feature.chat.data.repository.ChatRepositoryImpl
import com.example.lettucechat.feature.chat.presentation.message.ChatScreenUi
import com.example.lettucechat.feature.chat.presentation.message.ChatViewModel
import com.example.lettucechat.feature.chat.presentation.message.ChatViewModelFactory
import com.example.lettucechat.screens.HomeScreen
import com.example.lettucechat.screens.LoginScreen
import com.example.lettucechat.screens.SignupScreen
import com.example.lettucechat.viewModel.AuthState
import com.example.lettucechat.viewModel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Navigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.observeAsState(AuthState.Loading)

    if (authState is AuthState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF23232C)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bglogo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 32.dp),
                    contentScale = ContentScale.Crop
                )


                CircularProgressIndicator(
                    color = Color(0xFF8FC385),
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = "LettuceChat",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Checking session...",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        return
    }

    val startDestination = if (authState is AuthState.Authenticated) {
        Routes.HOME
    } else {
        Routes.LOGIN
    }


    NavHost(navController, startDestination = startDestination, builder = {
        composable(Routes.LOGIN) {
            LoginScreen(authViewModel, navController)
        }
        composable(Routes.SIGNUP) {
            SignupScreen(authViewModel, navController)
        }
        composable(Routes.HOME) {
            HomeScreen(authViewModel, navController)
        }
        composable(Routes.CHAT) {
            val chatViewModel: ChatViewModel = viewModel(
                factory = ChatViewModelFactory(
                    repository = ChatRepositoryImpl(
                        firestore = FirebaseFirestore.getInstance(),
                        auth = FirebaseAuth.getInstance()
                    )
                )
            )
            ChatScreenUi(chatViewModel)
        }

    })


}