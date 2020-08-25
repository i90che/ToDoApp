package ru.temp.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_to_do_item.*
import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ru.temp.todo.model.ToDoItem
import java.text.SimpleDateFormat
import java.util.*

class AddToDoItem : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do_item)

        auth = FirebaseAuth.getInstance()

        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        tv_data_picker.text = sdf.format(Calendar.getInstance().time)

        tv_data_picker.setOnClickListener {
                val now = Calendar.getInstance()
                val datePicker = DatePickerDialog(
                    this, R.style.DatePicker, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(Calendar.YEAR, year)
                        selectedDate.set(Calendar.MONTH, month)
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val date = sdf.format(selectedDate.time)
                        tv_data_picker.text = date.toString()
                        Log.d("add tod11o", date.toString())
                    },
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                )
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }


        btn_add.setOnClickListener {

            if (et_todo_item_text.text.trim().isEmpty()) {
                makeToast("Необходимо заполнить поле «Название»")
                return@setOnClickListener
            }
            val authID = auth.uid!!
            val database = FirebaseDatabase.getInstance().getReference(authID)
            val key = database.push().key!!
            //make a push so that a new item is made with a unique ID
            val todoItem = ToDoItem()
            todoItem.itemText = et_todo_item_text.text.toString()
            todoItem.done = "false"
            todoItem.date = tv_data_picker.text.toString()
            todoItem.key = key
            Log.d("add todo", todoItem.toString())
            Log.d("add todo db ", database.toString())

            database.child(key).setValue(todoItem)
                .addOnCompleteListener {
                    //Toast.makeText(this, "Запись успешно добавлена!", Toast.LENGTH_SHORT).show()
                    Log.d("add todo complete", database.child(key).toString())


                }.addOnFailureListener {
                    Log.d("add todo uncomplete", database.child(key).toString())
                    //Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                }
            val intentMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentMainActivity)
            finish()
        }

        btn_cancel.setOnClickListener {
            val intentMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentMainActivity)
            finish()
        }
    }

    override fun onBackPressed() {
        val intentMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentMainActivity)
        finish()
        //Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show()
        //super.onBackPressed()
    }

}