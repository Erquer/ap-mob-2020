package com.example.apmob

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Basic database class for the application
 *
 * The only class that should use this is [AppProvider].
 */
private const val TAG = "AppDatabase"
private const val DATABASE_NAME = "ap-mob.db"
private const val DATABASE_VERSION = 3

internal class AppDatabase private constructor(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        Log.d(TAG,"AppDatabase: initialising")
    }

    override fun onCreate(db: SQLiteDatabase) {
        //CREATE TABLE Brainstorm(_id INTEGER PRIMARY KEY NOT NULL, title TEXT NOT NULL, ownerID INTEGER, FOREIGN KEY(ownerID) REFERENCES user(_id));
        //CREATE TABLE user(_id INTEGER PRIMARY KEY NOT NULL, login TEXT NOT NULL, password TEXT NOT NULL);
        //CREATE TABLE IF NOT EXISTS Answers(_id INTEGER PRIMARY KEY NOT NULL, answer TEXT NOT NULL, bsID INTEGER NOT NULL, userID INTEGER,
        // FOREIGN KEY(bsID) REFERANCES Brainstorm(_id), FOREIGN KEY(userID) REFERANCES user(_id);
        Log.d(TAG, "onCreate: starts")
        //create table user if not exists
        val sSQL = """CREATE TABLE IF NOT EXISTS ${User.TABLE_NAME} (${User.Columns.ID} INTEGER PRIMARY KEY NOT NULL, ${User.Columns.LOGIN} TEXT NOT NULL, ${User.Columns.PASSWORD} TEXT NOT NULL)""".replaceIndent(" ")
        Log.d(TAG,"sSQL: ${sSQL}")
        db.execSQL(sSQL)
        //create table Brainstorms if not exists
        val sSql = """CREATE TABLE IF NOT EXISTS ${Brainstorm.TABLE_NAME} (${Brainstorm.Columns.ID} INTEGER PRIMARY KEY NOT NULL, ${Brainstorm.Columns.TITLE} TEXT NOT NULL, ${Brainstorm.Columns.OWNER} INTEGER, FOREIGN KEY(${Brainstorm.Columns.OWNER}) REFERENCES ${User.TABLE_NAME}(${User.Columns.ID}))""".trimMargin()
        Log.d(TAG,"sSql: ${sSql}")
        db.execSQL(sSql)
        //create table answers if not exists
        val aSQL = """CREATE TABLE IF NOT EXISTS ${Answer.TABLE_NAME}(${Answer.Columns.ID} INTEGER PRIMARY KEY NOT NULL, ${Answer.Columns.answer} TEXT NOT NULL,${Answer.Columns.brainstormID} INTEGER NOT NULL,${Answer.Columns.userID} INTEGER, FOREIGN KEY(${Answer.Columns.brainstormID}) REFERENCES ${Brainstorm.TABLE_NAME}(${Brainstorm.Columns.ID}), FOREIGN KEY(${Answer.Columns.userID}) REFERENCES ${User.TABLE_NAME}(${User.Columns.ID}))""".trimMargin()
        Log.d(TAG,"aSQL: ${aSQL}")
        db.execSQL(aSQL)
        Log.d(TAG,"onCraete: ends")


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG,"onUpgrade: starts")
        when(oldVersion){
            1->{
                //upgrade logic from version 1
            }
            else -> throw IllegalStateException("onUpgrade() with unknown newVersion: $newVersion")
        }


        Log.d(TAG,"onUpgrade: ends")
    }
    companion object :SingletonHolder<AppDatabase, Context> (::AppDatabase)
//    companion object{
//        @Volatile
//        private var instance: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this){
//            instance ?: AppDatabase(context).also{ instance = it}
//        }
//    }
}