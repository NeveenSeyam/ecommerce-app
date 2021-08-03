package com.example.finalprojectlab2.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.finalprojectlab2.Activity.ProdcutAndCatalog.EditItem
import com.example.finalprojectlab2.R
import com.example.projectlab.Model.Mian_Home_page.TopProducts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detiles_item.*
import kotlinx.android.synthetic.main.activity_detiles_item.rb_item_Rating as rb_item_Rating1

class ItemDetailsActivity : AppCompatActivity() {
    lateinit var item_Rating :Any
    var id = ""
    var Ratings_System_flag =  false
    var isRating = false
    lateinit var old_Rating: RatingBar
    val fireUser = FirebaseAuth.getInstance().currentUser
    lateinit var Ratings_System : ArrayList<String>
    var itemCounter: Any? = 0.0

    lateinit var rating : ArrayList<String>
    lateinit var itemIBuy : ArrayList<String>
    var db = FirebaseFirestore.getInstance()

    var datashow = mutableListOf<TopProducts>()

    lateinit var alertDialog:AlertDialog


    var  datafire= ""
    var itemname = ""
    var item_brand = ""
    var item_catlog = ""
    var item_price= ""
    var item_dec=""
    var img = ""

    var con = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detiles_item)
         id = intent.getStringExtra("id")
        geitemData(id)
        Ratings_System(fireUser!!.uid)
        getuserData(fireUser!!.uid)
        if (Ratings_System_flag){
           // rb_item_Rating1.isIndicator= true
        }
        ic_mene.setOnClickListener {
            val popup = android.widget.PopupMenu(this, ic_mene)
            popup.menuInflater.inflate(R.menu.popup_menu,
                popup.getMenu())
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.Edit ->

                        goEditItem()
                    R.id.Delet ->
                        delee()

                }
                true
            }
            popup.show()
        }
        ic_mene.visibility = View.GONE

        if (fireUser!!.email.equals("admin@gmail.com")){
            //   floating_action_button.visibility = View.GONE
            ic_mene.visibility = View.VISIBLE
        }

        anamtin.setOnClickListener {
            var intent = Intent (this, MapsActivity::class.java)
            intent.putExtra("item_id" , id)

            this.startActivity(intent)
        }
        btnAddCatalog.setOnClickListener {
            itemCounter()

            datashow.add(
                TopProducts(
                    id,
                    img,
                    itemname,
                    item_brand,
                    item_Rating.toString().toFloat(),
                    item_price
                  )
            )

            itemIBuy()

        }

    }



    fun goEditItem(){
        var intent = Intent (this, EditItem::class.java)
        intent.putExtra("idfromitem",id)
        Log.d("aaa1", id)
        this.startActivity(intent)
    }
    fun delee(){
        var db = FirebaseFirestore.getInstance()

        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(" Delete this  item")
        alertDialog.setMessage("are u show u wont to delet this item?")
        alertDialog.setIcon(R.drawable.ic_delete)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Yes") { dialogInterface, i ->
            Toast.makeText(applicationContext, "This deleted", Toast.LENGTH_SHORT).show()
            db.collection("item").document(id)
                .delete()
                .addOnSuccessListener {
                    Log.d( "aaa1", "DocumentSnapshot successfully deleted!")
                    val intent = Intent(this , MainActivity ::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                        e -> Log.w("aaa1", "Error deleting document", e) }

        }
        alertDialog.setNegativeButton("No") { dialogInterface, i ->
            Toast.makeText(applicationContext, "no", Toast.LENGTH_SHORT).show()
        }

        alertDialog.show()
    }
    override fun onStop() {
        super.onStop()
        Log.d("aaa2" ,isRating.toString() )

        Log.d("aaa1" , Ratings_System_flag.toString())
        if (Ratings_System_flag == false) {

            var Rating = rb_item_Rating1.rating.toString()
            if(Rating.toDouble() >= 1 ) {
                Ratings_System.add(fireUser!!.uid)
                userArray(Ratings_System)
                rating.add(Rating)
                ratingArray(rating)
            }
        }
    }
    fun Rating (Rating : Float){
        var db = FirebaseFirestore.getInstance()
        var ref = db.collection("item").document(id)
        ref.update("item_Rating" , Rating)
            .addOnSuccessListener {
                Toast.makeText(this,"Success Edit",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failer Edit",Toast.LENGTH_LONG).show()
            }
    }
    fun ratingArray (Ratings_array : ArrayList<String>){
        var db = FirebaseFirestore.getInstance()
        var ref = db.collection("item").document(id)
        ref.update("rating" , Ratings_array)
            .addOnSuccessListener {
                Rating(con)

                Toast.makeText(this,"Success Edit",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failer Edit",Toast.LENGTH_LONG).show()
            }
    }
    fun userArray ( Ratings_System : ArrayList<String>){
        var db = FirebaseFirestore.getInstance()
        var ref = db.collection("item").document(id)
        ref.update("Ratings_System" , Ratings_System)
            .addOnSuccessListener {
                Toast.makeText(this,"Success Edit",Toast.LENGTH_LONG).show()
             //   startActivity(Intent(this,MainActivity::class.java))
            }
            .addOnFailureListener{
                Toast.makeText(this,"Failer Edit",Toast.LENGTH_LONG).show()
            }
    }
    fun Ratings_System(uId : String){
        Log.d("aaa1" , uId)
        var db = FirebaseFirestore.getInstance()
        var ref = db.collection("item").document(id)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var  data=documentSnapshot.data
                     Ratings_System=data!!["Ratings_System"] as ArrayList<String>
                     rating= data["rating"] as ArrayList<String>

                    for (item in Ratings_System) {

                    if(item.equals(uId)){
                        var Rating_Item = rb_item_Rating1
                        Ratings_System_flag = true
                    }
                    }
                    var counter= 0f
                    for (item in rating) {
                        Log.d("aaa1",rating.toString() )
                         counter +=  item.toString().toFloat()

                    }
                    Log.d("aaa1","rating Conterr >>"+counter.toString() )
             con = counter / rating.size
                    Log.d("aaa1","rating.size  >>" +rating.size.toString() )
                    Log.d("aaa1","reslt >> "+con.toString() )


                }
                else{
                    Toast.makeText(this,"Failer",Toast.LENGTH_LONG).show()
                }


            }
    }
    fun geitemData(id : String){

        var db = FirebaseFirestore.getInstance()
        var ref = db.collection("item").document(id)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var  data=documentSnapshot.data
                     itemname = data!!["item_Name"] as String
                     item_brand = data!!["item_Brand"] as String
                     item_catlog = data["item_Catlog"] as String
                     item_price= data["item_Price"] as String
                     item_dec=data["item_Description"] as String
                    item_Rating=data["item_Rating"]!!
                     img=data["item_Img"] as String
                    tv_item_name_details.text=itemname
                    nameOfmarkca.text=item_brand
                    tv_price_details.text=item_price
                    tv_item_details.text=item_dec
                    rb_item_Rating1.rating=item_Rating.toString().toFloat()
                    old_Rating= rb_item_Rating1
//                    img_details.setImageURI(Uri.parse(img))
                    Glide.with(getApplicationContext()).load(img).into(img_details);
                }
                else{
                    Toast.makeText(this,"Failer",Toast.LENGTH_LONG).show()
                }


            }
    }

    fun getuserData(id : String){

        var db = FirebaseFirestore.getInstance()
        var ref = db.collection("users").document(id)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var  data=documentSnapshot.data
                        datashow = data!!["itemIBuy"] as MutableList<TopProducts>

                }
                else{
                    Toast.makeText(this,"Failer",Toast.LENGTH_LONG).show()
                }


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

    fun itemIBuy (){

        var db = FirebaseFirestore.getInstance()
        var ref = db.collection("users").document(fireUser!!.uid)
        ref.update("itemIBuy" , datashow)
            .addOnSuccessListener {
                Toast.makeText(this,"Success Edit",Toast.LENGTH_LONG).show()
                //startActivity(Intent(this,MainActivity::class.java))

            }
            .addOnFailureListener{
                Toast.makeText(this,"Failer Edit",Toast.LENGTH_LONG).show()
            }

    }



    fun itemCounter() {
        var db = FirebaseFirestore.getInstance()
        var ref = db.collection("item").document(id)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    var data = documentSnapshot.data
                    itemCounter = data!!["itemCounter"]
                    Log.d("aaa2", itemCounter.toString())
                } else {
                    Toast.makeText(this, "Failer", Toast.LENGTH_LONG).show()
                }
                db = FirebaseFirestore.getInstance()
                var ref = db.collection("item").document(id)

                var itemCounter = itemCounter.toString().toDouble() + 1.0
                Log.d("aaa2", itemCounter.toString())

                ref.update("itemCounter", itemCounter)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Success Edit", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failer Edit", Toast.LENGTH_LONG).show()
                    }
            }


    }}










