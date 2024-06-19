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

class SplashViewModel : ViewModel() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    var loggedIn by mutableStateOf(false)
        private set
    var notLoggedIn by mutableStateOf(false)
        private set

    fun checkAuth() {
        if (auth.currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val user = UserRepository.readByEmail(auth.currentUser!!.email!!)
                if (user != null) {
                    Globals.user = user
                    loggedIn = true
                } else {
                    notLoggedIn = true
                }
            }
        } else {
            notLoggedIn = true
        }
    }
}