package com.example.srwa.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.srwa.R
import com.example.srwa.ui.DashboardFragment
import com.example.srwa.ui.FabricFragment
import com.example.srwa.ui.InputFragment
import com.example.srwa.ui.OutputFragment
import com.example.srwa.ui.ProductionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    //DEKLARASI
//    lateinit var textFullName: TextView
//    lateinit var textEmail: TextView
    private lateinit var navview:BottomNavigationView

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mengatur window flag agar elemen UI tidak memasuki area status bar
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }


        navview=findViewById(R.id.nav_view)
        replace(DashboardFragment())

        navview.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_dasboard->replace(DashboardFragment())
                R.id.navigation_input->replace(InputFragment())
                R.id.navigation_production->replace(ProductionFragment())
                R.id.navigation_output->replace(OutputFragment())
            }
            true
        }

        //INISIALISASI
//        textFullName = findViewById(R.id.full_name)
//        textEmail = findViewById(R.id.email)

        val firebaseuser = firebaseAuth.currentUser
        if (firebaseuser!=null){
//            textFullName.text = firebaseuser.displayName
//            textEmail.text = firebaseuser.email
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, InputFragment())
//                .commit()
//        }


    }

    private fun replace(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val fragmenttransition=fragmentManager.beginTransaction()
        fragmenttransition.replace(R.id.nav_host,fragment)
        fragmenttransition.commit()
    }
}