package com.example.apmob

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

/**
 * Provider for the ApMob app. This the only class that knows about [AppDatabase]
 */
private const val TAG = "AppProvider"
const val CONTENT_AUTHORITY = "com.example.apmob.provider"

private const val USERS = 100
private const val USERS_ID = 101

private const val BRAINSTORMS = 200
private const val BRAINSTORMS_ID = 201

private const val ANSWERS = 300
private const val ANSWERS_ID = 301

private const val PARTICIPATIONS = 400
private const val PARTICIPATIONS_ID = 401

val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AppProvider : ContentProvider() {

    private val uriMatcher by lazy { buildUriMatcher() }

    private fun buildUriMatcher(): UriMatcher {
        Log.d(TAG, "buildUriMatcher: starts")
        val matcher = UriMatcher(UriMatcher.NO_MATCH)

        //1st: content://com.example.amob.provider/user
        //2nd: content://com.example.amob.provider/user/8
        matcher.addURI(CONTENT_AUTHORITY, User.TABLE_NAME, USERS)
        matcher.addURI(CONTENT_AUTHORITY, "${User.TABLE_NAME}/#", USERS_ID)

//        matcher.addURI(CONTENT_AUTHORITY, Brainstorm.TABLE_NAME, BRAINSTORMS)
//        matcher.addURI(CONTENT_AUTHORITY, "${Brainstorm.TABLE_NAME}/#", BRAINSTORMS_ID)
//
//        matcher.addURI(CONTENT_AUTHORITY, Answer.TABLE_NAME, ANSWERS)
//        matcher.addURI(CONTENT_AUTHORITY, "${Answer.TABLE_NAME}/#", ANSWERS_ID)
        return matcher
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate: starts")
        val appDatabase = AppDatabase.getInstance(context!!)
        return true
    }

    override fun getType(uri: Uri): String? {
        val match = uriMatcher.match(uri)
        return when (match) {
            USERS -> User.CONTENT_TYPE
            USERS_ID -> User.CONTENT_ITEM_TYPE

            BRAINSTORMS -> Brainstorm.CONTENT_TYPE
            BRAINSTORMS_ID -> Brainstorm.CONTENT_ITEM_TYPE

            ANSWERS -> Answer.CONTENT_TYPE
            ANSWERS_ID -> Answer.CONTENT_ITEM_TYPE

            PARTICIPATIONS -> Participation.CONTENT_TYPE
            PARTICIPATIONS_ID -> Participation.CONTENT_ITEM_TYPE

            else -> throw IllegalArgumentException("unknown URI: $uri")
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "query: called with uri: $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "query: match is $match")
        val queryBuilder: SQLiteQueryBuilder = SQLiteQueryBuilder()

        when (match) {
            USERS -> queryBuilder.tables = User.TABLE_NAME

            USERS_ID -> {
                queryBuilder.tables = User.TABLE_NAME
                val userID = User.getId(uri)
                queryBuilder.appendWhere("${User.Columns.ID} = ")
                queryBuilder.appendWhereEscapeString("$userID")
            }

            BRAINSTORMS -> queryBuilder.tables = Brainstorm.TABLE_NAME

            BRAINSTORMS_ID -> {
                queryBuilder.tables = Brainstorm.TABLE_NAME
                val brainstormID = Brainstorm.getId(uri)
                queryBuilder.appendWhere("${Brainstorm.Columns.ID} =")
                queryBuilder.appendWhereEscapeString("$brainstormID")
            }

            ANSWERS -> queryBuilder.tables = Answer.TABLE_NAME

            ANSWERS_ID -> {
                queryBuilder.tables = Answer.TABLE_NAME
                val answerID = Answer.getId(uri)
                queryBuilder.appendWhere("${Answer.Columns.ID} = ")
                queryBuilder.appendWhereEscapeString("$answerID")

            }

            PARTICIPATIONS -> queryBuilder.tables = Participation.TABLE_NAME

            PARTICIPATIONS_ID -> {
                queryBuilder.tables = Participation.TABLE_NAME
                val participationID = Participation.getId(uri)
                queryBuilder.appendWhere("${Participation.Columns.userID} = ")
                queryBuilder.appendWhereEscapeString("$participationID")
            }

            else -> throw IllegalStateException("Unknown URI: $uri")

        }
        val db = AppDatabase.getInstance(context!!).readableDatabase
        val cursor =
            queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        Log.d(TAG, "query: rows in returned cursor = ${cursor.count}") //TODO remove this line
        return cursor
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri {
        Log.d(TAG, "insert: called with uri: $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "insert: match is $match")

        val recordID: Long
        val returnURI: Uri
        when (match) {
            USERS -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                if (db != null) {
                    recordID = db.insert(User.TABLE_NAME, null, values)
                } else recordID = -1
                if (recordID != -1L) {
                    returnURI = User.buildUriFromId(recordID)
                } else {
                    throw SQLException("Failed to inserd, Uri was: $uri")
                }
            }

            BRAINSTORMS -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                if (db != null) {
                    recordID = db.insert(Brainstorm.TABLE_NAME, null, values)
                } else recordID = -1
                if (recordID != -1L) {
                    returnURI = Brainstorm.buildUriFromId(recordID)
                } else {
                    throw SQLException("Failed to inserd, Uri was: $uri")
                }
            }

            ANSWERS -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                if (db != null) {
                    recordID = db.insert(Answer.TABLE_NAME, null, values)
                } else recordID = -1
                if (recordID != -1L) {
                    returnURI = Answer.buildUriFromId(recordID)
                } else {
                    throw SQLException("Failed to inserd, Uri was: $uri")
                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        Log.d(TAG, "Exiting insert, returning $returnURI")
        return returnURI
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.d(TAG, "update: called with uri: $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "update: match is $match")

        val count: Int
        var selectionCriteria: String
        when (match) {

            USERS -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                if (db != null) {
                    count = db.update(User.TABLE_NAME, values, selection, selectionArgs)
                } else count = 0
            }

            USERS_ID -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                val id = User.getId(uri)
                selectionCriteria = "${User.Columns.ID} = $id"
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }
                if (db != null) {
                    count = db.update(User.TABLE_NAME, values, selectionCriteria, selectionArgs)
                } else count = 0
            }

            BRAINSTORMS -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                if (db != null) {
                    count = db.update(Brainstorm.TABLE_NAME, values, selection, selectionArgs)
                } else count = 0
            }

            BRAINSTORMS_ID -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                val id = Brainstorm.getId(uri)
                selectionCriteria = "${Brainstorm.Columns.ID} = $id"
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }
                if (db != null) {
                    count =
                        db.update(Brainstorm.TABLE_NAME, values, selectionCriteria, selectionArgs)
                } else count = 0
            }

            ANSWERS -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                if (db != null) {
                    count = db.update(Answer.TABLE_NAME, values, selection, selectionArgs)
                } else count = 0
            }

            ANSWERS_ID -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                val id = Answer.getId(uri)
                selectionCriteria = "${Answer.Columns.ID} = $id"
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }
                if (db != null) {
                    count = db.update(Answer.TABLE_NAME, values, selectionCriteria, selectionArgs)
                } else count = 0
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        Log.d(TAG, "Exiting update, returning $count")
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "delete: called with uri: $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "delete: match is $match")

        val count: Int
        var selectionCriteria: String
        when (match) {

            USERS -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                if (db != null) {
                    count = db.delete(User.TABLE_NAME, selection, selectionArgs)
                } else count = 0
            }

            USERS_ID -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                val id = User.getId(uri)
                selectionCriteria = "${User.Columns.ID} = $id"
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }
                if (db != null) {
                    count = db.delete(User.TABLE_NAME, selectionCriteria, selectionArgs)
                } else count = 0
            }

            BRAINSTORMS -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                if (db != null) {
                    count = db.delete(Brainstorm.TABLE_NAME, selection, selectionArgs)
                } else count = 0
            }

            BRAINSTORMS_ID -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                val id = Brainstorm.getId(uri)
                selectionCriteria = "${Brainstorm.Columns.ID} = $id"
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }
                if (db != null) {
                    count = db.delete(Brainstorm.TABLE_NAME, selectionCriteria, selectionArgs)
                } else count = 0
            }

            ANSWERS -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                if (db != null) {
                    count = db.delete(Answer.TABLE_NAME, selection, selectionArgs)
                } else count = 0
            }

            ANSWERS_ID -> {
                val db = context?.let { AppDatabase.getInstance(it).writableDatabase }
                val id = Answer.getId(uri)
                selectionCriteria = "${Answer.Columns.ID} = $id"
                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }
                if (db != null) {
                    count = db.delete(Answer.TABLE_NAME, selectionCriteria, selectionArgs)
                } else count = 0
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        Log.d(TAG, "Exiting delete, returning $count")
        return count
    }


}
