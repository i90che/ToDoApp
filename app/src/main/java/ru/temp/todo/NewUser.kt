package ru.temp.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_new_user.*

class NewUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        val auth = FirebaseAuth.getInstance()

        btnCreateNewUser.setOnClickListener {
            val email = etNewUserName.text.toString()
            val password = etNewUserPassword.text.toString()


            if (correctInput(email, password)) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        makeToast("Пользователь успешно создан!")
                        //val intentSignIn = Intent(this, SignIn::class.java)
                        auth.signOut()
                        //startActivity(intentSignIn)
                        finish()
                    } else {
                        if (password.length < 6) {
                            makeToast("Длинна пароля должна быть больше 5 символов")
                        } else {
                            makeToast("Проверьте правильность введённой почты")
                        }
                    }
                }
            } else {
                makeToast("Проверьте правильность введённой почты и пароля")
            }
        }
    }

    private fun makeToast(toastText: String){
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun correctInput(email: String? ,password: String?): Boolean{
        return !(email.isNullOrEmpty() || password.isNullOrEmpty())
    }
}