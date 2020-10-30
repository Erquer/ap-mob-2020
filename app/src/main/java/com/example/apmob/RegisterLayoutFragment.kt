package com.example.apmob

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.register_layout.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "user"

private const val TAG = "REGISTER_FRAGMENT"
/**
 * A simple [Fragment] subclass.
 * Use the [login_layout.newInstance] factory method to
 * create an instance of this fragment.
 */
class login_layout : Fragment() {
    private var user: UserClass? = null
    private var listener: OnRegisterClicked? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_USER)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: starts")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG,"onViewCreated: called")
        if(savedInstanceState == null) {
            val user = user
            if (user != null) {
                //temporary to know how to display data.
                Log.d(TAG, "onViewCreated: user details found, editing user ${user.login}")
                register_login.setText(user.login)
                register_password.setText(user.password)
            } else {
                // no task, so we must be adding a new user, and not editing an existing one
                Log.d(TAG, "onViewCreated: No arguments, adding new record")
            }
        }
    }

    private fun registerUser(){
        //update the database if atleast one field is changed.
        // - There's no need to hit the data base untill this has happened.
        val login = if(register_login.text.isEmpty()){
            ""
        }else{
            register_login.text
        }
        Log.d(TAG,"registerUser: Checking password ${register_password.text} with ${register_password_confirm.text}")
        val password = if(register_password.text.toString() == register_password_confirm.text.toString() && register_password.text.toString().isNotEmpty()){
            register_password.text
        }else{
            "???"
        }
        val values = ContentValues()
        val user = user
        if(user == null){
            Log.d(TAG,"registerUser: registering new user with login: $login and password: $password")
            if(login.isNotEmpty() && password.isNotEmpty() && password != "???"){
                //if everything is fine, we can add new user
                values.apply {
                    put(User.Columns.LOGIN, login.toString())
                    put(User.Columns.PASSWORD, password.toString())
                }
            }else{
                view?.let {
                    Snackbar.make(it, "Wrong data!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
            }
            if(values.size() != 0){
                Log.d(TAG,"registerUser: Adding new User")
                val selection = "${User.Columns.LOGIN} = ?"
                val selectionGroup = arrayOf(login.toString())
                val cursor = activity?.contentResolver?.query(User.CONTENT_URI, arrayOf(User.Columns.LOGIN),
                            selection, selectionGroup,null)
                if (cursor != null) {
//                   Log.d(TAG,"registerUser: already exists")
                    var loginFound = ""
                    cursor.use {
                        while (it.moveToNext()){
                            with(it){
                                loginFound = getString(0)
                            }
                        }
                    }
                    Log.d(TAG,"registerUser: $login and probably found $loginFound;")
                    if(login.toString() != loginFound) {
                        Log.d(TAG, "registerUser: adding new user")
                        activity?.contentResolver?.insert(User.CONTENT_URI, values)
                        //TODO: login after successful register.
                        val newUser = UserClass(login.toString(), password.toString())
                        val newFragment = LoggedFragment.newInstance(newUser)
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.main_fragment, newFragment)?.commit()
                    }else{
                        Log.d(TAG,"registerUser: found user with matching login $loginFound to $login ")
                        Snackbar.make(requireView(), "User Already exists", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                }
                cursor?.close()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG,"onActivityCreated: starts")
        super.onActivityCreated(savedInstanceState)

        val actionBar = (listener as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        register_register.setOnClickListener{
            registerUser()
            listener?.onRegisterClicked()
        }

    }
    override fun onAttach(context:Context){
        Log.d(TAG, "onAttach: starts")
        super.onAttach(context)
        if(context is OnRegisterClicked){
            listener = context
        }else{
            throw RuntimeException(context.toString() + " must implement OnRegisterClicked")
        }
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: starts")
        super.onDetach()
        listener = null
    }

    interface OnRegisterClicked{
        fun onRegisterClicked()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param user The user to be registered.
         * @return A new instance of fragment login_layout.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: UserClass?) =
            login_layout().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, user)
                }
            }
    }





    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d(TAG,"onViewStateRestored: called")
        super.onViewStateRestored(savedInstanceState)
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

    override fun onDestroyView() {
        Log.d(TAG,"onDestroyView: called")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG,"onDestroy: called")
        super.onDestroy()
    }

}