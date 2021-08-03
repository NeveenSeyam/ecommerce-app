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
import com.bumptech.glide.Glide
import com.example.finalprojectlab2.Activity.MainActivity
import com.example.finalprojectlab2.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_edit_item.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class EditItem : AppCompatActivity() {
    var imageURI:String=""
    val PERMISSION_ID = 42
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()
    var item_Catalog  = ""
    var lag= ""
    var lat= ""
    final val  TAG = "aaa1"
    private val PICK_IMAGE_REQUEST = 71
    private var dialog: ProgressDialog? = null
    val storageRef = storage.reference
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)
        storageReference = FirebaseStorage.getInstance().reference

        val id: String = intent.getStringExtra("idfromitem")
        Log.d("aaa1", id)


        input_img_of_Item.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }



        var fb = FirebaseFirestore.getInstance()
        var ref = fb.collection("item").document(id)

        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {

                    var  data=documentSnapshot.data
                    val username= data!!["item_Name"] as String
                    val item_Brand=data["item_Brand"] as String
                    val item_Price=data["item_Price"] as String
                     item_Catalog=data["item_Catlog"] as String
                    val item_Description=data["item_Description"] as String
                    val img=data["item_Img"] as String
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
                            Spinner_sex.setSelection(dataAdapter.getPosition(item_Catalog))

                        }

                    input_name_of_item.setText(username)
                    input_name_of_Marka.setText(item_Brand)
                    price_item.setText(item_Price)
                    input_desctption.setText(item_Description)

                    //         profile_img.setImageURI(Uri.parse(img))

                    if (img.equals("default") ||img.equals("") ) {

                        input_img_of_Item.setImageResource(R.mipmap.ic_launcher)

                    } else { //change this
                        Glide.with(this).load(img).into(input_img_of_Item)
                    }

                }else{
                    Toast.makeText(this,"Failer",Toast.LENGTH_LONG).show()
                }

            }

            .addOnFailureListener { exception ->
                Toast.makeText(this,"$exception",Toast.LENGTH_LONG).show()
            }

        SpinnerList()




        btn_AddItem.setOnClickListener {


            if(filePath != null){
                dialog = ProgressDialog(this)
                dialog!!.setMessage(" please wait.");
                dialog!!.show()
                val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
                Log.d("aaa2","--")

                Log.d("aaa2",filePath.toString())
                Log.d("aaa2",ref.toString())

                val uploadTask = ref?.putFile(filePath!!)
                Log.d("aaa2","1111 Heeereee")
                Log.d("aaa2",uploadTask.toString())

                val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    Log.d("aaa2"," Heeereee")

                    if (!task.isSuccessful) {
                        Log.d("aaa2","done")

                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    Log.d("aaa2"," Heeereee 22222")

                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        Log.d("aaa2"," Succ")
                        dialog!!.dismiss()
                        val imageURLFirestore = downloadUri.toString()
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
                        item["item_Description"] = itemDescription
                        item["item_Img"] = imageURLFirestore


                        db.collection("item").document(id).update(item)
                            .addOnSuccessListener { documentReference ->

                            }
                            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)

                    } else {
                        // Handle failures
                    }
                }?.addOnFailureListener{
                    Log.d("aaa2"," Heeereee 33")

                }
            }else{
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
                item["item_Description"] = itemDescription


                db.collection("item").document(id).update(item)
                    .addOnSuccessListener { documentReference ->

                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)            }

        }
    }





    fun SpinnerList() {


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            Log.d("aaa2" , filePath.toString())
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                input_img_of_Item.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}



