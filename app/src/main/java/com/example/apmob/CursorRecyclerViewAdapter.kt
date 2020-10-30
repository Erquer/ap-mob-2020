package com.example.apmob

import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.user_brainstorms_items.*
import java.lang.IllegalStateException

private const val TAG = "CURSOR_RECYCLER_ADAPT"

class BrainstormViewHolder(override val containerView: View):RecyclerView.ViewHolder(containerView),LayoutContainer{
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
            //Remember that ID isn't set in constructor
            brainstorm.id = cursor.getLong(cursor.getColumnIndex(Brainstorm.Columns.ID))
            holder.brainstorm_title.setText(brainstorm.title)
            holder.login_owner.setText(brainstorm.ownerID)
            holder.answers.visibility = View.GONE
            holder.brainstorm_title.visibility = View.VISIBLE
            holder.login_owner.visibility = View.VISIBLE
            holder.showDetails.visibility = View.VISIBLE //TODO: add and onClick fun

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