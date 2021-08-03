package com.example.finalprojectlab2.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.finalprojectlab2.Activity.ProdcutAndCatalog.AddSwitch
import com.example.finalprojectlab2.Fragment.CatalogFragment
import com.example.finalprojectlab2.Fragment.ProfileFragment
import com.example.finalprojectlab2.Fragment.SearchFragment
import com.example.finalprojectlab2.R
import com.example.projectlab.Fragment.Main_show.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.iammert.library.readablebottombar.ReadableBottomBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ViewModel
    val fireUser = FirebaseAuth.getInstance().currentUser

    companion object {
        val ID_HOME = 1
        val ID_SEARCH =2
        val ID_CATALOG = 3
        val ID_PROFILE = 4

    }




    var meo: MeowBottomNavigation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
//        supportActionBar!!.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn()
        floating_action_button.setOnClickListener {
           val i =  Intent(this, AddSwitch::class.java)
            startActivity(i)
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        floating_action_button.hide()

        Log.d("aaa2" , fireUser!!.email)
        if (fireUser!!.email.equals("admin@gmail.com")){
        //   floating_action_button.visibility = View.GONE
            floating_action_button.show()
        }

    }




    fun btn() {
        meo = bottom_nav
        meo!!.add(MeowBottomNavigation.Model(1,
            R.drawable.home
        ))
        meo!!.add(MeowBottomNavigation.Model(2,
            R.drawable.ic_search
        ))
        meo!!.add(MeowBottomNavigation.Model(3,
            R.drawable.about
        ))
        meo!!.add(MeowBottomNavigation.Model(4,
            R.drawable.login
        ))

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                HomeFragment()
            ).commit()
        meo!!.setOnClickMenuListener { item ->
            Toast.makeText(applicationContext, "Clicked item" + item.id, Toast.LENGTH_SHORT).show()

           if(item.id == 2){
                startActivity(  Intent(this@MainActivity, SearchActivty::class.java) )


            }

        }


        meo!!.setOnShowListener { item ->
            var select_fragment: Fragment? = null

            when (item.id) {
                ID_HOME -> select_fragment =
                    HomeFragment()
                ID_SEARCH -> select_fragment =
                    SearchFragment()
                ID_PROFILE -> select_fragment =
                    ProfileFragment()
                ID_CATALOG -> select_fragment =
                    CatalogFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, select_fragment!!).commit()

        }
    }


}
