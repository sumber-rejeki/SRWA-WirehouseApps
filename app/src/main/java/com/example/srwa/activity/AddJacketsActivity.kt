package com.example.srwa.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.srwa.Model.Jackets
import com.example.srwa.databinding.ActivityAddJacketsBinding
import com.google.firebase.database.FirebaseDatabase

class AddJacketsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddJacketsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJacketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdd.setOnClickListener {
            saveJacketToDatabase()
        }
    }

    private fun saveJacketToDatabase() {
        val id = binding.editTextId.text.toString().trim()
        val type = binding.editTextType.text.toString().trim()
        val color = binding.editTextColor.text.toString().trim()
        val quantityStr = binding.editTextQty.text.toString().trim()

        if (id.isEmpty() || type.isEmpty() || color.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = try {
            quantityStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show()
            return
        }

        val jacket = Jackets(id, type, color, quantity)
        val database = FirebaseDatabase.getInstance().reference.child("jackets").child(id)
        database.setValue(jacket).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Jacket saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save jacket: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
