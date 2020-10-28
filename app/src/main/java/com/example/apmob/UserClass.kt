package com.example.apmob

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@SuppressLint("ParcelCreator")
class UserClass(val login: String, val password: String): Parcelable{
    var id: Long = 0L

}