package ru.temp.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.*
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_new_user.*
import ru.temp.todo.utils.PASSWORD_LENGTH

class NewUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        val auth = FirebaseAuth.getInstance()

        btnCreateNewUser.setOnClickListener {
            newUserClick(auth)
        }
    }

    private fun makeToast(toastText: String) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
    }

    private fun correctInput(email: String?, password: String?): Boolean {
        return !(email.isNullOrEmpty() || password.isNullOrEmpty())
    }

    private fun newUserClick(auth: FirebaseAuth) {

        val email = etNewUserName.text.toString()
        val password = etNewUserPassword.text.toString()

        if (password.length < PASSWORD_LENGTH) {
            makeToast("Длинна пароля должна быть больше 5 символов")
            return
        }

        if (correctInput(email, password)) {
            pbCreatingAccount.visibility = VISIBLE
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                newUser(auth, it)
            }
            pbCreatingAccount.visibility = INVISIBLE
        } else {
            makeToast("Проверьте правильность введённой почты и пароля")
            pbCreatingAccount.visibility = INVISIBLE
        }
    }

    private fun newUser(auth: FirebaseAuth, it: Task<AuthResult>) {
        if (it.isSuccessful) {
            makeToast("Пользователь успешно создан!")
            auth.signOut()
            finish()
        }
    }
}