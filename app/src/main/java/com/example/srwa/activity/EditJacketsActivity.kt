package com.example.srwa.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.srwa.Model.Jackets
import com.example.srwa.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditJacketsActivity : AppCompatActivity() {
    private lateinit var jacketId: String
    private lateinit var typeEditText: EditText
    private lateinit var colorEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_jackets)

        jacketId = intent.getStringExtra("jacketId") ?: ""
        typeEditText = findViewById(R.id.editTextType)
        colorEditText = findViewById(R.id.editTextColor)
        quantityEditText = findViewById(R.id.editTextQty)
        updateButton = findViewById(R.id.buttonUpdate)
        deleteButton = findViewById(R.id.buttonDelete)

        updateButton.setOnClickListener {
            editJacketInDatabase()
        }

        deleteButton.setOnClickListener {
            deleteJacketFromDatabase()
        }

        // Retrieve Fabric details from the database and populate the EditText fields
        val database = FirebaseDatabase.getInstance().reference.child("jackets").child(jacketId)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val jacket = dataSnapshot.getValue(Jackets::class.java)
                jacket?.let {
                    typeEditText.setText(it.type)
                    colorEditText.setText(it.color)
                    quantityEditText.setText(it.qty.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@EditJacketsActivity, "Failed to load jacket data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editJacketInDatabase() {
        val type = typeEditText.text.toString().trim()
        val color = colorEditText.text.toString().trim()
        val quantityString = quantityEditText.text.toString().trim()

        if (type.isEmpty() || color.isEmpty() || quantityString.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = quantityString.toInt()

        val database = FirebaseDatabase.getInstance().reference.child("jackets").child(jacketId)
        val jacket = Jackets(jacketId, type, color, quantity)

        database.setValue(jacket)
            .addOnSuccessListener {
                Toast.makeText(this, "Jackets updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update jackets", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteJacketFromDatabase() {
        val database = FirebaseDatabase.getInstance().reference.child("jackets").child(jacketId)
        database.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Jackets deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete Jackets", Toast.LENGTH_SHORT).show()
            }
    }
}