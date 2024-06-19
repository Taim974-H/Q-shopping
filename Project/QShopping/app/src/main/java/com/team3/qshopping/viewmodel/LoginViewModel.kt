package com.team3.qshopping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.team3.qshopping.Globals
import com.team3.qshopping.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val auth by lazy { FirebaseAuth.getInstance() }

    var uiState by mutableStateOf(LoginUiState())
        private set
    var loggedIn by mutableStateOf(false)
        private set

    fun onEmailChange(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password)
    }

    fun clearMessage() {
        uiState = uiState.copy(message = null)
    }

    fun signIn() {
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(message = "All fields must be filled")
            return
        }

        auth
            .signInWithEmailAndPassword(uiState.email, uiState.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setUser()
                } else {
                    uiState = uiState.copy(message = task.exception?.message)
                }
            }
    }

    private fun setUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = UserRepository.readByEmail(auth.currentUser!!.email!!)
            if (user != null) {
                Globals.user = user
                loggedIn = true
            } else {
                uiState = uiState.copy(message = "An error occurred")
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val message: String? = null,
)
