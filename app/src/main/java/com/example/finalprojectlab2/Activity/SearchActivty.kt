package com.example.finalprojectlab2.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalprojectlab2.R
import com.example.projectlab.Adapter.Main_Page.SelaAdapter
import com.example.projectlab.Adapter.Main_Page.SelaAdapter1
import com.example.projectlab.Model.Mian_Home_page.TopProducts
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search_activty.*

class SearchActivty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_activty)

        result_list.visibility = View.GONE
        readAllData()

           search_field.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
               override fun onQueryTextSubmit(query: String?): Boolean {
                   Log.d("aaa1",query)
                   return false
               } override fun onQueryTextChange(newText: String): Boolean {
                   Log.d("aaa1",newText)
                   readData(newText.toLowerCase())
                   result_list.visibility = View.VISIBLE
                   return true   }
           })
       result_list.layoutManager = GridLayoutManager(this,  2)
    }
    private fun readData(s :String) {
        val datashow = mutableListOf<TopProducts>()
        var db = FirebaseFirestore.getInstance()

        db.collection("item").orderBy("item_Search")
            .startAt(s)
            .endAt(s + "\uf8ff")
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
                        val item_Catlog = data["item_Catlog"] as String?
                        val item_Rating = data["item_Rating"]
                        Log.d("aaa1", data.toString() )
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
                    result_list.setHasFixedSize(true)
                    val NewAdapter = SelaAdapter1(this,datashow)
                    result_list.adapter = NewAdapter
                }
            }
            .addOnFailureListener {
                Log.d("aaa1", "faidled" )

            }
    }
    private fun readAllData() {
        val datashow = mutableListOf<TopProducts>()
        var db = FirebaseFirestore.getInstance()

        db.collection("item")
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
                        val item_Catlog = data["item_Catlog"] as String?
                        Log.d("aaa1", data.toString() )
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
                    result_list.setHasFixedSize(true)
                    val NewAdapter = SelaAdapter(this,datashow)
                    result_list.adapter = NewAdapter
                }
            }
            .addOnFailureListener {
                Log.d("aaa1", "faidled" )

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
