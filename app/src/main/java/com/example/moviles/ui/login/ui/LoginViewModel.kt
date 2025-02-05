package com.example.moviles.ui.login.ui

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviles.apiService.RetroClient
import com.example.moviles.ui.login.ui.models.LoginReq
import com.example.moviles.ui.login.ui.models.LoginRes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var loginEnabled by mutableStateOf(false)
        private set

    private val _navigateToHome = MutableSharedFlow<Boolean>()
    val navigateToHome: SharedFlow<Boolean> = _navigateToHome.asSharedFlow()

    var errorEmail by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var loginError by mutableStateOf<String?>(null)
        private set

    fun onEmailChanged(newEmail: String) {
        email = newEmail
        validateEmail()
        validateLogin()

    }


    fun onPasswordChanged(newPassword: String) {
        password = newPassword
        validateLogin()
    }

    private fun validateEmail() {
        errorEmail = if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Email no válido"
        } else {
            null
        }
    }

    private fun validateLogin() {
        loginEnabled = email.isNotBlank() && password.isNotBlank() && errorEmail == null
    }

    fun onLoginButtonClicked() {
        if (!loginEnabled) {
            return
        }

        isLoading = true
        loginError = null

        val credentials = LoginReq(email, password)
        val call = RetroClient.instance.login(credentials)

        call.enqueue(object : retrofit2.Callback<LoginRes> {
            override fun onResponse(call: Call<LoginRes>, response: Response<LoginRes>) {
                isLoading = false
                if (response.isSuccessful) {
                    val body = response.body()
                    println(body)
                    if (body?.message == "Login exitoso") {
                        viewModelScope.launch{
                            _navigateToHome.emit(true)
                        }
                        println("Login con exito")
                    } else {
                        loginError = "Inicio de sesión fallido"
                        println("Error, el body llegó vacío")
                    }
                } else {
                    loginError = "Error del servidor: ${response.errorBody()?.string()}"
                    println("Error del servidor: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<LoginRes>, t: Throwable) {
                isLoading = false
                loginError = "Error en la conexión: ${t.message}"
                println("Error en la conexión: ${t.message}")
            }
        })
    }
}