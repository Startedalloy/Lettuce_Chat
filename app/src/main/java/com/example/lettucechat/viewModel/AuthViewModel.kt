package com.example.lettucechat.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>(AuthState.Loading)
    val authState = _authState

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.UnAuthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please Enter Email and Password")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value =
                    AuthState.Error(task.exception?.message ?: "Something went Wrong")
            }
        }
    }

    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please Enter Email and Password")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value =
                    AuthState.Error(task.exception?.message ?: "Something went Wrong")
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }
}


sealed class AuthState {
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}