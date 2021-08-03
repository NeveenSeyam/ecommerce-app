package com.example.finalprojectlab2.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalprojectlab2.R
import com.example.projectlab.Adapter.Main_Page.SelaAdapter
import com.example.projectlab.Model.Mian_Home_page.TopProducts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_most_item_reting.*

class MostItemReting : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_most_item_reting)
        raterec.layoutManager = GridLayoutManager(this,  2)
        raterec.setHasFixedSize(true)
        productData()

    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(this, MainActivity::class.java).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        )
    }

    private fun productData(){
        val datashow = mutableListOf<TopProducts>()
        db.collection("item").whereGreaterThanOrEqualTo("item_Rating",1 )
            .orderBy("item_Rating", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    for (document in task.result!!){
                        val id = document.id
                        val data =
                            document.data
                        val item_Name = data["item_Name"] as String?
                        val item_Brand = data["item_Brand"] as String?
                        val item_Price = data["item_Price"] as String?
                        val item_Catlog = data["item_Catlog"] as String?
                        val item_Rating = data["item_Rating"]
                        Log.d("aaa1", data.toString() )
                        val item_Img = data["item_Img"] as String?
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
                    val adapter = SelaAdapter(this ,datashow)
                    raterec.adapter = adapter
                }
            }



    }

}
