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
import com.bumptech.glide.Glide
import com.example.finalprojectlab2.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.profile_img
import kotlinx.android.synthetic.main.activity_edit_profile.profile_uesrname
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class EditProfile : AppCompatActivity() {
    var imageURI = ""
    private var progressDialog: ProgressDialog?=null
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    val fireUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        storageReference = FirebaseStorage.getInstance().reference


        // add image
     profile_img.setOnClickListener { launchGallery() }



        var db = FirebaseFirestore.getInstance()
        var ref = db.collection("users").document(fireUser!!.uid)

        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {

                    var  data=documentSnapshot.data
                    val username= data!!["username"] as String
                    val email=data["usermail"] as String
                    val phone=data["phone_number"] as String
                    val AboutMe=data["AboutMe"] as String
                    val img=data["imageURL"] as String
                    val Location=data["Location"] as String
                    val Skills=data["Skills"] as String
                    val Career=data["career"] as String?
                    profile_uesrname.setText(username)
                    txt_Skills.setText(Skills)
                    numberid_profile.setText(phone)
                    txt_aboutMe.setText(AboutMe)
                    email_profile.setText(email)
                    txt_Career.setText(Career)
                    location_profile.setText(Location)
           //         profile_img.setImageURI(Uri.parse(img))

                    if (img.equals("default") ||img.equals("") ) {

                        profile_img.setImageResource(R.mipmap.ic_launcher)

                    } else { //change this
                        Glide.with(this).load(img).into(profile_img)
                    }

                }else{
                    Toast.makeText(this,"Failer",Toast.LENGTH_LONG).show()
                }

            }

            .addOnFailureListener { exception ->
                Toast.makeText(this,"$exception",Toast.LENGTH_LONG).show()
            }




        EditDone.setOnClickListener {
            uploadImage()
        }






    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                profile_img.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage(){
        if(filePath != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)
            showDialog()
            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }}
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                   val imtURL  = downloadUri.toString()
                    Log.d("aaa2" ,imtURL )
                hideDialog()
                    var db = FirebaseFirestore.getInstance()
                    var ref = db.collection("users").document(fireUser!!.uid)
                    val update = HashMap<String,Any>()
                    update["username"] = profile_uesrname.text.toString()
                    update["usermail"] =  email_profile.text.toString()
                    update["phone_number"] = numberid_profile.text.toString()
                    update["AboutMe"] = txt_aboutMe.text.toString()
                    update["Location"] = location_profile.text.toString()
                    update["career"] = txt_Career.text.toString()
                    update["imageURL"]=imtURL

                    ref.update(update)
                        .addOnSuccessListener {
                            Toast.makeText(this,"Success Edit",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,MainActivity::class.java))
                        }
                        .addOnFailureListener{
                            Toast.makeText(this,"Failer Edit",Toast.LENGTH_LONG).show()
                        }

                } else {
                    // Handle failures
                }
            }


                ?.addOnFailureListener{

            }
        }else{

            var db = FirebaseFirestore.getInstance()
            var ref = db.collection("users").document(fireUser!!.uid)
            val update = HashMap<String,Any>()
            update["username"] = profile_uesrname.text.toString()
            update["usermail"] =  email_profile.text.toString()
            update["phone_number"] = numberid_profile.text.toString()
            update["AboutMe"] = txt_aboutMe.text.toString()
            update["Location"] = location_profile.text.toString()
            update["career"] = txt_Career.text.toString()

            ref.update(update)
                .addOnSuccessListener {
                    Toast.makeText(this,"Success Edit",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,MainActivity::class.java))
                }
                .addOnFailureListener{
                    Toast.makeText(this,"Failer Edit",Toast.LENGTH_LONG).show()
                }
        }    }






    private fun showDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Uploading image ...")
        progressDialog!!.show()
    }

    private fun hideDialog(){
        if(progressDialog!!.isShowing)
            progressDialog!!.dismiss()
    }

}
