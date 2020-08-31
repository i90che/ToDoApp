package ru.temp.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.temp.todo.model.ToDoItem

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash_screen)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val authID = auth.uid
        val toDoItems: ArrayList<ToDoItem> = ArrayList()

        if (user != null) {

            val database = FirebaseDatabase.getInstance().getReference(authID!!)

            database.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    inflateToDoItems(dataSnapshot, toDoItems)
                }
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        } else{
            signIn()
        }
    }

    private fun signIn(){
        val intentSignIn = Intent(this, SignInActivity::class.java)
        startActivity(intentSignIn)
        finish()
    }

    private fun inflateToDoItems(dataSnapshot: DataSnapshot, toDoItems: ArrayList<ToDoItem>){

        if (dataSnapshot.exists()){
            for (i in dataSnapshot.children){
                val toDoItem = i.getValue(ToDoItem::class.java)!!
                toDoItems.add(toDoItem)
            }
        }
        val intentMainActivity = Intent(this@SplashScreenActivity, MainActivity::class.java)
        //intentMainActivity.putParcelableArrayListExtra(TODO_ITEMS, toDoItems)
        startActivity(intentMainActivity)
        finish()
    }
}