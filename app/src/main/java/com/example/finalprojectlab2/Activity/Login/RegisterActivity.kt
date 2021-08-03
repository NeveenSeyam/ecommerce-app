package com.example.finalprojectlab2.Activity.Login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectlab2.Activity.MainActivity
import com.example.finalprojectlab2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = FirebaseAuth.getInstance()
    val ت: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        btn_signup.setOnClickListener(View.OnClickListener {
            val txt_username = input_name.text.toString()
            val txt_email: String = input_email.text.toString()
            val txt_password: String = input_password.text.toString()
            val txt_phone: String = input_phonenumber.text.toString()
            if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(
                    txt_password
                ) || TextUtils.isEmpty(txt_phone)

            ) {
                Toast.makeText(
                    this@RegisterActivity,
                    "All fileds are required",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (txt_password.length < 6) {
                Toast.makeText(
                    this@RegisterActivity,
                    "password must be at least 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                register(txt_username, txt_email, txt_password, txt_phone)
            }
        })
    }

    private fun register(
        username: String,
        usermail: String,
        password: String,
        phone_number: String
    ) {
        var auth: FirebaseAuth? = FirebaseAuth.getInstance()
        auth!!.createUserWithEmailAndPassword(usermail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val fireUser = FirebaseAuth.getInstance().currentUser
                    val firebaseUser = auth!!.currentUser!!
                    val userid = firebaseUser.uid
                    var fb = FirebaseFirestore.getInstance()
                    val hashMap = HashMap<String, Any>()
                               hashMap["id"] = userid
                               hashMap["username"] = username
                               hashMap["usermail"] = usermail
                               hashMap["phone_number"] = phone_number
                               hashMap["AboutMe"] = "none"
                               hashMap["Location"] = "none"
                               hashMap["Skills"] = "none"
                               hashMap["Career"] = "none"
                               hashMap["imageURL"] = "default"
                               hashMap["itemIBuy"] = ArrayList<Any>()
                    var ref = fb.collection("users").document(fireUser!!.uid)
                        .set(hashMap)
                        .addOnCompleteListener { task ->
                                   if (task.isSuccessful) {
                                       Log.d("aaa1 ", "successfu")

                                       val intent =
                                Intent(this@RegisterActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                       }
                    }


                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                  //(4)
                    Toast.makeText(
                        this@RegisterActivity,
                        "You can't register woth this email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


}
