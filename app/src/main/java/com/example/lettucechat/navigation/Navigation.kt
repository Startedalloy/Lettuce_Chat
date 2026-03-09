package com.example.lettucechat.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text("Checking session...")
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