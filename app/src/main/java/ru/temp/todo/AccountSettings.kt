package ru.temp.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val auth = FirebaseAuth.getInstance()
        val authID = auth.uid!!

        btn_change_password_yes.setOnClickListener {

            if (!checkPass(
                    et_old_password.text.toString(),
                    et_new_password.text.toString(),
                    et_new_password_confirm.text.toString()
                )
            ) return@setOnClickListener

            val credential = EmailAuthProvider
                .getCredential(auth.currentUser?.email.toString(), et_old_password.text.toString())

            auth.currentUser?.reauthenticate(credential)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        auth.currentUser?.updatePassword(et_new_password_confirm.text.toString())
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    makeToast("Пароль успешно сменён!")
                                } else {
                                    makeToast("Ошибка")
                                }
                            }
                    } else {
                        makeToast("Ошибка, проверьте правильность введённых данных")
                        return@addOnCompleteListener
                    }
                }

        }

        btn_change_password_no.setOnClickListener {
            val intentMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentMainActivity)
            finish()
        }


    }

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun checkPass(oldPass: String?, newPass: String?, newPassConfirm: String?): Boolean {

        Log.d("emails", oldPass + newPass + newPassConfirm)

        if (oldPass.isNullOrEmpty() || newPass.isNullOrEmpty() || newPassConfirm.isNullOrEmpty()) {
            makeToast("Одно из полей пустое!")
            return false
        }
        if (newPass.toString() != newPassConfirm.toString()) {
            makeToast("Новые пароли не совпадают!")
            return false
        }

        if (newPassConfirm.toString().length < 6) {
            makeToast("Длина пароля должна быть больше 5 символов!")
            return false
        }

        if (newPass.toString() == newPassConfirm.toString() && newPass.toString() == oldPass.toString()) {
            makeToast("Старый и новый пароли совпадают")
            return false
        }

        return true
    }

    override fun onBackPressed() {
        val intentMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentMainActivity)
        finish()
    }
}