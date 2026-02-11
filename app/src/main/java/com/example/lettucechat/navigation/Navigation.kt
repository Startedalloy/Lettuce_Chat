package com.example.lettucechat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lettucechat.screens.HomeScreen
import com.example.lettucechat.screens.LoginScreen
import com.example.lettucechat.screens.SignupScreen
import com.example.lettucechat.viewModel.ChatViewModel

@Composable
fun Navigation(chatViewModel: ChatViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Routes.SIGNUP, builder = {
        composable(Routes.LOGIN) {
            LoginScreen(chatViewModel,navController)
        }
        composable(Routes.SIGNUP) {
            SignupScreen(chatViewModel,navController)
        }
        composable(Routes.HOME) {
            HomeScreen(chatViewModel,navController)
        }
    })


}