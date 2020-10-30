package com.example.apmob

import android.content.ContentValues
import kotlin.random.Random

class AnswerGenerator{
    fun generateSingleAnswer(bsID:Long, userID: Long): ContentValues{
        val random = Random(540)
        var randomString = getRandomString(random.nextInt(25))
        return ContentValues().apply {
            put(Answer.Columns.brainstormID, bsID)
            put(Answer.Columns.answer,randomString)
            put(Answer.Columns.userID, userID)
        }
    }
    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun generateAnswers(bsID: Long,count: Int,userID: Long):ArrayList<out ContentValues>{
        val list: ArrayList<ContentValues> = ArrayList()
        if(count > 0) {
            for (i in 0..count) {
                list.add(generateSingleAnswer(bsID, userID))
            }
            return list
        }else{
            return list
        }
    }
}