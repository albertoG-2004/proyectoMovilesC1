package com.example.moviles.apiService

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


fun uriToMultipart(uri: Uri, context: Context, fieldName: String): MultipartBody.Part {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    if(inputStream == null){
        throw Exception("Error al obtener el InputStream")
    }
    val tempFile = File(context.cacheDir, "tempFile${System.currentTimeMillis()}.jpg")
    FileOutputStream(tempFile).use {
        inputStream.copyTo(it)
    }
    val requestFile = RequestBody.create(
        context.contentResolver.getType(uri)?.toMediaTypeOrNull(),
        tempFile
    )
    return MultipartBody.Part.createFormData(fieldName, tempFile.name, requestFile)
}