package com.example.finalprojectlab2.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalprojectlab2.Activity.Login.LoginActivity
import com.example.finalprojectlab2.Activity.Login.RegisterActivity
import com.example.finalprojectlab2.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActvitiy : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        auth= FirebaseAuth.getInstance()
        btn_change.setOnClickListener {
            changePassword()
        }
    }

    private fun  changePassword(){
        if (input_Current_Password.text.isNotEmpty() && input_password.text.isNotEmpty() && input_ConfirmationPassword.text.isNotEmpty()){

            if (input_password.text.toString().equals(input_ConfirmationPassword.text.toString())){

                val user = auth.currentUser
                if (user != null && user.email != null){

                    val credential= EmailAuthProvider
                        .getCredential(user.email!!,input_Current_Password.text.toString())
                    Log.d("aaa3" ,input_password.text.toString() )
                    Log.d("aaa3" ,user.email.toString() )

                    user?.reauthenticate(credential)

                        ?.addOnCompleteListener {it ->
                            if (it.isSuccessful){
                                Toast.makeText(this,"Re-Authentication success", Toast.LENGTH_SHORT).show()
                                Log.d("aaa3" ,input_password.text.toString() )

                                user?.updatePassword(input_password.text.toString())
                                    ?.addOnCompleteListener { it ->
                                        if (it.isSuccessful){
                                            Toast.makeText(this,"Password Changed successfuly ",
                                                Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                            startActivity(Intent(this,LoginActivity::class.java))
                                            finish()
                                        }
                                    }
                            }else{
                                Toast.makeText(this,"Re-Authentication faild", Toast.LENGTH_SHORT).show()
                            }
                        }

                }else{
                    startActivity(Intent(this,RegisterActivity::class.java))
                    finish()
                }

            }else{
                Toast.makeText(this,"Missing matching", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this,"Please Enter all the fields", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(this, MainActivity::class.java).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        )
    }

}
