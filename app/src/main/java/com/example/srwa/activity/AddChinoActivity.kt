package com.example.srwa.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.srwa.Model.Chino
import com.example.srwa.databinding.ActivityAddChinoBinding
import com.google.firebase.database.FirebaseDatabase

class AddChinoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddChinoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdd.setOnClickListener {
            saveChinoToDatabase()
        }
    }

    private fun saveChinoToDatabase() {
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

        val chino = Chino(id, type, color, quantity)
        val database = FirebaseDatabase.getInstance().reference.child("chinos").child(id)
        database.setValue(chino).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Chino saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to save chino: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
