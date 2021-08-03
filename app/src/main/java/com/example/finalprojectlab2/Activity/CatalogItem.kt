package com.example.finalprojectlab2.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalprojectlab2.Fragment.CatalogFragment
import com.example.finalprojectlab2.R
import com.example.projectlab.Adapter.Main_Page.SelaAdapter
import com.example.projectlab.Model.Mian_Home_page.TopProducts
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_catalog_item.*


class CatalogItem : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var catalogName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_item)
        catalogName = intent.getStringExtra("catalogName")


        readData()

       RV_New.layoutManager = GridLayoutManager(this,  2)
        //rvStudents.layoutManager = GridLayoutManager(this,2)
       RV_New.setHasFixedSize(true)


    }

    private fun readData() {


        val datashow = mutableListOf<TopProducts>()

        db.collection("item").whereEqualTo("item_Catlog" ,catalogName )
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val id = document.id
                        val data =
                            document.data
                        val item_Name = data["item_Name"] as String?
                        val item_Brand = data["item_Brand"] as String?
                        val item_Price = data["item_Price"] as String?
                        val item_Rating = data["item_Rating"]
                        Log.d("aaa4", data.toString() )
                        val item_Img = data["item Img"] as String?
                        datashow.add(
                            TopProducts(
                                id,
                                item_Img,
                                item_Name,
                                item_Brand,
                                item_Rating.toString().toFloat(),
                                item_Price

                            )
                        )
                    }
                    RV_New.setHasFixedSize(true)
                    val NewAdapter = SelaAdapter(this,datashow)
                    RV_New.adapter = NewAdapter
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val i = Intent(this, MainActivity::class.java)
        startActivity(i)


    }

}
