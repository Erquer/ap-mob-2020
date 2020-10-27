package com.example.apmob

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActicity"

class MainActivity : AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        //TESTS
//        testInsert() - tested
//        testUpdate() - tested
//        testUpdateMultiple() - tested
//        testDelete() - tested
//        testDeleteMultiple() - tested

        // **********************
        // PRINTING RESULTS
        //Projection odpowiada za część Select KOLUMNY WYMIENIONE W PROJEKCI from ....
        val projection = arrayOf(User.Columns.LOGIN, User.Columns.ID)
        // sortColumn odpowiada za część ORDER BY 'sortColumn'
        val sortColumn = User.Columns.ID


//        val cursor = contentResolver.query(User.buildUriFromId(2), projection,null,null, sortColumn)
        val cursor = contentResolver.query(User.CONTENT_URI,null,null,null,sortColumn)
        Log.d(TAG, "************************" )

        cursor.use{
            if (it != null) {
                while (it.moveToNext()){
                    //cycle through all records
                    with(it){
                        val id = getLong(0)
                        val login = getString(1)
                        val password = getString(2)
                        val result = "ID: $id: Login = $login; Password= $password"
                        Log.d(TAG,"onCreate: reading data: $result")
                    }
                }
            }
        }
        Log.d(TAG, "************************")
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun testInsert(){
        val values = ContentValues().apply{
            put(User.Columns.LOGIN, "Bear")
            put(User.Columns.PASSWORD, "5443")
        }
        val uri = contentResolver.insert(User.CONTENT_URI, values)
        Log.d(TAG,"New row ID (in uri) is $uri")
        Log.d(TAG, "id (in uri) is ${uri?.let { User.getId(it) }}")

    }
    private fun testUpdate(){
        val values = ContentValues().apply{
            put(User.Columns.LOGIN, "Zeus")
            put(User.Columns.PASSWORD, "5443")
        }
        val userURI = User.buildUriFromId(4)
        val rowAffected = contentResolver.update(userURI, values, null,null)
//        Log.d(TAG,"New row ID (in uri) is $uri")
//        Log.d(TAG, "id (in uri) is ${uri?.let { User.getId(it) }}")
        Log.d(TAG,"Number of rows updated is $rowAffected")
    }
    private fun testUpdateMultiple(){
        val values = ContentValues().apply{
            put(User.Columns.LOGIN, "Bear ")
            put(User.Columns.PASSWORD, "254")
        }
        val selection = User.Columns.LOGIN + " = ?"
        val selectionArgs = arrayOf("Bear Grylls")
//        val userURI = User.buildUriFromId(5)
        val rowAffected = contentResolver.update(User.CONTENT_URI, values, selection,selectionArgs)
//        Log.d(TAG,"New row ID (in uri) is $uri")
//        Log.d(TAG, "id (in uri) is ${uri?.let { User.getId(it) }}")
        Log.d(TAG,"Number of rows updated is $rowAffected")

    }
    private fun testDelete(){
        val userURI = User.buildUriFromId(5)
        val rowAffected = contentResolver.delete(userURI,  null,null)
//        Log.d(TAG,"New row ID (in uri) is $uri")
//        Log.d(TAG, "id (in uri) is ${uri?.let { User.getId(it) }}")
        Log.d(TAG,"Number of rows deleted is $rowAffected")
    }
    private fun testDeleteMultiple(){

        val selection = User.Columns.LOGIN + " = ?"
        val selectionArgs = arrayOf("Apollo")
//        val userURI = User.buildUriFromId(5)
        val rowAffected = contentResolver.delete(User.CONTENT_URI, selection,selectionArgs)
//        Log.d(TAG,"New row ID (in uri) is $uri")
//        Log.d(TAG, "id (in uri) is ${uri?.let { User.getId(it) }}")
        Log.d(TAG,"Number of rows deleted is $rowAffected")
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.mainmenu_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}