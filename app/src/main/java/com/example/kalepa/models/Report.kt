package com.example.kalepa.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import java.util.*

@SuppressLint("ParcelCreator")
@Parcelize
class Report (
    var id: Int,
    var user_id: Int,
    var user_nick: String,
    var product_id: Int,
    var reason: String,
    var date: String

) : Parcelable {
    constructor(): this(0,0,"",0,"","")

    public fun fromJSON (jsonObject: JSONObject){
        id = jsonObject.get("id").toString().toInt()
        user_id = jsonObject.get("user_id").toString().toInt()
        user_nick = jsonObject.get("user_nick").toString()
        product_id = jsonObject.get("product_id").toString().toInt()
        reason = jsonObject.get("reason").toString()
        date = jsonObject.get("date").toString()
    }
}