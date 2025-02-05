package com.example.moviles.ui.Products.ui

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviles.apiService.ApiService
import com.example.moviles.apiService.uriToMultipart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.HttpException

class AddProductViewModel(private val apiService: ApiService) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var success by mutableStateOf(false)
        private set

    fun addProduct(
        name: String,
        price: String,
        imageUri: Uri?,
        context: Context
    ) {
        if (name.isBlank() || price.isBlank() || imageUri == null) {
            error = "Por favor, completa todos los campos."
            return
        }

        viewModelScope.launch {
            isLoading = true
            error = null
            success = false
            try {

                val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(),name)
                val priceBody = RequestBody.create("text/plain".toMediaTypeOrNull(),price)
                val imagePart =  uriToMultipart(uri = imageUri, context = context,fieldName = "image")


                val response = apiService.addProduct(nameBody,priceBody,imagePart)

                if (response.isSuccessful) {
                    success = true;
                } else {
                    error = "Error al agregar producto: ${response.errorBody()?.string()}"
                }
            }catch (e:HttpException){
                error = "Error al agregar producto, excepcion HTTP: ${e.message()}"
            } catch (e: Exception) {
                error = "Error al agregar producto, otra excepcion: ${e.message}"
            }finally {
                isLoading = false
            }
        }

    }
}