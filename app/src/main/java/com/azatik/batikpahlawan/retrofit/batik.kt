package com.azatik.batikpahlawan.retrofit

import android.graphics.Bitmap
import android.provider.MediaStore
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import java.io.File
data class batik(
    val file:  MultipartBody.Part,
)
