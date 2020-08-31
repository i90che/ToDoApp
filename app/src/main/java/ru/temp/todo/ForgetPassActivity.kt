package ru.temp.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_pass.*

class ForgetPassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        val auth = FirebaseAuth.getInstance()

        btnRecoverPassword.setOnClickListener {
            recoverPasswordClick(auth)
        }
    }

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun correctInput(email: String?): Boolean {
        return !(email.isNullOrEmpty())
    }

    private fun recoverPasswordClick(auth: FirebaseAuth) {
        if (correctInput(etEmailRecover.text.toString())) {
            auth.sendPasswordResetEmail(etEmailRecover.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    makeToast("Пароль успешно сброшен!")
                    auth.signOut()
                    finish()
                } else {
                    makeToast("Проверьте правильность введённой почты")
                }
            }
        } else {
            makeToast("Проверьте правильность введённой почты")
        }
    }
}