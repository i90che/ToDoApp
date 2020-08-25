package ru.temp.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ru.temp.todo.model.ToDoItem

class SplashScreen : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    companion object {
        const val TODOITEMS = "toDoItems"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        val authID = auth.uid
        val intentMainActivity = Intent(this@SplashScreen, MainActivity::class.java)

        if (user != null) {

            val database = FirebaseDatabase.getInstance().getReference(authID!!)
            val toDoItems: ArrayList<ToDoItem> = ArrayList()
            Log.d("TODOintentMaindb", database.toString())

            //if user currently sign get data and goto main activity
            database.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    Log.d("TODOintentMain", "start")
                    if (dataSnapshot.exists()){
                        for (i in dataSnapshot.children){
                            val toDoItem = i.getValue(ToDoItem::class.java)!!
                            toDoItems.add(toDoItem)
                            Log.d("TODOintentMain", toDoItem.toString())
                        }
                    }
                    //intentMainActivity.putParcelableArrayListExtra(TODOITEMS, toDoItems)
                    startActivity(intentMainActivity)
                    finish()
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        } else{
            //if user do not currently sign in goto signinscreen
            val intentSignIn = Intent(this, SignIn::class.java)
            startActivity(intentSignIn)
            finish()
        }
    }
}