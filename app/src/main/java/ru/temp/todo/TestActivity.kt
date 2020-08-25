package ru.temp.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test.*
import ru.temp.todo.model.ToDoItem

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        var toDoItems: ArrayList<ToDoItem> = ArrayList()
        toDoItems = intent?.getParcelableArrayListExtra<ToDoItem>(SplashScreen.TODOITEMS) as ArrayList<ToDoItem>
        textView2.text = toDoItems.toString()
    }
}