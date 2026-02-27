package com.example.lettucechat.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lettucechat.navigation.Routes
import com.example.lettucechat.viewModel.AuthState
import com.example.lettucechat.viewModel.AuthViewModel

@Composable
fun HomeScreen(authViewModel: AuthViewModel, navController: NavController) {

    val authState = authViewModel.authState.observeAsState(AuthState.Loading)
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {

            is AuthState.UnAuthenticated -> navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.HOME) { inclusive = true }
                launchSingleTop = true
            }

            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home Screen", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                authViewModel.signOut()
            }
        ) {
            Text("Sign Out")
        }
    }

}