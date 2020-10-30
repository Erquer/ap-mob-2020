package com.example.apmob

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_main.*

private const val TAG = "LOGGED_ACTIVITY"

class LoggedActivity : AppCompatActivity(),AddBrainstorm.OnAddBrainstormClicked {

    var user:UserClass?=null

    private var mTwoPane = false
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate:starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)
        setSupportActionBar(findViewById(R.id.toolbar))
        val userID = intent.getLongExtra("userID",1)
        Log.d(TAG,"onCreate: read incoming user ID: $userID")
        val cursor = contentResolver.query(User.CONTENT_URI,null,"${User.Columns.ID} = $userID",null,null)
        var id=0L
        var login = ""
        var password = ""
        if(cursor!= null){
            cursor.use {
                while(it.moveToNext()){
                    with(it){
                        id = getLong(0)
                        login = getString(1)
                        password = getString(2)
                    }
                }
            }
        }
        user = UserClass(login,password)
        user?.id = id
        Log.d(TAG,"onCreate: added user with login: ${user?.login}")

        mTwoPane = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Log.d(TAG, "onCreate: twoPane is $mTwoPane")
        val fragment = supportFragmentManager.findFragmentById(R.id.brainstorm_details_container)
        if (fragment != null) {
            //there was an existing fragment to register user, make sure the panes are set correctly
            showRegisterPane()
        } else {
            brainstorm_details_container.visibility = if (mTwoPane) View.VISIBLE else View.GONE
            main_fragment.visibility = View.VISIBLE
        }

    }

    override fun onStart() {
        super.onStart()
        userLogged.text = if(user != null){
            user!!.login
        }else{
            "Error Loading user"
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.logged_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
//            R.id.mainmenu_settings -> true
            R.id.logout -> {
                //logout user, back to main fragment.
                Log.d(TAG,"onOptionsItemSelected: user logging out")
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
//                val newFragment = MainActivityFragment()
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_fragment, newFragment).commit()
            }
            R.id.newBrainstorm ->{
                Log.d(TAG,"onOptionsItemSelected: new Brainstorm clicked")
                newBrainstormRequest(user)
            }
            R.id.myBrainstorms ->{
                Log.d(TAG,"onOptionsItemSelected: clicked")
                //TODO in multiuser app option.
            }
            R.id.allBrainstorms ->{
                Log.d(TAG,"onOptionsItemSelected: clicked")
                //TODO in multiuser app option.
            }
            android.R.id.home -> {
                Log.d(TAG, "onOprionsItemSelected: home button pressed")
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.brainstorm_details_container)
                removeRegisterPane(fragment)
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onAddBrainstormButtonClicked() {
        Log.d(TAG,"addBrainstorm: starts")
        val fragment = supportFragmentManager.findFragmentById(R.id.brainstorm_details_container)
        removeRegisterPane(fragment)
    }


    /* ******************
        Private functions
       ******************
     */
    private fun removeRegisterPane(fragment: Fragment? = null) {
        Log.d(TAG, "removeRegisterPane: called")

        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
        //set the visibility of the right hand pane
        brainstorm_details_container.visibility = if (mTwoPane) View.INVISIBLE else View.GONE
        //show the left hand pane
        main_fragment.visibility = View.VISIBLE
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
    private fun showRegisterPane() {
        brainstorm_details_container.visibility = View.VISIBLE
        main_fragment.visibility = if (mTwoPane) View.VISIBLE else View.GONE
    }
    private fun newBrainstormRequest(user: UserClass?) {
        Log.d(TAG, "newBrainstormRequest: starts")

        val newFragment = AddBrainstorm.newInstance(user!!)
        supportFragmentManager.beginTransaction()
            .replace(R.id.brainstorm_details_container, newFragment)
            .commit()
        showRegisterPane()
        Log.d(TAG, "Exiting userAddRequest")
    }
}