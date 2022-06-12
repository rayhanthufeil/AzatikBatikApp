package com.azatik.batikpahlawan.retrofit
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class batikResponse(
    @field:SerializedName("motifBatik")
    val items: ArrayList<MotifBatik>


):Parcelable
