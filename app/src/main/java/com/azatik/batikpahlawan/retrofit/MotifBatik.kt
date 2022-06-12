package com.azatik.batikpahlawan.retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MotifBatik (
 @field:SerializedName("Motif Batik")
 val Motif_Batik : String,
 @field:SerializedName("persentase")
 val persentase :  String,

):Parcelable