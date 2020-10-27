package com.example.apmob

import android.content.ContentUris
import android.net.Uri

object Participation{
    internal const val TABLE_NAME = "Participation"

    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    const val  CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"
    const val  CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"
    object Columns{
        const val userID = "_idUser"
        const val brainstormID = "_idBS"
    }
    fun getId(uri: Uri):Long{
        return ContentUris.parseId(uri)
    }
    fun buildUriFromId(id:Long):Uri{
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }
}