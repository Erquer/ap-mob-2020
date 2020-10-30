package com.example.apmob

import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_brainstorms_items.*
import java.lang.IllegalStateException
import kotlin.math.log

private const val TAG = "CURSOR_RECYCLER_ADAPT"

class BrainstormViewHolder(override val containerView: View):RecyclerView.ViewHolder(containerView),LayoutContainer{
    fun bind(brainstorm:BrainstormClass){
        var projection = arrayOf(User.Columns.LOGIN)
        var selection = "${User.Columns.ID} = ${brainstorm.ownerID}"

        val login_owner_cur = containerView.context.contentResolver.query(User.CONTENT_URI,projection,selection, null,null)
        var login = ""
        if(login_owner_cur != null ){
            login_owner_cur.use {
                while (it.moveToNext()){
                    with(it){
                        login = getString(0)
                    }
                }
            }
        }
        login_owner.text = login
        projection = arrayOf(Answer.Columns.answer)
        selection = "${Answer.Columns.brainstormID} = ${brainstorm.id}"
        var answersList = ArrayList<String>()
        val ansCursor = containerView.context.contentResolver.query(Answer.CONTENT_URI,projection,selection,null,null)
        if(ansCursor != null){
            ansCursor.use{
                while (it.moveToNext()){
                    with(it){
                        Log.d(TAG,"answer Finding, adding new answer: ${getString(0)}")
                        answersList.add(getString(0))
                    }
                }
            }
        }
        //populate listView with answers.
        val adapter = ArrayAdapter(containerView.context, android.R.layout.simple_list_item_1, answersList)
        answers.adapter = adapter
        login_owner.visibility = View.VISIBLE
        answers.visibility = View.GONE

        showDetails.setOnClickListener {
            Log.d(TAG,"showDetails: clicked at ${brainstorm.title}")
            if(answers.visibility == View.VISIBLE){
                answers.visibility = View.GONE
            }else{
                answers.visibility = View.VISIBLE
            }
        }
        containerView.setOnLongClickListener {
            Log.d(TAG,"onLongClick: cliced on ${brainstorm.title}")
            true
        }
    }

}
class CursorRecyclerViewAdapter(private var cursor: Cursor?) : RecyclerView.Adapter<BrainstormViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrainstormViewHolder {
      Log.d(TAG,"onCreateViewHolder: new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_brainstorms_items,parent,false)
        return BrainstormViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrainstormViewHolder, position: Int) {
        Log.d(TAG,"onBindViewHolder: starts")
        val cursor = cursor // avoid problem in later code
        if(cursor == null || cursor.count == 0){
            Log.d(TAG,"onBindViewHolder: providing instructions")
            holder.login_owner.setText(R.string.Instruction_heading)
            holder.brainstorm_title.setText("To display any data there must be atleast one user to create Brainstorm, to do so click \"Register\" button and register user")
            holder.login_owner.visibility = View.GONE
            holder.answers.visibility = View.GONE
            holder.showDetails.visibility = View.GONE
        }else{
            if(!cursor.moveToPosition(position)){
                throw IllegalStateException("Couldn't move curor to position $position")
            }
            //Create a task object from the data in the cursor
            val brainstorm = BrainstormClass(
                cursor.getString(cursor.getColumnIndex(Brainstorm.Columns.TITLE)),
                cursor.getInt(cursor.getColumnIndex(Brainstorm.Columns.OWNER))
            )
            brainstorm.id = cursor.getLong(cursor.getColumnIndex(Brainstorm.Columns.ID))
            Log.d(TAG,"onBindViewHolder: adding new item with ${brainstorm.id}, ${brainstorm.title}, ${brainstorm.ownerID}")

            //Remember that ID isn't set in constructor
            brainstorm.id = cursor.getLong(cursor.getColumnIndex(Brainstorm.Columns.ID))
            holder.brainstorm_title.text = brainstorm.title
            val text = "OwnerID = ${brainstorm.ownerID}"
            //TODO: add and onClick fun
            holder.bind(brainstorm)
        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG,"getItemCount: starts")
        val cursor = cursor
        val count = if(cursor == null || cursor.count == 0){
            1
        }else{
            cursor.count
        }
        Log.d(TAG,"returning $count")
        return count
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.
     * The returned old Cursor is *not* closed.
     *
     * @param newCursor The new cursor to be used
     * @return Returns set Cursor, or null if there was't one.
     *
     * If the given new Cursor is the same instance
     * as previously set Cursor,null i also returned.
     */
    fun swapCursor(newCursor: Cursor?):Cursor?{
        if(newCursor === cursor){
            return null
        }
        val numItems = itemCount
        val oldCursor = cursor
        cursor = newCursor
        if(newCursor != null){
            notifyDataSetChanged()
        }else{
            notifyItemRangeChanged(0,numItems)
        }
        return oldCursor
    }
}