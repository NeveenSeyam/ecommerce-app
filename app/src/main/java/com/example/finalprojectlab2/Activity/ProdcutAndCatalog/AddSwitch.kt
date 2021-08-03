package com.example.finalprojectlab2.Activity.ProdcutAndCatalog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalprojectlab2.R
import kotlinx.android.synthetic.main.activity_add_switch.*

class AddSwitch : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_switch)
        btnAddProdcut.setOnClickListener {
            val i =  Intent(this, AddProductActivity::class.java)
            startActivity(i)
        }
        btnAddCatalog.setOnClickListener {
            val i =  Intent(this, AddCatalog::class.java)
            startActivity(i)
        }


    }
}
