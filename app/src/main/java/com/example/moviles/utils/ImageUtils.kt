package com.example.moviles.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun bitmapToUri(context: Context, bitmap: Bitmap): Uri {
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, "productImage.jpg")
    var stream: OutputStream? = null
    try {
        stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    } catch (e: Exception) {
        e.printStackTrace()
        return Uri.EMPTY
    } finally {
        stream?.close()
    }
    return  Uri.fromFile(file)
}