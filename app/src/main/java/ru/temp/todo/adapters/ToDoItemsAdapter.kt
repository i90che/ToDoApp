package ru.temp.todo.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.temp.todo.R
import ru.temp.todo.model.ToDoItem
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ToDoItemsAdapter(val toDoItems: ArrayList<ToDoItem>, var clickListener: OnToDoItemClickListener) :
    RecyclerView.Adapter<ToDoItemsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val toDoText: TextView = itemView.findViewById(R.id.toDoText)
        val toDoCheckBox: CheckBox = itemView.findViewById(R.id.toDoCheckBox)
        val toDoDate: TextView = itemView.findViewById(R.id.tv_date)

        fun initialize (item: ToDoItem, action: OnToDoItemClickListener) {
            toDoText.text = item.itemText
            //toDoCheckBox.isChecked = item.done
            if (item.done == "true"){
                toDoDate.text = "Выполнено!"
                toDoDate.setTextColor(Color.rgb(40,170,3))
            }else {
                toDoDate.text = "Закончить к " + item.date
                toDoDate.setTextColor(Color.rgb(246,62,165))
            }

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_rows, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = toDoItems.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initialize(toDoItems[position], clickListener)
    }

    interface OnToDoItemClickListener{
        fun onItemClick(item: ToDoItem, position: Int)
    }
}