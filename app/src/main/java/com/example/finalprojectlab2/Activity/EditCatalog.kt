package com.example.finalprojectlab2.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.finalprojectlab2.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_edit_catalog.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class EditCatalog : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var dialog: ProgressDialog? = null
    private var filePath: Uri? = null
    var id = ""
    private var storageReference: StorageReference? = null
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(this, MainActivity::class.java).setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_catalog)
        id = intent.getStringExtra("cID")
        storageReference = FirebaseStorage.getInstance().reference
        input_img_of_Item.setOnClickListener { launchGallery() }

        btn_AddItem.setOnClickListener {
            val catalogName = input_name_of_item.text.toString()
            if(filePath != null){
                val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
                val uploadTask = ref?.putFile(filePath!!)
                dialog = ProgressDialog(this)
                dialog!!.setMessage(" please wait.");
                dialog!!.show()
                val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                })?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        dialog!!.dismiss()
                        val downloadUri = task.result
                        val imgURL = downloadUri.toString()
                        val Catalog: MutableMap<String, Any> = HashMap()
                        Catalog["Catalog_Name"] = catalogName

                        Catalog["Catalog_Img"] = imgURL


                        val db: FirebaseFirestore = FirebaseFirestore.getInstance()


                        db.collection("Catalog").document(id)
                            .update(Catalog)
                            .addOnSuccessListener { documentReference ->

                            }
                            .addOnFailureListener { e -> Log.w("aaa1", "Error adding document", e) }
                        val i =  Intent(this, MainActivity::class.java)
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


    private fun uploadImage(catalogName : String){


    }

}
