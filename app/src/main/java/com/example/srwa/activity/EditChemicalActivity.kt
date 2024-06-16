package com.example.srwa.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.srwa.Model.Chemical
import com.example.srwa.Model.Yarn
import com.example.srwa.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class EditChemicalActivity : AppCompatActivity() {

    private lateinit var chemicalId: String
    private lateinit var typeEditText: EditText
    private lateinit var colorEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_chemical)

        chemicalId = intent.getStringExtra("chemicalId") ?: ""
        typeEditText = findViewById(R.id.editTextType)
        colorEditText = findViewById(R.id.editTextColor)
        quantityEditText = findViewById(R.id.editTextQty)
        updateButton = findViewById(R.id.buttonUpdate)
        deleteButton = findViewById(R.id.buttonDelete)

        updateButton.setOnClickListener {
            editChemicalInDatabase()
        }

        deleteButton.setOnClickListener {
            deleteChemicalFromDatabase()
        }

        // Retrieve Yarn details from the database and populate the EditText fields
        val database = FirebaseDatabase.getInstance().reference.child("chemicals").child(chemicalId)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val yarn = dataSnapshot.getValue(Yarn::class.java)
                yarn?.let {
                    typeEditText.setText(it.type)
                    colorEditText.setText(it.color)
                    quantityEditText.setText(it.qty.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@EditChemicalActivity, "Failed to load yarn data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editChemicalInDatabase() {
        val type = typeEditText.text.toString().trim()
        val color = colorEditText.text.toString().trim()
        val quantityString = quantityEditText.text.toString().trim()

        if (type.isEmpty() || color.isEmpty() || quantityString.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = quantityString.toInt()

        val database = FirebaseDatabase.getInstance().reference.child("chemicals").child(chemicalId)
        val chemical = Chemical(chemicalId, type, color, quantity)

        database.setValue(chemical)
            .addOnSuccessListener {
                Toast.makeText(this, "Chemical updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update chemical", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteChemicalFromDatabase() {
        val database = FirebaseDatabase.getInstance().reference.child("chemicals").child(chemicalId)
        database.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Chemical deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete chemical", Toast.LENGTH_SHORT).show()
            }
    }
}
