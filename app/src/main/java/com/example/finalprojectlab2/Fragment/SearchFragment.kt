package com.example.finalprojectlab2.Fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalprojectlab2.R
import com.example.projectlab.Adapter.Main_Page.SelaAdapter
import com.example.projectlab.Model.Mian_Home_page.TopProducts
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title="Explore"
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_search, container, false)


        root.result_list.visibility = View.GONE
        //readAllData()

        root.search_field.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("aaa1",query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("aaa1",newText)
                readData(newText)
                root.result_list.visibility = View.VISIBLE

                return true
            }
        })


        root.result_list.layoutManager = GridLayoutManager(context,  2)

        return root
    }

    private fun readData(s :CharSequence) {
        val datashow = mutableListOf<TopProducts>()
        var db = FirebaseFirestore.getInstance()

        db.collection("item").whereEqualTo("item_Search" ,s )
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
                        val item_Rating = data["item_Rating"] as String?
                        val item_Catlog = data["item_Catlog"] as String?
                        Log.d("aaa1", data.toString() )
                        val item_Img = data["item Img"] as String?
                        datashow.add(
                            TopProducts(
                                id,
                                item_Img,
                                item_Name,
                                item_Brand,
                                item_Rating!!.toFloat(),
                                item_Price

                            )
                        )
                    }
                    result_list.setHasFixedSize(true)
                    val NewAdapter = SelaAdapter(context!!,datashow)
                    result_list.adapter = NewAdapter
                }
            }
    }

/*
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
                        val item_Catlog = data["item_Catlog"] as String?
                        val item_Rating = data["item_Rating"] as String?
                        Log.d("aaa1", data.toString() )
                        val item_Img = data["item Img"] as String?
                        datashow.add(
                            TopProducts(
                                id,
                                item_Img,
                                item_Name,
                                item_Brand,
                                item_Rating!!.toFloat(),
                                item_Price

                            )
                        )
                    }
                    result_list.setHasFixedSize(true)
                    val NewAdapter = SelaAdapter(context!!,datashow)
                    result_list.adapter = NewAdapter
                }
            }
            .addOnFailureListener {
                Log.d("aaa1", "faidled" )

            }
    }
*/


}
