package com.example.apmob

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_logged.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "user"
private const val TAG = "LOGGED_FRAGMENT"

/**
 * A simple [Fragment] subclass.
 * Use the [LoggedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoggedFragment : Fragment() {
    private var user: UserClass? = null

    private val viewModel by lazy{ ViewModelProviders.of(requireActivity()).get(BrainstormViewModel::class.java)}
    private val mAdapter = CursorRecyclerViewAdapter(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_PARAM1)
        }
        viewModel.cursor.observe(this, Observer { cursor -> mAdapter.swapCursor(cursor)?.close() })

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_logged, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG,"onViewCreated:called")
        super.onViewCreated(view, savedInstanceState)
        user_login?.text = user?.login
        logged_brainstorm_list.layoutManager = LinearLayoutManager(context)
        logged_brainstorm_list.adapter = mAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(TAG,"onCreateOptionsMenu:called")
        menu.clear()
        inflater.inflate(R.menu.logged_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id){
            R.id.logout -> {
                //logout user, back to main fragment.
                Log.d(TAG,"onOptionsItemSelected: user logging out")
                val newFragment = MainActivityFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_fragment, newFragment)?.commit()
            }
            R.id.newBrainstorm->{
                Log.d(TAG,"onOptionsItemSelected: user logging out")
                val newFragment = AddBrainstorm.newInstance(user!!)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_fragment, newFragment)?.commit()
                //showAddingPane()
            }

            R.id.myBrainstorms ->{
                //show on recyclerView only my brainstorms
                Log.d(TAG, "onOptionsItemSelected: show only my brainstorms")
            }

            R.id.allBrainstorms -> {
                //show all brainstorms in database
                Log.d(TAG,"onOptionsItemSelected: show all brainstorms")

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        Log.d(TAG,"onAttach:called")
        super.onAttach(context)
    }
    override fun onStart() {
        Log.d(TAG, "onStart: called")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume: called")
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState: called")
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        Log.d(TAG, "onPause: called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop: called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: called")
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoggedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: UserClass) =
            LoggedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }


}
