package com.example.apmob

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@SuppressLint("ParcelCreator")
class BrainstormClass(val title:String, val ownerID: Int) :Parcelable {
var id = 0L
}