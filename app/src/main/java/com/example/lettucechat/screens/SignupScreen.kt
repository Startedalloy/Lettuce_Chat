package com.example.lettucechat.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lettucechat.Primary
import com.example.lettucechat.R
import com.example.lettucechat.Secondary
import com.example.lettucechat.navigation.Routes
import com.example.lettucechat.viewModel.AuthState
import com.example.lettucechat.viewModel.AuthViewModel


@Composable
fun SignupScreen(authViewModel: AuthViewModel, navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState(AuthState.Loading)
    val context = LocalContext.current
    val isLoading = authState.value is AuthState.Loading


    Surface(Modifier.fillMaxSize(), color = Primary) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bglogo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 4.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                "Create Account",
                style = MaterialTheme.typography.headlineMedium,
                color = Secondary,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter Your Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(32.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Secondary,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Secondary,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter Your Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(32.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Secondary,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Secondary,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { authViewModel.signUp(email, password) },
                Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Secondary,
                    contentColor = Primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Primary, strokeWidth = 2.dp)
                } else {
                    Text("SIGNUP", fontWeight = FontWeight.ExtraBold)
                }
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = { navController.navigate(Routes.LOGIN) }) {
                Text(
                    "Already have an account? Login",
                    fontWeight = FontWeight.Medium,
                    color = Secondary
                )
            }
        }
    }
}