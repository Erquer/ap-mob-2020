package com.example.apmob

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.register_layout.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "user"
private const val TAG = "LOGIN_FRAGMENT"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var user: UserClass? = null
    private var listener: OnLoginClicked? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_PARAM1)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun loginUser() {
        Log.d(TAG,"loginUser: called")
        //check if there is in database any user with given login, if not login user, else show message.
        val userLogin = if (login_user_login.text.isEmpty()) {
            "???"
        } else {
            login_user_login.text.toString()
        }
        val userPassword = if (login_user_password.text.isEmpty()) {
            "???"
        } else {
            login_user_password.text.toString()
        }
        val projection = arrayOf(User.Columns.LOGIN, User.Columns.PASSWORD)
        val selection = "${User.Columns.LOGIN} = ?"
        val selectionArgs = arrayOf(userLogin)
        val cursor = activity?.contentResolver?.query(
            User.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )
        if (cursor != null && userLogin != "???") {
            var foundLogin = ""
            var foundPassword = ""

            cursor.use {
                while (it.moveToNext()) {
                    with(it) {
                        foundLogin = getString(0)
                        foundPassword = getString(1)
                    }
                }
                Log.d(TAG, "loginUser: Found match $foundLogin and $foundPassword")
            }
            if (foundLogin.isNotBlank()) {
                //there was a match.
                when (userPassword) {
                    foundPassword -> {
                        // password matches, login this user.
                        Log.d(TAG, "Logging in user with login ${userLogin}")
                        val newUser = UserClass(userLogin, userPassword)
                        val newFragment = LoggedFragment.newInstance(newUser)
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.main_fragment, newFragment)?.commit()
                    }
                    else -> {
                        // incorrect password. Show message
                        Snackbar.make(requireView(), "Incorrect data", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                }
            }
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated: starts")
        super.onActivityCreated(savedInstanceState)

        val actionBar = (listener as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        loginButton.setOnClickListener {
            loginUser()
            listener?.onLoginClicked()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnLoginClicked){
            listener = context
        }else{
            throw RuntimeException(context.toString() + " must implement OnRegisterClicked")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnLoginClicked {
        fun onLoginClicked()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        @JvmStatic
        fun newInstance(user: UserClass?) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, user)

                }
            }
    }
}