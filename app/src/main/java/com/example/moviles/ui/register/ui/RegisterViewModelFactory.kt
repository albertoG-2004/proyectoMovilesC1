package com.example.moviles.ui.register.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviles.ui.register.data.RegisterModel

class RegisterViewModelFactory(private val registerModel: RegisterModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(registerModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}