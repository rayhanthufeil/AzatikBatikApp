package com.azatik.batikpahlawan.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azatik.batikpahlawan.retrofit.ApiService
import com.azatik.batikpahlawan.retrofit.MotifBatik
import com.azatik.batikpahlawan.retrofit.batikResponse
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ViewModelBatik : ViewModel() {
    val listUser = MutableLiveData<List<MotifBatik>>()
    //data= "batikResponse"
    val isDone=MutableLiveData<Boolean>()
    init {
        isDone.value=false
    }

    suspend fun upload(file: MultipartBody.Part):batikResponse? {
        val res=ApiService.apiInstance.upload(file).body()
        return res
    }


  fun getResult(): LiveData<List<MotifBatik>> {
    return listUser
  }

}