package ru.temp.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*
import ru.temp.todo.utils.PASSWORD_LENGTH

class AccountSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        btn_change_password_yes.setOnClickListener {
            changePasswordClick()
            backToMainActivity()
        }

        btn_change_password_no.setOnClickListener {
            backToMainActivity()
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

    private fun changePasswordClick() {
        val auth = FirebaseAuth.getInstance()

        if (!checkCorrectInputPass(
                et_old_password.text.toString(),
                et_new_password.text.toString(),
                et_new_password_confirm.text.toString()
            )
        ) return


        val credential = EmailAuthProvider
            .getCredential(auth.currentUser!!.email.toString(), et_old_password.text.toString())

        auth.currentUser!!.reauthenticate(credential)
            .addOnCompleteListener {
                changePassword(it, auth)
            }
    }

    private fun checkCorrectInputPass(
        oldPass: String?,
        newPass: String?,
        newPassConfirm: String?
    ): Boolean {
        if (oldPass.isNullOrEmpty() || newPass.isNullOrEmpty() || newPassConfirm.isNullOrEmpty()) {
            makeToast("Одно из полей пустое!")
            return false
        }
        if ("$newPass" != "$newPassConfirm") {
            makeToast("Новые пароли не совпадают!")
            return false
        }

        if ("$newPassConfirm".length < PASSWORD_LENGTH) {
            makeToast("Длина пароля должна быть больше 5 символов!")
            return false
        }

        if ("$newPass" == "$newPassConfirm" && "$newPass" == "$oldPass") {
            makeToast("Старый и новый пароли совпадают")
            return false
        }

        return true
    }

    private fun changePassword(it: Task<Void>, auth: FirebaseAuth) {
        if (it.isSuccessful) {
            auth.currentUser!!.updatePassword(et_new_password_confirm.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        makeToast("Пароль успешно сменён!")
                        backToMainActivity()
                    } else {
                        makeToast("Ошибка")
                    }
                }
        } else {
            makeToast("Ошибка, проверьте правильность введённых данных")
            return
        }
    }

    override fun onBackPressed() {
        backToMainActivity()
    }
}