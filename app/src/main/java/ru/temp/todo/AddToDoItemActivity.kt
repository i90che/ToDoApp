package ru.temp.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_to_do_item.*
import android.app.DatePickerDialog
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ru.temp.todo.model.ToDoItem
import java.text.SimpleDateFormat
import java.util.*

class AddToDoItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do_item)

        val authID = FirebaseAuth.getInstance().uid
        val database = FirebaseDatabase.getInstance().getReference(authID!!)

        tv_data_picker.setOnClickListener {
            setDateClick()
        }

        btn_add.setOnClickListener {
            addItemClick(et_todo_item_text.text.toString(), database)
        }

        btn_cancel.setOnClickListener {
            backToMainActivity()
        }
    }

    override fun onBackPressed() {
        backToMainActivity()
    }

    private fun addItemClick(itemText: String?, database: DatabaseReference) {

        if (itemText.isNullOrEmpty()) {
            makeToast("Необходимо заполнить поле «Название»")
            return
        }

        val key = database.push().key!!
        val todoItem = ToDoItem()
        todoItem.itemText = et_todo_item_text.text.toString()
        todoItem.done = "false"
        todoItem.date = tv_data_picker.text.toString()
        todoItem.key = key

        addItem(todoItem, database)
        backToMainActivity()
    }

    private fun addItem(toDoItem: ToDoItem, database: DatabaseReference) {
        database.child(toDoItem.key!!).setValue(toDoItem)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    makeToast("Запись успешно добавлена!")
                } else {
                    makeToast("Произошла ошибка")
                }
            }.addOnFailureListener {
                makeToast("Произошла ошибка")
            }
    }

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun backToMainActivity() {
        val intentMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentMainActivity)
        finish()
    }

    private fun setDateClick() {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        val now = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            R.style.DatePicker,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = sdf.format(selectedDate.time)
                tv_data_picker.text = date.toString()
                Log.d("add tod11o", date.toString())
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }
}