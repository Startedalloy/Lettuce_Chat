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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lettucechat.screens.HomeScreen
import com.example.lettucechat.screens.LoginScreen
import com.example.lettucechat.screens.SignupScreen
import com.example.lettucechat.viewModel.AuthState
import com.example.lettucechat.viewModel.ChatViewModel

@Composable
fun Navigation(chatViewModel: ChatViewModel) {
    val navController = rememberNavController()
    val authState by chatViewModel.authState.observeAsState(AuthState.Loading)

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
            LoginScreen(chatViewModel, navController)
        }
        composable(Routes.SIGNUP) {
            SignupScreen(chatViewModel, navController)
        }
        composable(Routes.HOME) {
            HomeScreen(chatViewModel, navController)
        }
    })


}