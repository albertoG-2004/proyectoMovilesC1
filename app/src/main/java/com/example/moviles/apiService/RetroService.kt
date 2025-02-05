package com.example.moviles.apiService

import com.example.moviles.ui.Products.ui.ProductResponse
import com.example.moviles.ui.login.ui.models.LoginReq
import com.example.moviles.ui.login.ui.models.LoginRes
import com.example.moviles.ui.register.ui.models.RegisterReq
import com.example.moviles.ui.register.ui.models.RegisterRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("users/login")
    fun login(@Body req: LoginReq): retrofit2.Call<LoginRes>

    @Multipart
    @POST("products")
    suspend fun addProduct(
        @Part("nombre") name: RequestBody,
        @Part("precio") price: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @GET("products")
    suspend fun getProducts(): Response<List<ProductResponse>>

    @DELETE("products/{nombre}")
    suspend fun deleteProduct(@Path("nombre") nombre:String): Response<Unit>

    @Multipart
    @PUT("products")
    suspend fun updateProduct(
        @Part("nombre") nombre: RequestBody,
        @Part("nuevoNombre") nuevoNombre: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Unit>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductResponse>

    // Registro de usuario
    @POST("users/add")
    suspend fun register(@Body req: RegisterReq): Response<RegisterRes>
}