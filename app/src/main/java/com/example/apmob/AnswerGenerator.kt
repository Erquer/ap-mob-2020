package com.example.apmob

import kotlin.random.Random

class AnswerGenerator {
    fun generateSingleAnswer(bsID:Long, userID: Long): AnswerClass{
        val random = Random(540)
        var randomString = getRandomString(random.nextInt(25))
        return AnswerClass(bsID,randomString,userID)
    }
    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun generateAnswers(bsID: Long,count: Int,userID: Long):ArrayList<AnswerClass>{
        val list: ArrayList<AnswerClass> = ArrayList()
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