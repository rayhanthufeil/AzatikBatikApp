package com.azatik.batikpahlawan.retrofit

import android.graphics.Bitmap
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiEndpoint {
    @Multipart
    @POST("batikMotif")
    suspend fun upload(
        @Part file: MultipartBody.Part
    ): Response<batikResponse>

}