package com.example.moviles.ui.Products.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviles.apiService.ApiService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.math.log


class ListProductsViewModel(private val apiService: ApiService) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var products by mutableStateOf<List<ProductResponse>>(emptyList())
        private set

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val response = apiService.getProducts()
                if (response.isSuccessful) {
                    products = response.body() ?: emptyList()
                    println("Lista de productos"+ products)
                } else {
                    error = "Error al obtener productos: ${response.errorBody()?.string()}"
                }

            } catch (e: HttpException) {
                error = "Error al obtener productos, excepcion HTTP: ${e.message()}"
            } catch (e: Exception) {
                error = "Error al obtener productos, otra excepcion: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

}