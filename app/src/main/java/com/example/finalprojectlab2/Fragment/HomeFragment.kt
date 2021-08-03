package com.example.projectlab.Fragment.Main_show


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectlab2.Adapter.ImageAdapter
import com.example.finalprojectlab2.Modle.users
import com.example.finalprojectlab2.R
import com.example.projectlab.Adapter.Main_Page.SelaAdapter
import com.example.projectlab.Model.Mian_Home_page.TopProducts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    var db = FirebaseFirestore.getInstance()
    val fireUser = FirebaseAuth.getInstance().currentUser
    val uId = fireUser!!.uid
    lateinit var itemIBuy : ArrayList<String>




    var geiuserData = mutableListOf<TopProducts>()
    var geiuserDatauseres = mutableListOf<users>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title="Home"
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //Silader
        readData()
        val adapter =
            ImageAdapter(activity!!)
        root.img_Random_item.setAdapter(adapter)
        root.img_Random_item.visibility = View.GONE
        productData()
        root.RV_Sale.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        root.RV_New.layoutManager = GridLayoutManager(context,  2)
        root.RV_Sale.setHasFixedSize(true)
        root.RV_New.setHasFixedSize(true)
        return root
    }


    private fun readData() {
        val datashow = mutableListOf<TopProducts>()
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
                                item_Price ))}
                    RV_New.setHasFixedSize(true)
                    val NewAdapter = SelaAdapter(context!! ,datashow)
                    RV_New.adapter = NewAdapter

                }
            }
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
                           ))}
                    val adapter = SelaAdapter(context!! ,datashow)
                    RV_Sale.adapter = adapter
                }
            }



    }






    }






