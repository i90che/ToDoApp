package ru.temp.todo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*
import ru.temp.todo.utils.PASSWORD_LENGTH

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btnSignIn.setOnClickListener {
            loginClick(etSignInName.text.toString(), etSignInPassword.text.toString())
        }

        tvNoAccount.setOnClickListener {
            newUserClick()
        }

        tvPasswordRecovery.setOnClickListener {
            forgetPasswordClick()
        }
    }



    private fun loginClick(email: String, password: String) {

        hideKeyboard(currentFocus ?: View(this))

        if (password.length < PASSWORD_LENGTH) {
            makeToast("Длина пароля должна быть больше 5 символов")
            return
        }

        pbEnteringAccount.visibility = View.VISIBLE
        val auth = FirebaseAuth.getInstance()

        if (correctInput(email , password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    enterAccount(it)
                }
        }
    }

    private fun forgetPasswordClick() {
        val intentRecoverPass = Intent(this, ForgetPassActivity::class.java)
        startActivity(intentRecoverPass)
    }

    private fun newUserClick() {
        val intentNewUser = Intent(this, NewUserActivity::class.java)
        startActivity(intentNewUser)
    }

    private fun startMainActivity(){
        val intentMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentMainActivity)
        finish()
    }

    private fun enterAccount(it: Task<AuthResult>){
        if (it.isSuccessful) {
            pbEnteringAccount.visibility = View.INVISIBLE
            startMainActivity()
        } else {
            pbEnteringAccount.visibility = View.INVISIBLE
            makeToast("Проверьте правильность введённой почты и пароля")
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
    }

    private fun correctInput(email: String? ,password: String?): Boolean{
        return !(email.isNullOrEmpty() || password.isNullOrEmpty())
    }
}