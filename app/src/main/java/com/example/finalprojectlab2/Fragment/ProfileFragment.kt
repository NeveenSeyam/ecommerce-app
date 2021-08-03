package com.example.finalprojectlab2.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.finalprojectlab2.*
import com.example.finalprojectlab2.Activity.ChangePasswordActvitiy
import com.example.finalprojectlab2.Activity.EditProfile
import com.example.finalprojectlab2.Activity.Login.StartActivity
import com.example.finalprojectlab2.Activity.MostItemReting
import com.example.finalprojectlab2.Activity.MostItemSelling
import com.example.finalprojectlab2.Modle.users
import com.example.projectlab.Adapter.Main_Page.SelaAdapter
import com.example.projectlab.Model.Mian_Home_page.TopProducts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    var geiuserDatauseres = mutableListOf<users>()
    val fireUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title="Profile"
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        root.Sing_out.setOnClickListener {
            val i = Intent(context, EditProfile::class.java)
            startActivity(i)
        }
        root.LogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(
                Intent(context!!, StartActivity::class.java).setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            )
        }

        geiuserData(fireUser!!.uid)

        root.personalinfo.visibility=View.VISIBLE
        root.experience.visibility=View.GONE

        root.personalinfobtn.setOnClickListener {
            personalinfo.visibility=View.VISIBLE
            experience.visibility=View.GONE
            personalinfobtn.setTextColor(resources.getColor(R.color.blue))
            experiencebtn.setTextColor(resources.getColor(R.color.grey))
        }

        root.experiencebtn.setOnClickListener {
            personalinfo.visibility=View.GONE
            experience.visibility=View.VISIBLE
            personalinfobtn.setTextColor(resources.getColor(R.color.grey))
            experiencebtn.setTextColor(resources.getColor(R.color.blue))
        }

        root.item.setOnClickListener {

            startActivity(
                Intent(context!!, MostItemSelling::class.java).setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            )
        }
        root.MostItemRating.setOnClickListener {

            startActivity(
                Intent(context!!, MostItemReting::class.java).setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            )
        }

        root.ChangePassword.setOnClickListener {

            startActivity(
                Intent(context!!, ChangePasswordActvitiy::class.java).setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            )
        }
        root.admin_dashboard1.visibility = View.GONE
        root.admin_das.text = "User Setting"
        if (fireUser!!.email.equals("admin@gmail.com")){
            //   floating_action_button.visibility = View.GONE
            root.admin_das.text = "Admin DashBord"

            root.admin_dashboard1.visibility = View.VISIBLE
        }


        val fireUser = FirebaseAuth.getInstance().currentUser
        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("users").document(fireUser!!.uid)
            ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var data = documentSnapshot.data
                    root.profile_uesrname.text = data!!["username"] as String
                    root.email_profile.text= data["usermail"] as String
                    root.numberid_profile.text= data["phone_number"] as String
                   root.location_profile.text=data["Location"] as String
                    val sKilles=data["Skills"] as String?
                    if (sKilles !=null)
                        root.SkillsProfile.text = sKilles
                  val carerr=data["career"] as String?
                    if (carerr !=null)
                        root.CareerProfile.text = carerr
                   val aboutMe=data["AboutMe"] as String?
                    if (aboutMe !=null)
                        root.txt_AboutMe.text = aboutMe
                    val img = data["imageURL"] as String?
                    Log.d("aaa1" ,img )
                    if (img.equals("default") ||img.equals("") ) {

                        root.profile_img.setImageResource(R.mipmap.ic_launcher)

                    } else { //change this
                        Glide.with(this@ProfileFragment).load(img).into(root.profile_img)
                    }




                    root.profile_img.setImageURI(Uri.parse(img))

                } else {
                    Toast.makeText(activity!!, "Failer", Toast.LENGTH_LONG).show()
                }
            }

        return root
    }

    fun geiuserData(id : String){

        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("users").document(id)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    val myObject = documentSnapshot.toObject(users::class.java)
                    geiuserDatauseres.add(myObject!!)
                    var  data=documentSnapshot.data

                    Log.d("aaa2" ,"MyList" +documentSnapshot.data!!.get("itemIBuy").toString() )
                    Log.d("aaa2" ,"MyListtest" +myObject.itemIBuy.toString() )

                    val adapter = SelaAdapter(context!! ,myObject.itemIBuy as MutableList<TopProducts> )
                    Profile_rv.layoutManager = GridLayoutManager(context,  2)
                    Profile_rv.setHasFixedSize(true)
                    Profile_rv.adapter = adapter
                }

                else{
                    Toast.makeText(context,"Failer",Toast.LENGTH_LONG).show()
                }


            }
    }

    fun  onClick(){

    }


}
