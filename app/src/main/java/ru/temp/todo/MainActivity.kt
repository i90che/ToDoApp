package ru.temp.todo

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_change_password.view.*
import kotlinx.android.synthetic.main.activity_change_password.view.et_new_password_confirm
import kotlinx.android.synthetic.main.activity_edit_to_do_item.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view
import kotlinx.android.synthetic.main.confirm_delete.view.*
import ru.temp.todo.adapters.ToDoItemsAdapter
import ru.temp.todo.model.ToDoItem
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ToDoItemsAdapter.OnToDoItemClickListener {

    lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val authID = auth.uid!!
        val database = FirebaseDatabase.getInstance().getReference(authID)
        tv_email.text = auth.currentUser?.email.toString()

        val toDoItems: ArrayList<ToDoItem> = ArrayList()

        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.setHasFixedSize(true)


        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    for (i in dataSnapshot.children){
                        val toDoItem = i.getValue(ToDoItem::class.java)!!
                        toDoItems.add(toDoItem)
                    }
                }
                val adapter = ToDoItemsAdapter(toDoItems, this@MainActivity)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

                if (toDoItems.isEmpty()){
                    view.layoutParams.height = resources.getDimension(R.dimen.view_main_header_empty).toInt()
                } else{
                    view.layoutParams.height = resources.getDimension(R.dimen.view_main_header_not_empty).toInt()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        fabAdd.setOnClickListener { view ->
            val intentAddToDoItem = Intent(this, AddToDoItem::class.java)
            startActivity(intentAddToDoItem)
            finish()
        }

        iv_logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }

        tv_email.setOnClickListener{
            startActivity(Intent(this, ChangePassword::class.java))
            finish()
        }


    }

    override fun onItemClick(item: ToDoItem, position: Int) {
        val intentEditToDo = Intent(this, EditToDoItem::class.java)
        Log.d("add todo complete", item.toString())
        intentEditToDo.putExtra("TODOEdit", item)
        startActivity(intentEditToDo)
        finish()
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }
}