package com.example.srwa.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.srwa.Model.Chemical
import com.example.srwa.databinding.ActivityAddChemicalBinding
import com.google.firebase.database.FirebaseDatabase

class AddChemicalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddChemicalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChemicalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdd.setOnClickListener {
            saveChemicalToDatabase()
        }
    }

    private fun saveChemicalToDatabase() {
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

        val chemical = Chemical(id, type, color, quantity)
        val database = FirebaseDatabase.getInstance().reference.child("chemicals").child(id)
        database.setValue(chemical).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Chemical saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save chemical: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
