package com.example.finalprojectlab2.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectlab2.Adapter.GatelodgeAdapter
import com.example.finalprojectlab2.Modle.Catalog
import com.example.finalprojectlab2.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_favorites.view.*
import kotlinx.android.synthetic.main.fragment_favorites.view.RV_Favorites_buttom

/**
 * A simple [Fragment] subclass.
 */
class CatalogFragment : Fragment() {
    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title="Catalog"
         val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        root.RV_Favorites_buttom.layoutManager = LinearLayoutManager(context , RecyclerView.VERTICAL,false)

        readData()

        return root
    }
    private fun readData() {
        val datashow = mutableListOf<Catalog>()

        db.collection("Catalog")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val id = document.id
                        val data =
                            document.data
                        val item_Name = data["Catalog_Name"] as String?
                        val item_Img = data["Catalog_Img"] as String?
                        Log.d("aaa1", data.toString() )
                        datashow.add(
                            Catalog(
                                id,
                                item_Name,
                                item_Img
                            ) )  }
                   RV_Favorites_buttom.setHasFixedSize(true)
                    val GatelodgeAdapter1 =  GatelodgeAdapter(context!! ,datashow)
                    RV_Favorites_buttom.adapter = GatelodgeAdapter1
                }
            }
    }


}
