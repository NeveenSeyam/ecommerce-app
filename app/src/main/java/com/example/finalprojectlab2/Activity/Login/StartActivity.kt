package com.example.finalprojectlab2.Activity.Login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finalprojectlab2.Activity.MainActivity
import com.example.finalprojectlab2.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null

    override fun onStart() {
        super.onStart()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        //check if user is null
        if (firebaseUser != null) {
            val intent = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        Login_Buttom.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@StartActivity,
                    LoginActivity::class.java
                )
            )
        })
        Creat_Account.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@StartActivity,
                    RegisterActivity::class.java
                )
            )
        })
    }

}
