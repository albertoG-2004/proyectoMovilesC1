package com.example.moviles.ui.Products.ui

import android.content.Context
import android.net.Uri
import android.util.Log
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

class EditSingleProductViewModel(private val apiService: ApiService) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set
    var success by mutableStateOf(false)
        private set
    var product by mutableStateOf<ProductResponse?>(null)
        private set

    fun getProduct(id: Int) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val response = apiService.getProductById(id)
                if (response.isSuccessful) {
                    product = response.body()
                } else {
                    error = "Error al obtener producto: ${response.errorBody()?.string()}"
                    Log.e(
                        "EditSingleProductViewModel",
                        "Error al obtener producto: ${response.errorBody()?.string()}"
                    )

                }
            } catch (e: HttpException) {
                error = "Error al obtener producto, excepcion HTTP: ${e.message()}"
                Log.e(
                    "EditSingleProductViewModel",
                    "Error al obtener producto, excepcion HTTP: ${e.message()}"
                )

            } catch (e: Exception) {
                error = "Error al obtener producto, otra excepcion: ${e.message}"
                Log.e(
                    "EditSingleProductViewModel",
                    "Error al obtener producto, otra excepcion: ${e.message}"
                )

            } finally {
                isLoading = false
            }

        }
    }

    fun updateProduct(
        nombre: String,
        nuevoNombre: String,
        precio: String,
        imageUri: Uri?,
        context: Context
    ) {
        if (nuevoNombre.isBlank() || precio.isBlank()) {
            error = "Por favor, completa todos los campos."
            return
        }

        viewModelScope.launch {
            isLoading = true
            error = null
            success = false
            try {
                val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), nombre)
                val nuevoNombreBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), nuevoNombre)
                val priceBody = RequestBody.create("text/plain".toMediaTypeOrNull(), precio)
                val imagePart = imageUri?.let {
                    uriToMultipart(uri = it, context = context, fieldName = "image")
                }

                // Imprime los valores
                Log.d("EditSingleProductViewModel", "Nombre: ${nombre}")
                Log.d("EditSingleProductViewModel", "Nuevo Nombre: ${nuevoNombre}")
                Log.d("EditSingleProductViewModel", "Precio: ${precio}")
                if (imagePart != null) {
                    Log.d("EditSingleProductViewModel", "Imagen file name: ${imagePart.body.toString()}")

                } else {
                    Log.d("EditSingleProductViewModel", "Imagen: null")
                }

                val response = apiService.updateProduct(nameBody, nuevoNombreBody, priceBody, imagePart)

                if (response.isSuccessful) {
                    success = true;
                    getProduct(product?.id!!)
                } else {
                    error = "Error al actualizar el producto: ${response.errorBody()?.string()}"
                    Log.e(
                        "EditSingleProductViewModel",
                        "Error al actualizar el producto: ${response.errorBody()?.string()}"
                    )

                }
            } catch (e: HttpException) {
                error = "Error al actualizar producto, excepcion HTTP: ${e.message()}"
                Log.e(
                    "EditSingleProductViewModel",
                    "Error al actualizar producto, excepcion HTTP: ${e.message()}"
                )

            } catch (e: Exception) {
                error = "Error al actualizar producto, otra excepcion: ${e.message}"
                Log.e(
                    "EditSingleProductViewModel",
                    "Error al actualizar producto, otra excepcion: ${e.message}"
                )

            } finally {
                isLoading = false
            }
        }

    }
}