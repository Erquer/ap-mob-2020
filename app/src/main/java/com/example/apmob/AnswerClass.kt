package com.example.apmob

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@SuppressLint("ParcelCreator")
class AnswerClass(val brainstormID:Long, val answer: String, val userID: Long): Parcelable {
    var id: Long = 0L
}