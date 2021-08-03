package com.example.finalprojectlab2.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectlab2.Activity.MainActivity
import com.example.finalprojectlab2.Activity.CatalogItem
import com.example.finalprojectlab2.Activity.EditCatalog
import com.example.finalprojectlab2.Modle.Catalog
import com.example.finalprojectlab2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.catalog_recview.view.*


class GatelodgeAdapter(var context: Context ,  var data: MutableList<Catalog>) :
    RecyclerView.Adapter<GatelodgeAdapter.MyViewHolder>() {
    var db = FirebaseFirestore.getInstance()
    val fireUser = FirebaseAuth.getInstance().currentUser

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Gatelodge_name = itemView.tv_gatelodge
        val Gatelodge_img = itemView.im_gatelodge
        var Card_Item = itemView.Card_Item
        var Menu = itemView.meue

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.catalog_recview, parent, false)
        return MyViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.Gatelodge_name.text = data[position].Gatelodge_name
       // holder.Gatelodge_img.setImageResource(data[position].Gatelodge_img)
        Glide.with(context).load(data[position].Gatelodge_img).into(holder.Gatelodge_img);

        holder.Card_Item.setOnClickListener {

            //  Toast.makeText(activity,"Student Details : ${data[position].name}", Toast.LENGTH_SHORT).show()
            var intent = Intent (context, CatalogItem::class.java)
            intent.putExtra("catalogName",data[position].Gatelodge_name)
            Log.d("aaa1" ,data[position].Gatelodge_name)
            context.startActivity(intent)
        }

        holder.Menu.visibility = View.GONE
        if (fireUser!!.email.equals("admin@gmail.com")){
            //   floating_action_button.visibility = View.GONE
            holder.Menu.visibility = View.VISIBLE
        }

        holder.Menu.setOnClickListener(View.OnClickListener  {
            //menu category
            val popup= PopupMenu(context,  holder.Menu)
            popup.inflate(R.menu.popup_menu)
            popup.setOnMenuItemClickListener {item->
                when(item.itemId){
                    R.id.Edit-> {
                        var i= Intent(context, EditCatalog::class.java)
                        i.putExtra("cID",data[position].id)
                        context.startActivity(i)
                        Toast.makeText(context,"Edit btn",Toast.LENGTH_LONG).show()}
                    R.id.Delet->{
                        var dialog= AlertDialog.Builder(context)
                        dialog.setTitle("Delete")
                        dialog.setMessage("you want delete this Category?")
                        dialog.setIcon(R.drawable.ic_delete)
                        dialog.setCancelable(false)
                        dialog.setPositiveButton("Yes"){ dialogInterface,i->
                            db.collection("Catalog").document(data[position].id!!).delete()
                                .addOnSuccessListener {

                                    Toast.makeText(context,"Deleted Success",Toast.LENGTH_LONG).show()

                                    var i= Intent(context, MainActivity::class.java)
                                    context.startActivity(i)

                                }
                                .addOnFailureListener {
                                    Toast.makeText(context,"Deleted Not Success",Toast.LENGTH_LONG).show()
                                }

                        }
                        dialog.setNegativeButton("No"){_,_->


                        }
                        dialog.show()


                        Toast.makeText(context,"Delete",Toast.LENGTH_LONG).show()}
                }

                popup.show()
                true

            }
            popup.show()
        })



    }
}