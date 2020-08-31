package ru.temp.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view
import ru.temp.todo.adapters.ToDoItemsAdapter
import ru.temp.todo.model.ToDoItem
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ToDoItemsAdapter.OnToDoItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance()
        val authID = auth.uid!!
        val database = FirebaseDatabase.getInstance().getReference(authID)

        tv_email.text = auth.currentUser?.email.toString()

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                inflateRecyclerView(dataSnapshot)
            }

            override fun onCancelled(p0: DatabaseError) {
                makeToast("Что-то пошло не так")
            }
        })

        fabAdd.setOnClickListener {
            addItemClick()
        }

        iv_logout.setOnClickListener {
            logOutClick(auth)
        }

        tv_email.setOnClickListener{
            settingsClick()
        }


    }

    override fun onItemClick(item: ToDoItem, position: Int) {
        val intentEditToDo = Intent(this, EditToDoItemActivity::class.java)
        intentEditToDo.putExtra("TODOEdit", item)
        startActivity(intentEditToDo)
        finish()
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    private fun inflateRecyclerView(dataSnapshot: DataSnapshot){

        val headerEmpty = resources.getDimension(R.dimen.view_main_header_empty).toInt()
        val headerNotEmpty = resources.getDimension(R.dimen.view_main_header_not_empty).toInt()

        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.setHasFixedSize(true)

        val toDoItems: ArrayList<ToDoItem> = ArrayList()

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
            view.layoutParams.height = headerEmpty
        } else{
            view.layoutParams.height = headerNotEmpty
        }
    }

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun logOutClick(auth: FirebaseAuth){
        auth.signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun settingsClick(){
        startActivity(Intent(this, AccountSettingsActivity::class.java))
        finish()
    }

    private fun addItemClick(){
        val intentAddToDoItem = Intent(this, AddToDoItemActivity::class.java)
        startActivity(intentAddToDoItem)
        finish()
    }
}