package com.team3.qshopping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.team3.qshopping.Globals
import com.team3.qshopping.data.local.models.User
import com.team3.qshopping.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val auth by lazy { FirebaseAuth.getInstance() }

    var uiState by mutableStateOf(RegisterUiState())
        private set
    var registered by mutableStateOf(false)
        private set

    fun onFullNameChange(fullName: String) {
        uiState = uiState.copy(fullName = fullName)
    }

    fun onUsernameChange(username: String) {
        uiState = uiState.copy(username = username)
    }

    fun onEmailChange(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password)
    }

    fun clearMessage() {
        uiState = uiState.copy(message = null)
    }

    fun signUp() {
        if (uiState.fullName.isBlank() || uiState.username.isBlank()
            || uiState.email.isBlank() || uiState.password.isBlank()
        ) {
            uiState = uiState.copy(message = "All fields must be filled")
            return
        }

        auth
            .createUserWithEmailAndPassword(uiState.email, uiState.password)
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
            UserRepository.create(newUser())
            Globals.user = UserRepository.readByEmail(uiState.email)
            registered = true
        }
    }

    private fun newUser(): User {
        return User(
            fullName = uiState.fullName,
            userName = uiState.username,
            email = uiState.email,
            password = uiState.password,
            isAdmin = false
        )
    }
}

data class RegisterUiState(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val message: String? = null,
)
