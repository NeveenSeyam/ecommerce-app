package com.example.projectlab.Adapter.Main_Page

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectlab2.Activity.ItemDetailsActivity
import com.example.finalprojectlab2.Activity.Login.StartActivity
import com.example.finalprojectlab2.R
import com.example.projectlab.Model.Mian_Home_page.TopProducts
import kotlinx.android.synthetic.main.main_show_rv_sale.view.*
import com.example.projectlab.Adapter.Main_Page.SelaAdapter.MyViewHolder as MyViewHolder1


class SelaAdapter(var context: Context , var data: MutableList<TopProducts>) :
    RecyclerView.Adapter<MyViewHolder1>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Item_img : ImageView = itemView.Item_img
        val nameOfMarkra = itemView.nameOfMarkra
        val nameOfitem = itemView.nameOfitem
        val Rating = itemView.Rating
        val realNum = itemView.realNum
        var Card_Item = itemView.Card_Item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder1 {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.main_show_rv_sale, parent, false)
        return MyViewHolder1(
            itemView
        )
    }
    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(holder: MyViewHolder1, position: Int) {

        Glide.with(context).load(data[position].Item_img).into(holder.Item_img);
        holder.nameOfMarkra.text = data[position].nameOfBrand
        holder.nameOfitem.text = data[position].nameOfitem
        holder.Rating.rating = data[position].Rating
        holder.realNum.text = data[position].realNum.toString()
        context = holder.Card_Item.context
        holder.Card_Item.setOnClickListener {

          //  Toast.makeText(activity,"Student Details : ${data[position].name}", Toast.LENGTH_SHORT).show()
           var intent = Intent (context, ItemDetailsActivity::class.java)
            intent.putExtra("id",data[position].id)
            Log.d("aaa1" ,data[position].id)
            context.startActivity(intent)
        }
    }
}



