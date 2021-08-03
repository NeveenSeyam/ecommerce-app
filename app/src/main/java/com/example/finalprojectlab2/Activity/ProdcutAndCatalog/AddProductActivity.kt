package com.example.finalprojectlab2.Activity.ProdcutAndCatalog

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojectlab2.Activity.MainActivity
import com.example.finalprojectlab2.Activity.MapsActivity
import com.example.finalprojectlab2.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set


class AddProductActivity : AppCompatActivity()  , OnMapReadyCallback {
    var flagMarking = false
    final val TAG = "aaa1"
    var imageURI: String = ""
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var rating_counter = ArrayList<String>()
    var Ratings_System = ArrayList<String>()
    var newLag= ""
    var newLat=""
    var imageURLFirestore = ""
    private val PICK_IMAGE_REQUEST = 71
    private var dialog: ProgressDialog? = null
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        SpinnerList()
        input_img_of_Item
        input_img_of_Item.setOnClickListener { launchGallery() }


        backbtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        btn_AddItem.setOnClickListener {

            dialog = ProgressDialog(this)
            dialog!!.setMessage(" please wait.");
            dialog!!.show()
            if(filePath != null){
                val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
                val uploadTask = ref?.putFile(filePath!!)

                val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        Log.d("aaa2","done")

                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
        Log.d("aaa1" , "yser")
                        dialog!!.dismiss()
                        imageURLFirestore = downloadUri.toString()
                        val item: MutableMap<String, Any> = HashMap()


                        val itemName = input_name_of_item.text.toString()
                        val itemBrand = input_name_of_Marka.text.toString()
                        val itemPrice = price_item.text.toString()
                        val itemDescription = input_desctption.text.toString()
                        val itemCatlog1 = Spinner_sex.selectedItem.toString()
                        item["item_Name"] = itemName
                        item["item_Search"] = itemName.toLowerCase()
                        item["item_Brand"] = itemBrand
                        item["item_Price"] = itemPrice
                        item["item_Catlog"] = itemCatlog1
                        item["rating"] = ArrayList<String>()
                        item["Ratings_System"] = ArrayList<String>()
                        item["item_Rating"] = 0.0
                        item["item_Description"] = itemDescription
                        item["item_Img"] = imageURLFirestore
                        item["item_Local_lat"] = newLat
                        item["item_Local_lag"] = newLag
                        item["itemCounter"] = 0.0



                        Toast.makeText(
                            this,
                            newLag+ ", " + newLat,
                            Toast.LENGTH_SHORT
                        ).show()

                        db.collection("item")
                            .add(item)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot added with ID: " + documentReference.id
                                )
                            }
                            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)

                    } else {
                        // Handle failures
                    }
                }?.addOnFailureListener{

                }
            }else{
                Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            }

        }


    }

    fun SpinnerList() {

        val list: MutableList<String> = ArrayList()
        db.collection("Catalog")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val id = document.id
                        val data =
                            document.data
                        val Catalog_Name = data["Catalog_Name"] as String?
                        list.add(Catalog_Name!!)
                    }
                }
                val dataAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, list
                )
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                Spinner_sex.adapter = dataAdapter
            }
    }
    override fun onMapReady(p0: GoogleMap?) {
       val mMap = p0


        mMap!!.uiSettings.isZoomControlsEnabled = true

        //   productMap(item_Local_lag.toDouble(),item_Local_lat.toDouble())
        mMap.setOnMapClickListener { point ->

            if (flagMarking ) {
                Log.d("aaa1", "map")
                mMap.clear()
                flagMarking = false

            }
            if (flagMarking == false) {
              mMap.addMarker(
                  MarkerOptions().position(LatLng(point.latitude, point.longitude)).title("Procdunt Marking")

                )
                Log.d("aaa1", "map")
                flagMarking = true
            }



             newLag=point.latitude.toString()
             newLat=point.longitude.toString()
            Toast.makeText(
                this,
                point.latitude.toString() + ", " + point.longitude,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                input_img_of_Item.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}
