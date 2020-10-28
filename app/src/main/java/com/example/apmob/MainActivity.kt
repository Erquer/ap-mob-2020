package com.example.apmob

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*

private const val TAG = "MainActicity"

class MainActivity : AppCompatActivity(), login_layout.OnRegisterClicked {


    //wherer or the activity is in 2-pane mode
    //i.e running landscape, or on a tablet
    private var mTwoPane = false
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate: finished")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        mTwoPane = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Log.d(TAG, "onCreate: twoPane is $mTwoPane")
        var fragment = supportFragmentManager.findFragmentById(R.id.brainstorm_details_container)
        if(fragment!=null){
            //there was an existing fragment to register user, make sure the panes are set correclty
            showRegisterPane()
        }else{
            brainstorm_details_container.visibility = if(mTwoPane) View.VISIBLE else View.GONE
            main_fragment.visibility =View.VISIBLE
        }

    }
    private fun showRegisterPane(){
        brainstorm_details_container.visibility = View.VISIBLE
        main_fragment.visibility = if(mTwoPane) View.VISIBLE else View.GONE
    }
    private fun removeRegisterPane(fragment: Fragment? = null){
        Log.d(TAG,"removeRegisterPane: called")

        if(fragment!=null){
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
        //set the visibility of the right hand pane
        brainstorm_details_container.visibility = if(mTwoPane) View.INVISIBLE else View.GONE
        //show the left hand pane
        main_fragment.visibility = View.VISIBLE
    }

    override fun onRegisterClicked() {
        Log.d(TAG,"onRegisterClicked: called")
        var fragment = supportFragmentManager.findFragmentById(R.id.brainstorm_details_container)
        removeRegisterPane(fragment)
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
        when (item.itemId) {
            R.id.newUser -> userAddRequest(null)
//            R.id.mainmenu_settings -> true

        }
        return super.onOptionsItemSelected(item)

    }
    private fun userAddRequest(user:UserClass?){
        Log.d(TAG,"userAddRequest: starts")

        val newFragment = login_layout.newInstance(user)
        supportFragmentManager.beginTransaction()
            .replace(R.id.brainstorm_details_container,newFragment)
            .commit()
        showRegisterPane()
        Log.d(TAG,"Exiting userAddRequest")
    }

    override fun onStart() {
        Log.d(TAG,"onStart: called")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG,"onResume: called")
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG,"onSaveInstanceState: called")
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        Log.d(TAG,"onPause: called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG,"onStop: called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG,"onDestroy: called")
        super.onDestroy()
    }

}