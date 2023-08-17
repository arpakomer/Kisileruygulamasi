package com.arpak.kisileruygulamasi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
    With Volley Library  to use
data class Kisiler(var kisi_id: Int, var kisi_ad: String, var kisi_tel: String ): Serializable
* */

data class Kisiler(
    @SerializedName("kisi_id")
    @Expose
    var kisi_id: Int,
    @SerializedName("kisi_ad")
    @Expose
    var kisi_ad: String,
    @SerializedName("kisi_tel")
    @Expose
    var kisi_tel: String
) : Serializable

data class KisilerCevap(
    @SerializedName("kisiler")
    @Expose
    var kisiler: List<Kisiler>,
    @SerializedName("success")
    @Expose
    var success: Int
)

data class CRUDCevap(
    @SerializedName("success")
    @Expose
    var success: Int,
    @SerializedName("message")
    @Expose
    var message: String
)