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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_to_do_item.*
import kotlinx.android.synthetic.main.activity_edit_to_do_item.*
import kotlinx.android.synthetic.main.confirm_delete.view.*
import ru.temp.todo.model.ToDoItem
import java.text.SimpleDateFormat
import java.util.*

class EditToDoItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_to_do_item)

        val auth = FirebaseAuth.getInstance()
        val authID = auth.uid!!
        val database = FirebaseDatabase.getInstance().getReference(authID)

        val toDoItem = intent.getParcelableExtra<ToDoItem>("TODOEdit")!!

        et_todo_item_text_edit.setText(toDoItem.itemText)
        tv_data_picker_edit.text = toDoItem.date
        cb_done_edit.isChecked = toDoItem.done == "true"

        et_todo_item_text_edit.isEnabled = toDoItem.done == "false"
        tv_data_picker_edit.isEnabled = toDoItem.done == "false"
        tv_data_picker_edit.isFocusable = false


        cb_done_edit.setOnClickListener {
            if (cb_done_edit.isChecked){
                tv_data_picker_edit.isEnabled = false
                et_todo_item_text_edit.isEnabled = false
            } else {
                tv_data_picker_edit.isEnabled = true
                et_todo_item_text_edit.isEnabled = true
            }
        }

        tv_data_picker_edit.setOnClickListener {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.US)
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this, R.style.DatePicker, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val date = sdf.format(selectedDate.time)
                    Log.d("add tod11o", date.toString())
                    tv_data_picker_edit.text = date.toString()
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }



        btn_save.setOnClickListener {

            if (!cb_done_edit.isChecked){
                if (et_todo_item_text_edit.text.trim().isEmpty()) {
                    makeToast("Необходимо заполнить поле «Название»")
                    return@setOnClickListener
                }
            }

            if  (cb_done_edit.isChecked && et_todo_item_text_edit.text.trim().isEmpty()){
                et_todo_item_text_edit.setText("Без названия")
            }

            toDoItem.itemText = et_todo_item_text_edit.text.toString()
            toDoItem.date = tv_data_picker_edit.text.toString()
            toDoItem.done = if (cb_done_edit.isChecked) "true" else "false"

            database.child(toDoItem.key!!).setValue(toDoItem)
                .addOnCompleteListener {
                    //Toast.makeText(this, "Запись успешно добавлена!", Toast.LENGTH_SHORT).show()
                    Log.d("add todo complete", database.child(toDoItem.key!!).toString())

                }.addOnFailureListener {
                    Log.d("add todo uncomplete", database.child(toDoItem.key!!).toString())
                    //Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                }
            val intentMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentMainActivity)
            finish()
        }

        btn_cancel_edit.setOnClickListener {
            val intentMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentMainActivity)
            finish()
        }

        btn_delete_item.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.confirm_delete, null)

            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val  mAlertDialog = mBuilder.show()
            mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mDialogView.btn_yes.setOnClickListener {

                database.child(toDoItem.key!!).removeValue()
                mAlertDialog.dismiss()
                val intentMainActivity = Intent(this, MainActivity::class.java)
                startActivity(intentMainActivity)
                finish()
            }
            mDialogView.btn_no.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    override fun onBackPressed() {
        val intentMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentMainActivity)
        finish()
    }

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }
}