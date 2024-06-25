package com.example.srwa.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.example.srwa.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    //DEKLARASI
    lateinit var editFullName: EditText
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var editPasswordConf: EditText
    lateinit var btnRegister: Button
    lateinit var btnLogin: TextView
    lateinit var progressDialog: ProgressDialog


    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //INISIALISASI
        editFullName = findViewById(R.id.full_name)
        editEmail = findViewById(R.id.email)
        editPassword = findViewById(R.id.password)
        editPasswordConf = findViewById(R.id.password_conf)
        btnRegister = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silahkan tunggu...")

        //BTNLOGIN
        btnLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        //BTNREGISTER
        btnRegister.setOnClickListener{
            if (editFullName.text.isNotEmpty() && editEmail.text.isNotEmpty() && editPassword.text.isNotEmpty()){
                if (editPassword.text.toString() == editPasswordConf.text.toString()){
                    //JALANKAN REGISTER
                    processRegister()
                }else{
                    Toast.makeText(this, "Konfirmasi kata sandi harus sama ",LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Silahkan isi dulu semua data",LENGTH_SHORT).show()
            }
        }
    }


    private fun processRegister(){
        val fullName = editFullName.text.toString()
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = fullName
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            progressDialog.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        .addOnFailureListener{ error2 ->
                            Toast.makeText(this, error2.localizedMessage ,LENGTH_SHORT).show()
                        }
                }else{
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener{ error ->
                Toast.makeText(this, error.localizedMessage ,LENGTH_SHORT).show()
            }
    }
}