package com.example.finalprojectlab2.Activity.Login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectlab2.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {


    var send_email: EditText? = null
    var btn_reset: Button? = null

    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        btn_reset_password.setOnClickListener(View.OnClickListener {
            val email = email.getText().toString()
            if (email == "") {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "All fileds are required!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                firebaseAuth!!.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "Please check you Email",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(
                                this@ResetPasswordActivity,
                                LoginActivity::class.java
                            )
                        )
                    } else {
                        val error = task.exception!!.message
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }
}
