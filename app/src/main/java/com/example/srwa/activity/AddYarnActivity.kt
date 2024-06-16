package com.example.srwa.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.srwa.Model.Yarn
import com.example.srwa.databinding.ActivityAddYarnBinding
import com.google.firebase.database.FirebaseDatabase

class AddYarnActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddYarnBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddYarnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdd.setOnClickListener {
            saveYarnToDatabase()
        }
    }

    private fun saveYarnToDatabase() {
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

        val yarn = Yarn(id, type, color, quantity)
        val database = FirebaseDatabase.getInstance().reference.child("yarns").child(id)
        database.setValue(yarn).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Yarn saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save yarn: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
