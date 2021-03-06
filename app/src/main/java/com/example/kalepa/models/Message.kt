package com.example.kalepa.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import java.util.*

@SuppressLint("ParcelCreator")
@Parcelize
class Message (
    var nick: String,
    var date: String,
    var body: String

) : Parcelable {
    constructor(): this("","", "")

    public fun fromJSON (jsonObject: JSONObject){
        nick  = jsonObject.get("nick").toString()
        date  = jsonObject.get("date").toString()
        body  = jsonObject.get("body").toString()
    }
}