package com.example.apmob

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns


object Brainstorm{
    internal const val TABLE_NAME = "Brainstorms"

    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI,TABLE_NAME)

    const val  CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"
    const val  CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.${TABLE_NAME}"
    //Brainstorm objects
    object Columns{
        const val ID = BaseColumns._ID
        const val TITLE = "Title"
        const val OWNER = "OwnerID"
    }
    fun getId(uri: Uri):Long{
        return ContentUris.parseId(uri)
    }
    fun buildUriFromId(id:Long):Uri{
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }
}
