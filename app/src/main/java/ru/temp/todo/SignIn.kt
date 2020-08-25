package ru.temp.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {

    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()

        btnSignIn.setOnClickListener {
            pbEnteringAccount.visibility = View.VISIBLE
            login(etSignInName.text.toString(), etSignInPassword.text.toString())
        }

        tvNoAccount.setOnClickListener {
            newUser()
        }

        tvPasswordRecovery.setOnClickListener {
            forgetPassword()
        }
    }

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun login(email: String, password: String) {

        if (correctInput(email , password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        //if sign in is successful login and goto main activity
                        val user = auth.currentUser
                        val intentMainActivity = Intent(this, MainActivity::class.java)
                        startActivity(intentMainActivity)
                        finish()
                    } else {
                        pbEnteringAccount.visibility = View.INVISIBLE
                        if (password.length < 6) {
                            makeToast("Длинна пароля должна быть больше 5 символов")
                        } else {
                            makeToast("Проверьте правильность введённой почты и пароля")
                        }
                    }
                }
        } else {
            pbEnteringAccount.visibility = View.INVISIBLE
            makeToast("Проверьте правильность введённой почты и пароля")
        }
    }

    private fun correctInput(email: String? ,password: String?): Boolean{
        return !(email.isNullOrEmpty() || password.isNullOrEmpty())
    }


    private fun forgetPassword() {
        val intentRecoverPass = Intent(this, ForgetPass::class.java)
        startActivity(intentRecoverPass)
    }

    private fun newUser() {
        val intentNewUser = Intent(this, NewUser::class.java)
        startActivity(intentNewUser)
    }
}