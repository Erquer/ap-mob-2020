package com.example.apmob

import android.app.Application
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private const val TAG="BRAINSTORM_VIEW_MODEL"
class BrainstormViewModel(application: Application):AndroidViewModel(application) {
    private val contentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            Log.d(TAG,"contentObserver.onChange:called. uri is $uri")
            loadBrainstorms()
        }
    }
    private val databaseCursor = MutableLiveData<Cursor>()
    val cursor:LiveData<Cursor>get()=databaseCursor
    init {
        Log.d(TAG,"BrainstormViewModel: created")
        getApplication<Application>().contentResolver.registerContentObserver(Brainstorm.CONTENT_URI,
        true,contentObserver)
        loadBrainstorms()

    }

    private fun loadBrainstorms(){
        val projection = arrayOf(Brainstorm.Columns.ID,
        Brainstorm.Columns.TITLE, Brainstorm.Columns.OWNER)
        val sortOrder = "${Brainstorm.Columns.TITLE}, ${Brainstorm.Columns.OWNER}"
        val cursor = getApplication<Application>().contentResolver.query(Brainstorm.CONTENT_URI, projection,
        null, null, sortOrder)
        databaseCursor.postValue(cursor)

    }

    override fun onCleared() {
        Log.d(TAG,"onCleared: calles")
        getApplication<Application>().contentResolver.unregisterContentObserver(contentObserver)
    }

}