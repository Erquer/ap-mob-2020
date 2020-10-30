package com.example.apmob

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_add_brainstorm.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.RuntimeException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val ARG_PARAM1 = "user"
private const val TAG = "ADD_BRAINSTORM"
/**
 * A simple [Fragment] subclass.
 * Use the [AddBrainstorm.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddBrainstorm : Fragment() {
    var user: UserClass? = null
    private var listener: OnAddBrainstormClicked? = null

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
        return inflater.inflate(R.layout.fragment_add_brainstorm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val actionBar = (listener as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        addBrainstormButton.setOnClickListener {
            addNewBrainstorm()
            listener?.onAddBrainstormButtonClicked()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnAddBrainstormClicked){
            listener = context
        }else{
            throw RuntimeException(context.toString() + " must implement.")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun addNewBrainstorm(){
        // add to Database new Brainstorm
        Log.d(TAG,"addBrainstorm: starts")
        val user = user
        Log.d(TAG,"addBrainstorm: user with id ${user?.id} wants to add new brainstorm")
        //create new brainstorm
        val brainTitle = if(brainstormTitle.text.isEmpty()){
            "???"
        }else{
            brainstormTitle.text.toString()
        }
        val count = if(answerCount.text.isEmpty()){
            7
        }else{
            Integer.parseInt(answerCount.text.toString())
        }
        val values = ContentValues()
        if(brainTitle != "???") {
            values.apply {
                put(Brainstorm.Columns.TITLE, brainTitle)
                put(Brainstorm.Columns.OWNER, user!!.id)
            }
            val generator = AnswerGenerator()
            val newURI = activity?.contentResolver?.insert(Brainstorm.CONTENT_URI,values)
            val newID = Brainstorm.getId(newURI!!)
            //if user didnt pass the count default value is 7, userID default 1, as a guest, other values when app will be able to serve multiple users.
            val answers :ArrayList<out ContentValues> = generator.generateAnswers(newID, count, 1)
            val countAns = activity?.contentResolver?.bulkInsert(Answer.CONTENT_URI, answers.toTypedArray())
            Log.d(TAG,"addBrainstorm: added all data including answers: $countAns")
        }


    }

    interface OnAddBrainstormClicked{
        fun onAddBrainstormButtonClicked()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment AddBrainstorm.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: UserClass) =
            AddBrainstorm().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }
}