package com.example.srwa.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.srwa.Model.Chino
import com.example.srwa.Model.Yarn
import com.example.srwa.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class EditChinoActivity : AppCompatActivity() {

    private lateinit var chinoId: String
    private lateinit var typeEditText: EditText
    private lateinit var colorEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_chino)

        chinoId = intent.getStringExtra("chinoId") ?: ""
        typeEditText = findViewById(R.id.editTextType)
        colorEditText = findViewById(R.id.editTextColor)
        quantityEditText = findViewById(R.id.editTextQty)
        updateButton = findViewById(R.id.buttonUpdate)
        deleteButton = findViewById(R.id.buttonDelete)

        updateButton.setOnClickListener {
            editChinoInDatabase()
        }

        deleteButton.setOnClickListener {
            deleteChinoFromDatabase()
        }

        // Retrieve Yarn details from the database and populate the EditText fields
        val database = FirebaseDatabase.getInstance().reference.child("chinos").child(chinoId)
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
                Toast.makeText(this@EditChinoActivity, "Failed to load yarn data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editChinoInDatabase() {
        val type = typeEditText.text.toString().trim()
        val color = colorEditText.text.toString().trim()
        val quantityString = quantityEditText.text.toString().trim()

        if (type.isEmpty() || color.isEmpty() || quantityString.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity = quantityString.toInt()

        val database = FirebaseDatabase.getInstance().reference.child("chinos").child(chinoId)
        val chino = Chino(chinoId, type, color, quantity)

        database.setValue(chino)
            .addOnSuccessListener {
                Toast.makeText(this, "Chino updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update chino", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteChinoFromDatabase() {
        val database = FirebaseDatabase.getInstance().reference.child("chinos").child(chinoId)
        database.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Chino deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete Chino", Toast.LENGTH_SHORT).show()
            }
    }
}
