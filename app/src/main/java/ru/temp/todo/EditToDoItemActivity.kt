package ru.temp.todo

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_to_do_item.*
import kotlinx.android.synthetic.main.confirm_delete.view.*
import ru.temp.todo.model.ToDoItem
import java.text.SimpleDateFormat
import java.util.*

class EditToDoItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_to_do_item)

        val auth = FirebaseAuth.getInstance()
        val authID = auth.uid!!
        val database = FirebaseDatabase.getInstance().getReference(authID)

        val toDoItem = intent.getParcelableExtra<ToDoItem>("TODOEdit")!!

        initUi(toDoItem)

        cb_done_edit.setOnClickListener {
            doneTaskClick()
        }

        tv_data_picker_edit.setOnClickListener {
            setDateClick()
        }

        btn_save.setOnClickListener {
            saveItemClick(database, toDoItem.key!!)
        }

        btn_cancel_edit.setOnClickListener {
            backToMainActivity()
        }

        btn_delete_item.setOnClickListener {
            deleteItemClick(database, toDoItem)
        }
    }

    private fun initUi(toDoItem: ToDoItem) {
        et_todo_item_text_edit.setText(toDoItem.itemText)
        tv_data_picker_edit.text = toDoItem.date
        cb_done_edit.isChecked = toDoItem.done == "true"

        et_todo_item_text_edit.isEnabled = toDoItem.done == "false"
        tv_data_picker_edit.isEnabled = toDoItem.done == "false"
        tv_data_picker_edit.isFocusable = false
    }

    override fun onBackPressed() {
        backToMainActivity()
    }

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun doneTaskClick() {
        if (cb_done_edit.isChecked) {
            tv_data_picker_edit.isEnabled = false
            et_todo_item_text_edit.isEnabled = false
        } else {
            tv_data_picker_edit.isEnabled = true
            et_todo_item_text_edit.isEnabled = true
        }
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
                Log.d("add tod11o", date.toString())
                tv_data_picker_edit.text = date.toString()
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    private fun backToMainActivity() {
        val intentMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentMainActivity)
        finish()
    }

    private fun deleteItemClick(database: DatabaseReference, toDoItem: ToDoItem) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.confirm_delete, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialogView.btn_yes.setOnClickListener {
            database.child(toDoItem.key!!).removeValue()
            mAlertDialog.dismiss()
            backToMainActivity()
        }
        mDialogView.btn_no.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun saveItemClick(database: DatabaseReference, key: String) {

        if (!cb_done_edit.isChecked && et_todo_item_text_edit.text.trim().isEmpty()) {
            makeToast("Необходимо заполнить поле «Название»")
            return
        }

        if (cb_done_edit.isChecked && et_todo_item_text_edit.text.trim().isEmpty()) {
            et_todo_item_text_edit.setText("Без названия")
        }

        val toDoItem = readToDoItemFromUi(key)
        updateItem(toDoItem, database)
        backToMainActivity()
    }

    private fun updateItem(toDoItem: ToDoItem, database: DatabaseReference) {
        database.child(toDoItem.key!!).setValue(toDoItem)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    makeToast("Запись успешно обновлена!")
                } else {
                    makeToast("Произошла ошибка")
                }
            }.addOnFailureListener {
                makeToast("Произошла ошибка")
            }
    }

    private fun readToDoItemFromUi(key: String): ToDoItem {
        val toDoItem = ToDoItem()
        toDoItem.itemText = et_todo_item_text_edit.text.toString()
        toDoItem.date = tv_data_picker_edit.text.toString()
        toDoItem.done = if (cb_done_edit.isChecked) "true" else "false"
        toDoItem.key = key
        return toDoItem
    }
}